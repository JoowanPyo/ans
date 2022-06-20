package com.gemiso.zodiac.app.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticSearchArticleRepository  extends ElasticsearchRepository<ElasticSearchArticle, Long>, ElasticSearchArticleCustom {
}
