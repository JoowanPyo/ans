package com.gemiso.zodiac.app.program.mapper;

import com.gemiso.zodiac.app.program.Program;
import com.gemiso.zodiac.app.program.dto.ProgramDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProgramMapper extends GenericMapper<ProgramDTO, Program, ProgramDTO> {
}
