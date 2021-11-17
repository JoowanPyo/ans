package com.gemiso.zodiac.app.anchorCap.dto;

import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "앵커자막 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnchorCapDTO {

    @Schema(description = "앵커 자막 아이디")
    private Long anchorCapId;
    @Schema(description = "자막 구분 코드")
    private String capDivCd;
    @Schema(description = "자막 구분 코드 명")
    private String capDivCdNm;
    @Schema(description = "라인 번호")
    private int lnNo;
    @Schema(description = "자막 내용")
    private String capCtt;
    @Schema(description = "자막 비고")
    private String capRmk;
    @Schema(description = "기사 아이디")
    private ArticleSimpleDTO article;
    @Schema(description = "자막 템플릿 아이디")
    private CapTemplateDTO capTemplate;
    @Schema(description = "방송아이콘 아이디")
    private SymbolDTO symbol;
}
