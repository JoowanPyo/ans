package com.gemiso.zodiac.app.articleCap.dto;

import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateDTO;
import com.gemiso.zodiac.app.code.dto.CodeSimpleDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "기사자막 수정 DTO")
@Data
@Builder
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
    private int lnNo;
    @Schema(description = "자막 내용")
    private String capCtt;
    @Schema(description = "자막 비고")
    private String capRmk;
    @Schema(description = "자막 순번")
    private int lnOrd;
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
