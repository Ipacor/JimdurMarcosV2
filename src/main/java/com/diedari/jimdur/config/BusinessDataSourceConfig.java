package com.diedari.jimdur.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(BusinessDataSourceProperties.class)
@EntityScan(basePackages = "com.diedari.jimdur.model.business")
@EnableJpaRepositories(
    basePackages = "com.diedari.jimdur.repository.business",
    entityManagerFactoryRef = "businessEntityManagerFactory",
    transactionManagerRef = "businessTransactionManager"
)
public class BusinessDataSourceConfig {

    private final Environment env;

    public BusinessDataSourceConfig(Environment env) {
        this.env = env;
    }

    @Bean(name = "businessDataSource")
    public DataSource businessDataSource(BusinessDataSourceProperties props) {
        System.out.println("[DEBUG] businessDataSource url: " + props.getUrl());
        return org.springframework.boot.jdbc.DataSourceBuilder.create()
                .type(com.zaxxer.hikari.HikariDataSource.class)
                .url(props.getUrl())
                .username(props.getUsername())
                .password(props.getPassword())
                .driverClassName(props.getDriverClassName())
                .build();
    }

    @Bean(name = "businessEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean businessEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("businessDataSource") DataSource dataSource) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.business.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.business.properties.hibernate.dialect"));
        properties.put("hibernate.format_sql", env.getProperty("spring.jpa.business.properties.hibernate.format_sql"));
        properties.put("hibernate.show_sql", env.getProperty("spring.jpa.business.show-sql"));
        return builder
                .dataSource(dataSource)
                .packages("com.diedari.jimdur.model.business")
                .persistenceUnit("business")
                .properties(properties)
                .build();
    }

    @Bean(name = "businessTransactionManager")
    public PlatformTransactionManager businessTransactionManager(
            @Qualifier("businessEntityManagerFactory") LocalContainerEntityManagerFactoryBean businessEntityManagerFactory) {
        return new JpaTransactionManager(businessEntityManagerFactory.getObject());
    }
} 