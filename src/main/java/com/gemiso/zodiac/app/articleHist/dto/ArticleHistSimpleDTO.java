package com.gemiso.zodiac.app.articleHist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Schema(description = "기사이력 맵핑 articleDTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleHistSimpleDTO {

    @Schema(description = "기사 이력 아이디")
    private Long artclHistId;
    @Schema(description = "채널 구분 코드")
    private String chDivCd;
    @Schema(description = "기사 제목")
    private String artclTitl;
    @Schema(description = "영문 기사 제목")
    private String artclTitlEn;
    @Schema(description = "영문 기사 제목")
    private String artclCtt;
    @Schema(description = "앵커 멘트 내용")
    private String ancMentCtt;
    @Schema(description = "기사 순번")
    private int artclOrd;
    @Schema(description = "원본 기사 아이디")
    private Long orgArtclId;
    @Schema(description = "입력 일시")
    private Date inputDtm;
    @Schema(description = "버전")
    private int ver;
    /*@Schema(description = "기사 아이디")
    private ArticleDTO article;*/

   /* private Long artclHistId;
    private String chDivCd;
    private String artclTitl;
    private String artclTitlEn;
    private String artclCtt;
    private String ancMentCtt;
    private int artclOrd;
    private Long orgArtclId;
    private Date inputDtm;
    private int ver;
    //private ArticleDTO article;*/
}
