package com.gemiso.zodiac.app.articleHist.mapper;

import com.gemiso.zodiac.app.articleHist.ArticleHist;
import com.gemiso.zodiac.app.articleHist.dto.ArticleHistDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleHistMapper extends GenericMapper<ArticleHistDTO, ArticleHist, ArticleHistDTO> {
}
