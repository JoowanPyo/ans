package com.gemiso.zodiac.app.program.mapper;

import com.gemiso.zodiac.app.program.Program;
import com.gemiso.zodiac.app.program.dto.ProgramCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProgramCrateMapper extends GenericMapper<ProgramCreateDTO, Program,ProgramCreateDTO> {
}
