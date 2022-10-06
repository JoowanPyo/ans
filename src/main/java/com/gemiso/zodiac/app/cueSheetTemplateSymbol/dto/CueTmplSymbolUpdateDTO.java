package com.gemiso.zodiac.app.cueSheetTemplateSymbol.dto;

import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmpltItemSimpleDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CueTmplSymbolUpdateDTO {

    //private Long id;
    private CueTmpltItemSimpleDTO cueTmpltItem;
    private SymbolSimpleDTO symbol;
    private Integer ord;
}
