package com.gemiso.zodiac.app.breakingNewsDetail.mapper;

import com.gemiso.zodiac.app.breakingNewsDetail.BreakingNewsDtl;
import com.gemiso.zodiac.app.breakingNewsDetail.dto.BreakingNewsDtlCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BreakingNewsDtlCreateMapper extends GenericMapper<BreakingNewsDtlCreateDTO, BreakingNewsDtl, BreakingNewsDtlCreateDTO> {
}
