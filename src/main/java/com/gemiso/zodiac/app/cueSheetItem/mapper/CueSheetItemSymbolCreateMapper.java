package com.gemiso.zodiac.app.cueSheetItem.mapper;

import com.gemiso.zodiac.app.cueSheetItem.CueSheetItemSymbol;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSymbolCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueSheetItemSymbolCreateMapper extends GenericMapper<CueSheetItemSymbolCreateDTO, CueSheetItemSymbol
                                                                    , CueSheetItemSymbolCreateDTO> {
}
