package com.gemiso.zodiac.app.anchorCap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Schema(name = "앵커자막 등록 articleDTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnchorCapCreateDTO {

    //@Schema(description = "앵커 자막 아이디")
    //private Long anchorCapId;
    @Schema(description = "자막 구분 코드")
    private String capDivCd;
    @NotNull
    @Schema(description = "라인 번호")
    private int lnNo;
    @Schema(description = "자막 내용")
    private String capCtt;
    @Schema(description = "자막 비고")
    private String capRmk;
    @Schema(description = "자막 순번")
    private int lnOrd;
    @NotNull
    @Schema(description = "기사 아이디")
    private Long articleId;
    @Schema(description = "자막 템플릿 아이디")
    private Long capTemplateId;
    @Schema(description = "방송아이콘 아이디")
    private String symbolId;
}
