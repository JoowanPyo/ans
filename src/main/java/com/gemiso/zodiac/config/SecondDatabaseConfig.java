package com.gemiso.zodiac.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


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
    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(EntityManagerFactoryBuilder builder){

        return builder
                .dataSource(subDataSource)
                .packages("com.gemiso.zodiac.core.mis")
                .persistenceUnit("sub")
                .build();
    }

    @Bean("subTransactionManager")
    public PlatformTransactionManager secondTransactionManager(EntityManagerFactoryBuilder builder){

        return new JpaTransactionManager(secondEntityManagerFactory(builder).getObject());
    }
}
