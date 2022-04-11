package com.gemiso.zodiac.database;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties
public class DataSourceproperties {

    @Bean(name = "mainDataSource")
    @Qualifier("mainDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari.main")
    public DataSource masterDataSource(){
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "subDataSource")
    @Qualifier("subDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.sub")
    public DataSource secondDataSource(){
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

}
