package com.gemiso.zodiac.app.articleMedia.dto;

import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Schema(description = "기사미디어 등록 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleMediaCreateDTO {

    //private Long artclMediaId;
    @Schema(description = "미디어 유형 코드")
    private String mediaTypCd;
    //private String mediaTypCdNm;
    @Schema(description = "미디어 순번")
    private int mediaOrd;
    @Schema(description = "콘텐츠 아이디")
    private int contId;
    @Schema(description = "전송 파일 명")
    private String trnsfFileNm;
    @Schema(description = "미디어 길이")
    private String mediaDurtn;
    @Schema(description = "미디어 매칭 일시")
    private Date mediaMtchDtm;
    @Schema(description = "전송 상태 코드")
    private String trnsfStCd;
    //private String trnsfStCdNm;
    @Schema(description = "배정 상태 코드")
    private String assnStCd;
    //private String assnStCdNm;
    @Schema(description = "영상 편집자 명")
    private String videoEdtrNm;
    //private String delYn;
    //private Date delDtm;
    @Schema(description = "등록 일시")
    private Date inputDtm;
    //private Date updtDtm;
    @Schema(description = "등록자 아이디")
    private String inputrId;
    //private String inputrNm;
    //private String updtrId;
    //private String updtrNm;
    //private String delrId;
    //private String delrNm;
    @Schema(description = "영상 편집자 아이디")
    private String videoEdtrId;
    @Schema(description = "기사 아이디")
    private ArticleSimpleDTO article;
}
