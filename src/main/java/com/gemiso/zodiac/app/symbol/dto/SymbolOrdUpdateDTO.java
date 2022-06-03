package com.gemiso.zodiac.app.symbol.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SymbolOrdUpdateDTO {

    private String typCd;
    private Integer symbolOrd;
}
