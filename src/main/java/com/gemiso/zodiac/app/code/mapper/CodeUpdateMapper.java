package com.gemiso.zodiac.app.code.mapper;

import com.gemiso.zodiac.app.code.Code;
import com.gemiso.zodiac.app.code.dto.CodeUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CodeUpdateMapper extends GenericMapper<CodeUpdateDTO, Code, CodeUpdateDTO> {
}
