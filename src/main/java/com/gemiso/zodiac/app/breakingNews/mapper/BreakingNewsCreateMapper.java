package com.gemiso.zodiac.app.breakingNews.mapper;

import com.gemiso.zodiac.app.breakingNews.BreakingNews;
import com.gemiso.zodiac.app.breakingNews.dto.BreakingNewsCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BreakingNewsCreateMapper extends GenericMapper<BreakingNewsCreateDTO, BreakingNews, BreakingNewsCreateDTO> {
}
