package com.vestahealthcare.csvdataloader.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.vestahealthcare.csvdataloader.target",
    entityManagerFactoryRef = "targetEntityManager",
    transactionManagerRef = "targetTransactionManager")
public class TargetAutoConfiguration {
  private final Environment env;

  public TargetAutoConfiguration(final Environment env) {
    this.env = env;
  }

  @Bean("targetDataSource")
  @Primary
  public DataSource targetDataSource() {
    return targetDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class)
            .build();
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.target-datasource")
  public DataSourceProperties targetDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean("targetEntityManager")
  public LocalContainerEntityManagerFactoryBean targetEntityManager() {
    final LocalContainerEntityManagerFactoryBean em
            = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(targetDataSource());
    em.setPackagesToScan("com.vestahealthcare.csvdataloader.target");

    final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);
    final HashMap<String, Object> properties = new HashMap<>();
    properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
    properties.put("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));
    properties.put("hibernate.physical_naming_strategy", env.getProperty("spring.jpa.hibernate.naming.physical-strategy"));
    em.setJpaPropertyMap(properties);

    return em;
  }

  @Bean("targetTransactionManager")
  public PlatformTransactionManager targetTransactionManager() {
    final JpaTransactionManager transactionManager
            = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(
            targetEntityManager().getObject());
    return transactionManager;
  }
}

