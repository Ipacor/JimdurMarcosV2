package com.diedari.jimdur.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EntityScan(basePackages = "com.diedari.jimdur.model.security")
@EnableJpaRepositories(
    basePackages = "com.diedari.jimdur.repository.security",
    entityManagerFactoryRef = "securityEntityManagerFactory",
    transactionManagerRef = "securityTransactionManager"
)
@EnableConfigurationProperties(SecurityDataSourceProperties.class)
public class SecurityDataSourceConfig {

    private final Environment env;

    public SecurityDataSourceConfig(Environment env) {
        this.env = env;
    }

    @Primary
    @Bean(name = "securityDataSource")
    public DataSource securityDataSource(SecurityDataSourceProperties props) {
        System.out.println("[DEBUG] securityDataSource url: " + props.getUrl());
        return org.springframework.boot.jdbc.DataSourceBuilder.create()
                .type(com.zaxxer.hikari.HikariDataSource.class)
                .url(props.getUrl())
                .username(props.getUsername())
                .password(props.getPassword())
                .driverClassName(props.getDriverClassName())
                .build();
    }

    @Primary
    @Bean(name = "securityEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean securityEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("securityDataSource") DataSource dataSource) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.security.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.security.properties.hibernate.dialect"));
        properties.put("hibernate.format_sql", env.getProperty("spring.jpa.security.properties.hibernate.format_sql"));
        properties.put("hibernate.show_sql", env.getProperty("spring.jpa.security.show-sql"));
        return builder
                .dataSource(dataSource)
                .packages("com.diedari.jimdur.model.security")
                .persistenceUnit("security")
                .properties(properties)
                .build();
    }

    @Primary
    @Bean(name = "securityTransactionManager")
    public PlatformTransactionManager securityTransactionManager(
            @Qualifier("securityEntityManagerFactory") LocalContainerEntityManagerFactoryBean securityEntityManagerFactory) {
        return new JpaTransactionManager(securityEntityManagerFactory.getObject());
    }
} 