package com.gemiso.zodiac.app.articleMedia.dto;

import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Schema(description = "기사미디어 등록 articleDTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleMediaCreateDTO {

    //private Long artclMediaId;
    @Schema(description = "미디어 유형 코드")
    private String mediaTypCd;
    @Schema(description = "미디어 순번")
    private int mediaOrd;
    @Schema(description = "콘텐츠 아이디")
    private int contId;
    @Schema(description = "전송 파일 명")
    private String trnsfFileNm;
    @Schema(description = "미디어 길이")
    private Integer mediaDurtn;
    @Schema(description = "미디어 매칭 일시")
    private Date mediaMtchDtm;
    @Schema(description = "전송 상태 코드")
    private String trnsfStCd;
    @Schema(description = "배정 상태 코드")
    private String assnStCd;
    @Schema(description = "영상 편집자 명")
    private String videoEdtrNm;
    @Schema(description = "등록자 아이디")
    private String inputrId;
    @Schema(description = "영상 편집자 아이디")
    private String videoEdtrId;
    @Schema(description = "미디어 제목")
    private String artclMediaTitl;
    @Schema(description = "비디오 아이디")
    private String videoId;
    @Schema(description = "전송률")
    private Integer trnasfVal;
    @NotNull
    @Schema(description = "기사 아이디")
    private ArticleSimpleDTO article;
}
