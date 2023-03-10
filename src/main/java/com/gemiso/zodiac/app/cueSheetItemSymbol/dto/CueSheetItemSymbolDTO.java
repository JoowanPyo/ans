package com.gemiso.zodiac.app.cueSheetItemSymbol.dto;

import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSimpleDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetItemSymbolDTO {


    private Long id;
    private CueSheetItemSimpleDTO cueSheetItem;
    private SymbolDTO symbol;
    private Integer ord;
}
