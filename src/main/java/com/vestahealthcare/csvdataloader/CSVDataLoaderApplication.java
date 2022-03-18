package com.vestahealthcare.csvdataloader;

import com.vestahealthcare.csvdataloader.migrator.MigrationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@AllArgsConstructor
@SpringBootApplication
@EnableTransactionManagement
public class CSVDataLoaderApplication {
  private final MigrationService migrationService;
  public static void main(final String[] args) {
      SpringApplication.run(CSVDataLoaderApplication.class,args);

  }

  @EventListener(ApplicationReadyEvent.class)
  public void startMigration() throws IOException {
    final String migratorClassName = System.getProperty("migrators", "");
    if (migratorClassName != null && !migratorClassName.isEmpty()) {
      log.info("The following migrator would would be run: " + migratorClassName);

      final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      reader.read();
      reader.close();

      migrationService.run(migratorClassName);
    } else {
      log.info("No Migrator set to run, Skipping the backfill");
    }
  }
}
