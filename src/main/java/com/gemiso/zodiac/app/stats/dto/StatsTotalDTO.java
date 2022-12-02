package com.gemiso.zodiac.app.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsTotalDTO {

    private Integer mmOrd;
    private String artclDiv;
    private Integer brollCount;
    private Integer mngCount;
    private Integer telephoneCount;
    private Integer newsStudioCount;
    private Integer smartphoneCount;
    private Integer emptyCount;
    private Integer apkCount;
    private Integer pkCount;
}
