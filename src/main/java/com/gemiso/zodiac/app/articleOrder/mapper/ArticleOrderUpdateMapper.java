package com.gemiso.zodiac.app.articleOrder.mapper;

import com.gemiso.zodiac.app.articleOrder.ArticleOrder;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleOrderUpdateMapper extends GenericMapper<ArticleOrderUpdateDTO, ArticleOrder, ArticleOrderUpdateDTO> {
}
