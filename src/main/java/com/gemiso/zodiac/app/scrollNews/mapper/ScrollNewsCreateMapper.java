package com.gemiso.zodiac.app.scrollNews.mapper;

import com.gemiso.zodiac.app.scrollNews.ScrollNews;
import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScrollNewsCreateMapper extends GenericMapper<ScrollNewsCreateDTO, ScrollNews, ScrollNewsCreateDTO> {
}
