package com.gemiso.zodiac.app.articleMedia.mapper;

import com.gemiso.zodiac.app.articleMedia.ArticleMedia;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaSimpleDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleMediaSimpleMapper extends GenericMapper<ArticleMediaSimpleDTO, ArticleMedia, ArticleMediaSimpleDTO> {
}
