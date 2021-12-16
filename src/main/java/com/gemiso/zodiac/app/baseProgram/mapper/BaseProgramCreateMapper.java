package com.gemiso.zodiac.app.baseProgram.mapper;

import com.gemiso.zodiac.app.baseProgram.BaseProgram;
import com.gemiso.zodiac.app.baseProgram.dto.BaseProgramCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BaseProgramCreateMapper extends GenericMapper<BaseProgramCreateDTO, BaseProgram , BaseProgramCreateDTO> {
}
