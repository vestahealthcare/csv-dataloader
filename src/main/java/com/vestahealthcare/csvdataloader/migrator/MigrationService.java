package com.vestahealthcare.csvdataloader.migrator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MigrationService {
  private final List<? extends Migrator<?>> migrators;

  public MigrationService(final List<? extends Migrator<?>> migrators) {
    this.migrators = migrators;
  }

  public final void run(final String migratorName) {
    migrators.stream()
        .filter(migrator -> migrator.getClass().getSimpleName().startsWith(migratorName))
        .forEach(migrator -> {
          try {
            migrator.init();
            migrator.migrate();
          } catch (final Exception ex) {
            log.error("Migrator {} failed.", migrator.getClass().getSimpleName(), ex);
          } finally {
            migrator.finish();
            System.gc();
          }
        });
  }
}
