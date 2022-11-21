package com.encouragee.flyway;

import com.encouragee.DataAccessProperties;
import com.zoomint.encourage.common.eclipselink.Jsr310JpaConverters;
import com.zoomint.encourage.common.eclipselink.Slf4JEclipseLinkSessionLogger;
import com.zoomint.encourage.common.model.CommonEntity;
import org.eclipse.persistence.logging.SessionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;
import static org.eclipse.persistence.config.PersistenceUnitProperties.LOGGING_LOGGER;

@Configuration
@Profile("flyway")
@ComponentScan(basePackages = {"com.encouragee.*"}, basePackageClasses = {DataAccessProperties.class, })
public class FlywayConfiguration {
    @Autowired
    public DataAccessProperties properties;
    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(properties.getDatabase().getDriver());
        dataSource.setUrl(properties.getDatabase().getAddress());
        dataSource.setUsername(properties.getDatabase().getUsername());
        dataSource.setPassword(properties.getDatabase().getPassword());
        return dataSource;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(entityManagerFactory);
        jpaTransactionManager.setDataSource(dataSource());
        return jpaTransactionManager;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setPackagesToScan(
                CommonEntity.class.getPackage().getName(),
//                EventEntity.class.getPackage().getName(),
//                UserStateConverter.class.getPackage().getName(),
                Jsr310JpaConverters.class.getPackage().getName());
        factory.setDataSource(dataSource());
        factory.setJpaVendorAdapter(jpaVendorAdapter());
        factory.setJpaDialect(jpaDialect());
        factory.setSharedCacheMode(SharedCacheMode.NONE); // cache fully disabled
        factory.getJpaPropertyMap().put(WEAVING, "false");
        factory.getJpaPropertyMap().put(LOGGING_PARAMETERS, "true");
        factory.getJpaPropertyMap().put(LOGGING_LEVEL, SessionLog.INFO_LABEL);
        factory.getJpaPropertyMap().put(LOGGING_LOGGER, Slf4JEclipseLinkSessionLogger.class.getName());
        return factory;
    }

    @Bean
    public EclipseLinkJpaDialect jpaDialect() {
        EclipseLinkJpaDialect jpaDialect = new EclipseLinkJpaDialect();
        jpaDialect.setLazyDatabaseTransaction(true);
        return jpaDialect;
    }

    @Bean
    public EclipseLinkJpaVendorAdapter jpaVendorAdapter() {
        EclipseLinkJpaVendorAdapter jpaVendorAdapter = new EclipseLinkJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.setDatabase(properties.getDatabase().getType());
        return jpaVendorAdapter;
    }
}
