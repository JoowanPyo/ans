package com.gemiso.zodiac.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"com.gemiso.zodiac.core.mis"},
        entityManagerFactoryRef = "subEntityManager",
        transactionManagerRef = "subTransactionManager"

)
public class SecondDatabaseConfig {

    @Autowired
    @Qualifier("subDataSource")
    private DataSource subDataSource;

    @Bean(name = "subEntityManager")
    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                             @Qualifier("subDataSource")DataSource dataSource,
                                                                             Environment env){

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("sub.hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getRequiredProperty("sub.hibernate.dialect"));

        return builder
                .dataSource(subDataSource)
                .packages("com.gemiso.zodiac.core.mis")
                .persistenceUnit("sub")
                .properties(properties)
                .build();
    }

    @Bean("subTransactionManager")
    public PlatformTransactionManager secondTransactionManager(@Qualifier("subEntityManager")EntityManagerFactory builder){

        return new JpaTransactionManager(builder);
    }
}


  /*  @Bean("subTransactionManager")
    public PlatformTransactionManager secondTransactionManager(EntityManagerFactoryBuilder builder){

        return new JpaTransactionManager(secondEntityManagerFactory(builder).getObject());
    }*/