package com.gemiso.zodiac.app.articleCap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "기사자막 등록 articleDTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCapCreateDTO {

    /*@Schema(description = "기사 자막 아이디")
    private Long artclCapId;*/
    @Schema(description = "자막 구분 코드")
    private String capDivCd;
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

}
