package com.gemiso.zodiac.app.cueSheetItem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CueSheetItemCreateListDTO {

    //private Long cueId;
    private Long artclId;
    private int cueItemOrd;
    private String cueItemDivCd;
}
