package com.gemiso.zodiac.app.articleMedia.mapper;

import com.gemiso.zodiac.app.articleMedia.ArticleMedia;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleMediaUpdateMapper extends GenericMapper<ArticleMediaUpdateDTO, ArticleMedia, ArticleMediaUpdateDTO> {
}
