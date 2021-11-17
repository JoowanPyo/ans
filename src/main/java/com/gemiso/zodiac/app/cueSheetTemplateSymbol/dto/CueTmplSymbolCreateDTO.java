package com.gemiso.zodiac.app.cueSheetTemplateSymbol.dto;

import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateSimpleDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueTmplSymbolCreateDTO {

    //private Long id;
    private CueSheetTemplateSimpleDTO cueSheetTemplate;
    private SymbolSimpleDTO symbol;
    private int ord;
}
