package com.gemiso.zodiac.app.symbol.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SymbolOrdUpdateDTO {

    private String typCd;
    private Integer symbolOrd;
}
