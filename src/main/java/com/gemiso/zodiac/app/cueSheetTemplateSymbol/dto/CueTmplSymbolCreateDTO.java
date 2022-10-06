package com.gemiso.zodiac.app.cueSheetTemplateSymbol.dto;

import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmpltItemSimpleDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CueTmplSymbolCreateDTO {

    //private Long id;
    private CueTmpltItemSimpleDTO cueTmpltItem;
    @NotNull
    private SymbolSimpleDTO symbol;
    @NotNull
    private Integer ord;
}
