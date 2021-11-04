package com.gemiso.zodiac.app.articleOrder.mapper;

import com.gemiso.zodiac.app.articleOrder.ArticleOrderFile;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderFileUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleOrderFileUpdateMapper extends GenericMapper<ArticleOrderFileUpdateDTO,
        ArticleOrderFile, ArticleOrderFileUpdateDTO> {
}
