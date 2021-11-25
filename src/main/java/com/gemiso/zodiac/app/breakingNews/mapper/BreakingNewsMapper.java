package com.gemiso.zodiac.app.breakingNews.mapper;

import com.gemiso.zodiac.app.breakingNews.BreakingNews;
import com.gemiso.zodiac.app.breakingNews.dto.BreakingNewsDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

import javax.persistence.ManyToOne;

@Mapper(componentModel = "spring")
public interface BreakingNewsMapper extends GenericMapper<BreakingNewsDTO, BreakingNews, BreakingNewsDTO> {
}
