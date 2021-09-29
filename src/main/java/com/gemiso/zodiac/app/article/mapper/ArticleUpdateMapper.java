package com.gemiso.zodiac.app.article.mapper;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.dto.ArticleUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleUpdateMapper extends GenericMapper<ArticleUpdateDTO, Article, ArticleUpdateDTO> {
}
