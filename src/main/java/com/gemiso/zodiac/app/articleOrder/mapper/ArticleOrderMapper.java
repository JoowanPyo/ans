package com.gemiso.zodiac.app.articleOrder.mapper;

import com.gemiso.zodiac.app.articleOrder.ArticleOrder;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleOrderMapper extends GenericMapper<ArticleOrderDTO, ArticleOrder, ArticleOrderDTO> {
}
