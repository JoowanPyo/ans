package com.gemiso.zodiac.app.breakingNewsDetail.mapper;

import com.gemiso.zodiac.app.breakingNewsDetail.BreakingNewsDtl;
import com.gemiso.zodiac.app.breakingNewsDetail.dto.BreakingNewsDtlDTO;
import com.gemiso.zodiac.app.breakingNewsDetail.dto.BreakingNewsDtlUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BreakingNewsDtlUpdateMapper extends GenericMapper<BreakingNewsDtlUpdateDTO, BreakingNewsDtl, BreakingNewsDtlUpdateDTO> {
}
