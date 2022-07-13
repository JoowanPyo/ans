package com.gemiso.zodiac.app.articleTag.mapper;

import com.gemiso.zodiac.app.articleTag.ArticleTag;
import com.gemiso.zodiac.app.articleTag.dto.ArticleTagDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleTagMapper extends GenericMapper<ArticleTagDTO, ArticleTag, ArticleTagDTO> {
}
