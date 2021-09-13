package com.gemiso.zodiac.app.symbol.mapper;

import com.gemiso.zodiac.app.symbol.Symbol;
import com.gemiso.zodiac.app.symbol.dto.SymbolCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SymbolCreateMapper extends GenericMapper<SymbolCreateDTO, Symbol, SymbolCreateDTO> {
}
