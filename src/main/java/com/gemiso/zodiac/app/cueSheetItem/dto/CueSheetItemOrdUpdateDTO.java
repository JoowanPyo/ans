package com.gemiso.zodiac.app.cueSheetItem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CueSheetItemOrdUpdateDTO {

    private Long cueItemId;
    private int cueItemOrd;
    private String cueItemOrdCd;
}
