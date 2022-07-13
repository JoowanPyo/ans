package com.gemiso.zodiac.app.cueSheetItemSymbol.dto;

import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSimpleDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetItemSymbolUpdateDTO {

    //private Long id;
    private CueSheetItemSimpleDTO cueSheetItem;
    private SymbolSimpleDTO symbol;
    private Integer ord;
}
