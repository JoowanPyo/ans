package com.gemiso.zodiac.app.ArticleOrder.mapper;

import com.gemiso.zodiac.app.ArticleOrder.ArticleOrder;
import com.gemiso.zodiac.app.ArticleOrder.dto.ArticleOrderDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleOrderMapper extends GenericMapper<ArticleOrderDTO, ArticleOrder, ArticleOrderDTO> {
}
