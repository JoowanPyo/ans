package com.gemiso.zodiac.app.elasticsearch;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Schema(description = "엘라스틱 기사 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElasticSearchArticleDTO {

    private Long artclId;
    private String apprvDivCd;
    private String artclCateCd;
    private String artclDivCd;
    private Integer artclOrd;
    private String artclTitl;
    private String artclTitlEn;
    private String artclTypCd;
    private String artclTypDtlCd;
    private String brdcPgmId;
    private ElasticSearchCueSheet cueSheet;
    private String delYn;
    private Integer deptCd;
    private String embgYn;
    private String inputDtm;
    private String inputrId;
    private String lckYn;
    private Long orgArtclId;

}
