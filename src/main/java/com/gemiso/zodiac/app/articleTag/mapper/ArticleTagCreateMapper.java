package com.gemiso.zodiac.app.articleTag.mapper;

import com.gemiso.zodiac.app.articleTag.ArticleTag;
import com.gemiso.zodiac.app.articleTag.dto.ArticleTagCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleTagCreateMapper extends GenericMapper<ArticleTagCreateDTO, ArticleTag, ArticleTagCreateDTO> {
}
