package com.gemiso.zodiac.app.cueSheetItemSymbol.dto;

import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSimpleDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetItemSymbolDTO {


    private Long id;
    private CueSheetItemSimpleDTO cueSheetItem;
    private SymbolDTO symbol;
    private int ord;
}
