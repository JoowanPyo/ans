package com.gemiso.zodiac.app.breakingNews.mapper;

import com.gemiso.zodiac.app.breakingNews.BreakingNews;
import com.gemiso.zodiac.app.breakingNews.dto.BreakingNewsUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BreakingNewsUpdateMapper extends GenericMapper<BreakingNewsUpdateDTO, BreakingNews, BreakingNewsUpdateDTO> {
}
