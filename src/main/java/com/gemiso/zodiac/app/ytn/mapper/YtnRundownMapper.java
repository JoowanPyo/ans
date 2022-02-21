package com.gemiso.zodiac.app.ytn.mapper;

import com.gemiso.zodiac.app.ytn.YtnRundown;
import com.gemiso.zodiac.app.ytn.dto.YtnRundownDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface YtnRundownMapper extends GenericMapper<YtnRundownDTO, YtnRundown, YtnRundownDTO> {
}
