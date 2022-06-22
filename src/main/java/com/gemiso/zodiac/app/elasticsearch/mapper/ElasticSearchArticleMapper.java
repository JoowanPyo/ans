package com.gemiso.zodiac.app.elasticsearch.mapper;

import com.gemiso.zodiac.app.elasticsearch.articleEntity.ElasticSearchArticle;
import com.gemiso.zodiac.app.elasticsearch.articleDTO.ElasticSearchArticleDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ElasticSearchArticleMapper extends GenericMapper<ElasticSearchArticleDTO, ElasticSearchArticle, ElasticSearchArticleDTO> {
}
