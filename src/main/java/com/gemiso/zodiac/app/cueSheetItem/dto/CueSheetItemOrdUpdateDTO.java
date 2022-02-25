package com.gemiso.zodiac.app.cueSheetItem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CueSheetItemOrdUpdateDTO {

    private Long cueItemId;
    private int cueItemOrd;
    private String cueItemOrdCd;
}
