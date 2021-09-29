package com.gemiso.zodiac.app.cueSheetItemCap.mapper;

import com.gemiso.zodiac.app.cueSheetItemCap.CueSheetItemCap;
import com.gemiso.zodiac.app.cueSheetItemCap.dto.CueSheetItemCapCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueSheetItemCapCreateMapper extends GenericMapper<CueSheetItemCapCreateDTO, CueSheetItemCap, CueSheetItemCapCreateDTO> {
}
