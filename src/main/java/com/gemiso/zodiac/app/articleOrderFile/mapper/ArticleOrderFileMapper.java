package com.gemiso.zodiac.app.articleOrderFile.mapper;

import com.gemiso.zodiac.app.articleOrderFile.ArticleOrderFile;
import com.gemiso.zodiac.app.articleOrderFile.dto.ArticleOrderFileDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleOrderFileMapper extends GenericMapper<ArticleOrderFileDTO , ArticleOrderFile, ArticleOrderFileDTO> {
}
