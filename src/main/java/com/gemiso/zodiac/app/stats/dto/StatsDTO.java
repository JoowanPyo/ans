package com.gemiso.zodiac.app.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsDTO {

    private Long id;
    private String brdcDt;
    private Integer brollCount;
    private Integer mngCount;
    private Integer telephoneCount;
    private Integer newsStudioCount;
    private Integer smartphoneCount;
    private Integer emptyCount;
    private Integer apkCount;
    private Integer pkCount;
}
