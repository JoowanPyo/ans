package com.gemiso.zodiac.app.cueSheetItemCap.mapper;

import com.gemiso.zodiac.app.cueSheetItemCap.CueSheetItemCap;
import com.gemiso.zodiac.app.cueSheetItemCap.dto.CueSheetItemCapUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueSheetItemCapUpdateMapper extends GenericMapper<CueSheetItemCapUpdateDTO, CueSheetItemCap, CueSheetItemCapUpdateDTO> {
}
