package com.gemiso.zodiac.app.cueSheetTemplateSymbol.mapper;

import com.gemiso.zodiac.app.cueSheetTemplateSymbol.CueTmplSymbol;
import com.gemiso.zodiac.app.cueSheetTemplateSymbol.dto.CueTmplSymbolDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueTmplSymbolMapper extends GenericMapper<CueTmplSymbolDTO,
        CueTmplSymbol, CueTmplSymbolDTO> {
}
