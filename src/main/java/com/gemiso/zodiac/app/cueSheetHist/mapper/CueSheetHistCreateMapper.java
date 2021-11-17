package com.gemiso.zodiac.app.cueSheetHist.mapper;

import com.gemiso.zodiac.app.cueSheetHist.CueSheetHist;
import com.gemiso.zodiac.app.cueSheetHist.dto.CueSheetHistCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueSheetHistCreateMapper extends GenericMapper<CueSheetHistCreateDTO, CueSheetHist, CueSheetHistCreateDTO> {
}
