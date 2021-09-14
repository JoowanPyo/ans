package com.gemiso.zodiac.app.CueItemCap.mapper;

import com.gemiso.zodiac.app.CueItemCap.CueItemCap;
import com.gemiso.zodiac.app.CueItemCap.dto.CueItemCapDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueItemCapMapper extends GenericMapper<CueItemCapDTO, CueItemCap, CueItemCapDTO> {
}
