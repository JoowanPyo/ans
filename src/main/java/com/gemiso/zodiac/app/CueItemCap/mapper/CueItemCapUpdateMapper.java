package com.gemiso.zodiac.app.CueItemCap.mapper;

import com.gemiso.zodiac.app.CueItemCap.CueItemCap;
import com.gemiso.zodiac.app.CueItemCap.dto.CueItemCapUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueItemCapUpdateMapper extends GenericMapper<CueItemCapUpdateDTO, CueItemCap, CueItemCapUpdateDTO> {
}
