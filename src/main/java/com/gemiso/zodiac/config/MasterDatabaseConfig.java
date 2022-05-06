package com.gemiso.zodiac.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
        basePackages = {"com.gemiso.zodiac.app"},
        entityManagerFactoryRef = "mainEntityManager",
        transactionManagerRef = "mainTransactionManager"
)
public class MasterDatabaseConfig {


    @Autowired
    @Qualifier("mainDataSource")
    private DataSource mainDataSource;

    @Primary
    @Bean(name = "mainEntityManager")
    public LocalContainerEntityManagerFactoryBean masterEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                             @Qualifier("mainDataSource")DataSource dataSource,
                                                                             Environment env){

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("main.hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getRequiredProperty("main.hibernate.dialect"));

        return builder
                .dataSource(mainDataSource)
                .packages("com.gemiso.zodiac.app")
                .persistenceUnit("main")
                .properties(properties)
                .build();

    }

    @Primary
    @Bean("mainTransactionManager")
    public PlatformTransactionManager masterTransactionManager(@Qualifier("mainEntityManager") EntityManagerFactory builder){

        return new JpaTransactionManager(builder);

    }

}


    /*@Autowired
    private Environment env;

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean masterEntityManager(){

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(masterDataSource());

        //Entity 패키지 경로
        em.setPackagesToScan(new String[] {"com.gemiso.zodiac.app"});

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        //Hibernate설정
        HashMap<String, Object> properties = new HashMap<>();

        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")//prefix를 이용한 설정의 경우, 해당 어노테이션 선언 필요
    public DataSource masterDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager materTransactionManager(){
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        transactionManager.setEntityManagerFactory(masterEntityManager().getObject());

        return transactionManager;
    }*/
