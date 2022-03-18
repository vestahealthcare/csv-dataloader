package com.vestahealthcare.csvdataloader.migrator;

import com.vestahealthcare.csvdataloader.enums.TransactionType;
import com.vestahealthcare.csvdataloader.source.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract  class Migrator<S> {
  public abstract List<Object> transform(S source);

  public abstract void init();

  public abstract void finish();

  protected final EntityManager targetEntityManager;
  protected final FileRepository repository;
  protected final TransactionType objectTransactionType;

  @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
  private int batchSize;

  public Migrator(final EntityManager targetEntityManager, final FileRepository repository, final TransactionType objectTransactionType) {
    this.targetEntityManager = targetEntityManager;
    this.objectTransactionType = objectTransactionType;
    this.repository = repository;
  }

  protected <E> List<E> findAll(final EntityManager entityManager, final Class<E> clazz) {
    final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    final CriteriaQuery<E> cq = cb.createQuery(clazz);
    final Root<E> rootEntry = cq.from(clazz);
    final CriteriaQuery<E> all = cq.select(rootEntry).orderBy(cb.asc(rootEntry.get("id")));
    final TypedQuery<E> allQuery = entityManager.createQuery(all);
    return allQuery.getResultList();
  }

  @Transactional("targetTransactionManager")
  public void migrate() {
    log.info("Loading source entities");

    final List<S> sourceEntities = repository.findAll();
    if (sourceEntities != null && !sourceEntities.isEmpty()) {
      log.info("Total # of entities to be processed: {}", sourceEntities.size());

      final long startMillis = System.currentTimeMillis();

      int totalBatchSize = -1;
      final List<Object> transformationResult = new ArrayList<>();
      for (int i = 0; i < sourceEntities.size(); i++) {
        final S sourceEntity = sourceEntities.get(i);

        log.info("{}/{} - Processing {}", i + 1, sourceEntities.size(), sourceEntity);
        try {
          final List<Object> entities = transform(sourceEntity);
          transformationResult.addAll(entities);
          if (totalBatchSize == -1) {
            totalBatchSize = entities.size() * batchSize;
          }
        } catch (final IllegalStateException ex) {
          log.error("{} Entity: {}", ex.getMessage(), sourceEntity);
        } catch (final Exception ex) {
          log.error("{}/{} - {}", i + 1, sourceEntities.size(), sourceEntity, ex);
        }
      }

      long batchStartMillis = System.currentTimeMillis();
      for (int i = 0; i < transformationResult.size(); i++) {
        if (i != 0 && i % totalBatchSize == 0) {
          log.info("Synchronizing the persistence context to the underlying database.");
          targetEntityManager.flush();
          targetEntityManager.clear();
          log.info("Synchronization completed in {} ms.", (System.currentTimeMillis() - batchStartMillis));
          log.info("Migration has taken {} ms so far.", (System.currentTimeMillis() - startMillis));
          batchStartMillis = System.currentTimeMillis();
        }
        final Object ent = transformationResult.get(i);

        try {
          log.info("{}/{} - Persisting {}", i + 1, transformationResult.size(), ent);
          if (TransactionType.INSERT.equals(objectTransactionType)) {
            targetEntityManager.persist(ent);
          } else if (TransactionType.UPDATE.equals(objectTransactionType)) {
            targetEntityManager.merge(ent);
          } else {
            Object managedEntity = targetEntityManager.merge(ent);
            targetEntityManager.remove(managedEntity);
          }
        } catch (final Exception ex) {
          log.error("{}/{} - {}", i + 1, transformationResult.size(), ent, ex);
        }
      }

      log.info("Migration was completed in {} ms.", (System.currentTimeMillis() - startMillis));
    } else {
      log.info("Couldnt load from the file, Skipping the migration");
    }
  }
}
