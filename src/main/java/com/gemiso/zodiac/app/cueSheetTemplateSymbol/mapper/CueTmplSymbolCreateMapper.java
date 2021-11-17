package com.gemiso.zodiac.app.cueSheetTemplateSymbol.mapper;

import com.gemiso.zodiac.app.cueSheetTemplateSymbol.CueTmplSymbol;
import com.gemiso.zodiac.app.cueSheetTemplateSymbol.dto.CueTmplSymbolCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueTmplSymbolCreateMapper extends GenericMapper<CueTmplSymbolCreateDTO,
        CueTmplSymbol, CueTmplSymbolCreateDTO> {
}
