package com.gemiso.zodiac.app.elasticsearch.mapper;

import com.gemiso.zodiac.app.elasticsearch.ElasticSearchArticle;
import com.gemiso.zodiac.app.elasticsearch.ElasticSearchArticleDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import net.minidev.json.writer.ArraysMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ElasticSearchArticleMapper extends GenericMapper<ElasticSearchArticleDTO, ElasticSearchArticle, ElasticSearchArticleDTO> {
}
