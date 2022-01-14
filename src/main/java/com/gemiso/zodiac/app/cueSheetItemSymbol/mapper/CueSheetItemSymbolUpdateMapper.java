package com.gemiso.zodiac.app.cueSheetItemSymbol.mapper;

import com.gemiso.zodiac.app.cueSheetItemSymbol.CueSheetItemSymbol;
import com.gemiso.zodiac.app.cueSheetItemSymbol.dto.CueSheetItemSymbolUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueSheetItemSymbolUpdateMapper extends GenericMapper<CueSheetItemSymbolUpdateDTO,
        CueSheetItemSymbol, CueSheetItemSymbolUpdateDTO> {
}
