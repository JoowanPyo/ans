package com.gemiso.zodiac.app.articleOrder.mapper;

import com.gemiso.zodiac.app.articleOrder.ArticleOrder;
import com.gemiso.zodiac.app.articleOrder.ArticleOrderFile;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderFileDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleOrderFileMapper extends GenericMapper<ArticleOrderFileDTO , ArticleOrderFile, ArticleOrderFileDTO> {
}
