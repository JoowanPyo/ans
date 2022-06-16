package com.gemiso.zodiac.app.ArticleTag.mapper;

import com.gemiso.zodiac.app.ArticleTag.ArticleTag;
import com.gemiso.zodiac.app.ArticleTag.dto.ArticleTagDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleTagMapper extends GenericMapper<ArticleTagDTO, ArticleTag, ArticleTagDTO> {
}
