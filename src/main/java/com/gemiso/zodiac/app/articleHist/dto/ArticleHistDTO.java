package com.gemiso.zodiac.app.articleHist.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;

@Schema(description = "기사이력 articleDTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleHistDTO {

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
    private Integer artclOrd;
    @Schema(description = "원본 기사 아이디")
    private Long orgArtclId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "입력 일시")
    private Date inputDtm;
    @Schema(description = "버전")
    private Integer ver;
    @Schema(description = "입력자")
    private String inputrId;
    @Schema(description = "입력자 명")
    private String inputrNm;
    @Schema(description = "기사 아이디")
    private ArticleDTO article;
}
