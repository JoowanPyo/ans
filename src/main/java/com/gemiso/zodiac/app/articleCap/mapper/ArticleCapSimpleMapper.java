package com.gemiso.zodiac.app.articleCap.mapper;

import com.gemiso.zodiac.app.articleCap.ArticleCap;
import com.gemiso.zodiac.app.articleCap.dto.ArticleCapSimpleDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleCapSimpleMapper extends GenericMapper<ArticleCapSimpleDTO, ArticleCap, ArticleCapSimpleDTO> {
}
