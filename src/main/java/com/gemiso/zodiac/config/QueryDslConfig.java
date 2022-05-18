package com.gemiso.zodiac.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class QueryDslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    /*@Bean
    public JPAQueryFactory jpaQueryFactory() {
        LocalContainerEntityManagerFactoryBean fatory = new LocalContainerEntityManagerFactoryBean(entityManager);
        fatory.setPackagesToScan("");
        fatory.setDataSource();

        return new JPAQueryFactory(entityManager);
    }*/
}