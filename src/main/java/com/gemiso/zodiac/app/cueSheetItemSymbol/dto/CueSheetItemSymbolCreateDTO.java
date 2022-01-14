package com.gemiso.zodiac.app.cueSheetItemSymbol.dto;

import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSimpleDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetItemSymbolCreateDTO {

    //private Long id;
    @NotNull
    private CueSheetItemSimpleDTO cueSheetItem;
    @NotNull
    private SymbolSimpleDTO symbol;
    @NotNull
    private int ord;
}
