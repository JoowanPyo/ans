package com.gemiso.zodiac.app.elasticsearch;

import com.gemiso.zodiac.app.elasticsearch.articleEntity.ElasticSearchArticle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticSearchArticleRepository  extends ElasticsearchRepository<ElasticSearchArticle, Long>, ElasticSearchArticleCustom {
}
