package com.gemiso.zodiac.app.articleCapHist.mapper;

import com.gemiso.zodiac.app.articleCapHist.ArticleCapHist;
import com.gemiso.zodiac.app.articleCapHist.dto.ArticleCapHistCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleCapHistCreateMapper extends GenericMapper<ArticleCapHistCreateDTO, ArticleCapHist, ArticleCapHistCreateDTO> {
}
