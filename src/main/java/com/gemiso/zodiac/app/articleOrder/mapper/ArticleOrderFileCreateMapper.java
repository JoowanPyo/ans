package com.gemiso.zodiac.app.articleOrder.mapper;

import com.gemiso.zodiac.app.articleOrder.ArticleOrderFile;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderFileCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleOrderFileCreateMapper extends GenericMapper<ArticleOrderFileCreateDTO, ArticleOrderFile,
                                                                        ArticleOrderFileCreateDTO> {
}
