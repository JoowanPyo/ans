package com.gemiso.zodiac.app.tagArticle.mapper;

import com.gemiso.zodiac.app.tagArticle.ArticleTag;
import com.gemiso.zodiac.app.tagArticle.dto.ArticleTagDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleTagMapper extends GenericMapper<ArticleTagDTO, ArticleTag, ArticleTagDTO> {
}
