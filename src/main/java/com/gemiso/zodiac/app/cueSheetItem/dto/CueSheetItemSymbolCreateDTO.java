package com.gemiso.zodiac.app.cueSheetItem.dto;

import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetItemSymbolCreateDTO {

    //private Long id;
    private CueSheetItemSimpleDTO cueSheetItem;
    private SymbolSimpleDTO symbol;
    private int ord;
}
