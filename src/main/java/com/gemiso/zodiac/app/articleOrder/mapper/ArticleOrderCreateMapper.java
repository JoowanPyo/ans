package com.gemiso.zodiac.app.articleOrder.mapper;

import com.gemiso.zodiac.app.articleOrder.ArticleOrder;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleOrderCreateMapper extends GenericMapper<ArticleOrderCreateDTO, ArticleOrder, ArticleOrderCreateDTO> {
}
