package com.gemiso.zodiac.app.cueSheetItem.mapper;

import com.gemiso.zodiac.app.cueSheetItem.CueSheetItemSymbol;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSymbolUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueSheetItemSymbolUpdateMapper extends GenericMapper<CueSheetItemSymbolUpdateDTO,
        CueSheetItemSymbol, CueSheetItemSymbolUpdateDTO> {
}
