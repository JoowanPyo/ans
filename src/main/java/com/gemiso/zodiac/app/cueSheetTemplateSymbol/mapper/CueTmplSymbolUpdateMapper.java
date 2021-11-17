package com.gemiso.zodiac.app.cueSheetTemplateSymbol.mapper;

import com.gemiso.zodiac.app.cueSheetTemplateSymbol.CueTmplSymbol;
import com.gemiso.zodiac.app.cueSheetTemplateSymbol.dto.CueTmplSymbolUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueTmplSymbolUpdateMapper extends GenericMapper<CueTmplSymbolUpdateDTO
        , CueTmplSymbol, CueTmplSymbolUpdateDTO> {
}
