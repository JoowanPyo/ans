package com.gemiso.zodiac.app.cueSheetHist.mapper;

import com.gemiso.zodiac.app.cueSheetHist.CueSheetHist;
import com.gemiso.zodiac.app.cueSheetHist.dto.CueSheetHistDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueSheetHistMapper extends GenericMapper<CueSheetHistDTO, CueSheetHist, CueSheetHistDTO> {
}
