package com.gemiso.zodiac.app.elasticsearch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "엘라스틱 큐시트 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElasticSearchCueSheetDTO {

    private Long cueId;
    private String brdcPgmNm;
    private Long subrmId;
}
