package com.gemiso.zodiac.app.ArticleOrder.mapper;

import com.gemiso.zodiac.app.ArticleOrder.ArticleOrder;
import com.gemiso.zodiac.app.ArticleOrder.dto.ArticleOrderUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleOrderUpdateMapper extends GenericMapper<ArticleOrderUpdateDTO, ArticleOrder, ArticleOrderUpdateDTO> {
}
