package com.gemiso.zodiac.app.articleOrderFile.mapper;

import com.gemiso.zodiac.app.articleOrderFile.ArticleOrderFile;
import com.gemiso.zodiac.app.articleOrderFile.dto.ArticleOrderFileCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleOrderFileCreateMapper extends GenericMapper<ArticleOrderFileCreateDTO, ArticleOrderFile,
                                                                        ArticleOrderFileCreateDTO> {
}
