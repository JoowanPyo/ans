package com.gemiso.zodiac.app.code.mapper;

import com.gemiso.zodiac.app.code.Code;
import com.gemiso.zodiac.app.code.dto.CodeDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CodeMapper extends GenericMapper<CodeDTO, Code, CodeDTO> {

    List<CodeDTO> toDtoList(List<Code> codeList);
}
