package com.gemiso.zodiac.app.articleCap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "기사자막 수정 articleDTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCapUpdateDTO {

    @Schema(description = "기사 자막 아이디")
    private Long artclCapId;
    @Schema(description = "자막 구분 코드")
    private String capDivCd;
    /*@Schema(description = "자막 구분 코드 명")
    private String capDivCdNm;*/
    @Schema(description = "라인 번호")
    private Integer lnNo;
    @Schema(description = "자막 내용")
    private String capCtt;
    @Schema(description = "자막 비고")
    private String capRmk;
    @Schema(description = "자막 순번")
    private Integer lnOrd;
    @Schema(description = "기사 아이디")
    private Long articleId;
    @Schema(description = "자막 템플릿 아이디")
    private Long capTmpltId;
    @Schema(description = "방송아이콘 아이디")
    private String symbolId;

    /*private Long artclCapId;
    private String capDivCd;
    //private String capDivCdNm;
    private int lnNo;
    private String capCtt;
    private String capRmk;
    private Long articleId;
    private Long capTmpltId;
    private String symbolId;*/
}
