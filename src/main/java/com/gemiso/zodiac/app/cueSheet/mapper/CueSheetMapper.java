package com.gemiso.zodiac.app.cueSheet.mapper;

import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueSheetMapper extends GenericMapper<CueSheetDTO, CueSheet, CueSheetDTO> {
}
