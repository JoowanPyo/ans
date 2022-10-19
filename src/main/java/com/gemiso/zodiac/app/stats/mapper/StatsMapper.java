package com.gemiso.zodiac.app.stats.mapper;

import com.gemiso.zodiac.app.stats.Stats;
import com.gemiso.zodiac.app.stats.dto.StatsDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatsMapper extends GenericMapper<StatsDTO, Stats, StatsDTO> {
}
