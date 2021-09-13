package com.gemiso.zodiac.app.program.mapper;

import com.gemiso.zodiac.app.program.Program;
import com.gemiso.zodiac.app.program.dto.ProgramUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProgramUpdateMapper extends GenericMapper<ProgramUpdateDTO, Program, ProgramUpdateDTO> {
}
