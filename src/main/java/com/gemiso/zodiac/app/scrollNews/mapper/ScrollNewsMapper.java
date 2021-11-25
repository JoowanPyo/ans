package com.gemiso.zodiac.app.scrollNews.mapper;

import com.gemiso.zodiac.app.scrollNews.ScrollNews;
import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScrollNewsMapper extends GenericMapper<ScrollNewsDTO, ScrollNews, ScrollNewsDTO> {
}
