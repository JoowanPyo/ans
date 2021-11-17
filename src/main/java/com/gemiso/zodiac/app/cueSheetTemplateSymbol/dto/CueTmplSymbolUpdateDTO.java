package com.gemiso.zodiac.app.cueSheetTemplateSymbol.dto;

import com.gemiso.zodiac.app.symbol.dto.SymbolSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueTmplSymbolUpdateDTO {

    private Long id;
    //private CueSheetTemplateDTO cueSheetTemplate;
    private SymbolSimpleDTO symbol;
    private int ord;
}
