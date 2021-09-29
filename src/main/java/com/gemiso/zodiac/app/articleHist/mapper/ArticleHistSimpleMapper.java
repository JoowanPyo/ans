package com.gemiso.zodiac.app.articleHist.mapper;

import com.gemiso.zodiac.app.articleHist.ArticleHist;
import com.gemiso.zodiac.app.articleHist.dto.ArticleHistSimpleDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleHistSimpleMapper extends GenericMapper<ArticleHistSimpleDTO, ArticleHist, ArticleHistSimpleDTO> {
}
