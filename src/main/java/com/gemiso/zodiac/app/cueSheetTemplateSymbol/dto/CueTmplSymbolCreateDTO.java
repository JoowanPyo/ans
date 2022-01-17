package com.gemiso.zodiac.app.cueSheetTemplateSymbol.dto;

import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateSimpleDTO;
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
public class CueTmplSymbolCreateDTO {

    //private Long id;
    @NotNull
    private CueSheetTemplateSimpleDTO cueSheetTemplate;
    @NotNull
    private SymbolSimpleDTO symbol;
    @NotNull
    private int ord;
}
