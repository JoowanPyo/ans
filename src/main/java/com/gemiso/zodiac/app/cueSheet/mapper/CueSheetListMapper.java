package com.gemiso.zodiac.app.cueSheet.mapper;

import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetListDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueSheetListMapper extends GenericMapper<CueSheetListDTO, CueSheet, CueSheetListDTO> {
}
