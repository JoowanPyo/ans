package com.gemiso.zodiac.app.rundown.mapper;

import com.gemiso.zodiac.app.rundown.Rundown;
import com.gemiso.zodiac.app.rundown.dto.RundownCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RundownCreateMapper extends GenericMapper<RundownCreateDTO, Rundown, RundownCreateDTO> {
}
