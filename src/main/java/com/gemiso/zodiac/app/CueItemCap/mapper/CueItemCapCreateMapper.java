package com.gemiso.zodiac.app.CueItemCap.mapper;

import com.gemiso.zodiac.app.CueItemCap.CueItemCap;
import com.gemiso.zodiac.app.CueItemCap.dto.CueItemCapCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueItemCapCreateMapper extends GenericMapper<CueItemCapCreateDTO, CueItemCap, CueItemCapCreateDTO> {
}
