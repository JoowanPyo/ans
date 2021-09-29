package com.gemiso.zodiac.app.articleCap.mapper;

import com.gemiso.zodiac.app.articleCap.ArticleCap;
import com.gemiso.zodiac.app.articleCap.dto.ArticleCapDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleCapMapper extends GenericMapper<ArticleCapDTO, ArticleCap, ArticleCapDTO> {
}
