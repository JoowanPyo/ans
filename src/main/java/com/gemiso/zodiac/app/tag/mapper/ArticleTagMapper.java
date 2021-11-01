package com.gemiso.zodiac.app.tag.mapper;

import com.gemiso.zodiac.app.tag.ArticleTag;
import com.gemiso.zodiac.app.tag.dto.ArticleTagDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleTagMapper extends GenericMapper<ArticleTagDTO, ArticleTag, ArticleTagDTO> {
}
