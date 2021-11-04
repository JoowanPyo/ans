package com.gemiso.zodiac.app.cueSheetItem.dto;

import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetItemSymbolResponseDTO {

    private Long cueItemSymbolId;
    //private CueSheetItemSimpleDTO cueSheetItem;
    //private SymbolDTO symbol;
    //private int ord;
}
