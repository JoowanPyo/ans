package com.gemiso.zodiac.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.gemiso.zodiac.app.elasticsearch")
@ComponentScan(basePackages = { "com.gemiso.zodiac.app.elasticsearch" })
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    @Bean
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("172.20.10.42:9200")
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Bean(name = { "elasticsearchOperations", "elasticsearchTemplate" })
    public ElasticsearchOperations elasticsearchTemplate() {

        return new ElasticsearchRestTemplate(elasticsearchClient());
    }
}
