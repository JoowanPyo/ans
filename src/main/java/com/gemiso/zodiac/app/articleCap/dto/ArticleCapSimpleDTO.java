package com.gemiso.zodiac.app.articleCap.dto;

import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "기사 자막 맵핑 DTo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCapSimpleDTO {

    @Schema(description = "기사 자막 아이디")
    private Long artclCapId;
    @Schema(description = "자막 구분 코드")
    private String capDivCd;
    @Schema(description = "자막 구분 코드 명")
    private String capDivCdNm;
    @Schema(description = "라인 번호")
    private Integer lnNo;
    @Schema(description = "자막 내용")
    private String capCtt;
    @Schema(description = "자막 비고")
    private String capRmk;
    @Schema(description = "자막 순번")
    private Integer lnOrd;
    /*@Schema(description = "기사 아이디")
    private ArticleSimpleDTO article;*/
    @Schema(description = "자막 템플릿 아이디")
    private CapTemplateDTO capTemplate;
    @Schema(description = "방송아이콘 아이디")
    private SymbolDTO symbol;

   /* private Long artclCapId;
    private String capDivCd;
    private String capDivCdNm;
    private int lnNo;
    private String capCtt;
    private String capRmk;
    //private ArticleDTO article;
    private CapTemplateDTO capTemplate;
    private SymbolDTO symbol;*/
}
