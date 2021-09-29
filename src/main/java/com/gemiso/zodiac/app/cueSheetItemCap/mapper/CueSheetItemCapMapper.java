package com.gemiso.zodiac.app.cueSheetItemCap.mapper;

import com.gemiso.zodiac.app.cueSheetItemCap.CueSheetItemCap;
import com.gemiso.zodiac.app.cueSheetItemCap.dto.CueSheetItemCapDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueSheetItemCapMapper extends GenericMapper<CueSheetItemCapDTO, CueSheetItemCap, CueSheetItemCapDTO> {
}
