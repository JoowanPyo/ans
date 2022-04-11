package com.gemiso.zodiac.app.articleMedia.dto;

import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Schema(description = "기사미디어 수정 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleMediaUpdateDTO {

    @Schema(description = "기사 미디어 아이디")
    private Long artclMediaId;
    @Schema(description = "미디어 유형 코드")
    private String mediaTypCd;
    /*@Schema(description = "미디어 유형 코드 명")
    private String mediaTypCdNm;*/
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
    /*@Schema(description = "전송 상태 코드 명")
    private String trnsfStCdNm;*/
    @Schema(description = "배정 상태 코드")
    private String assnStCd;
    /*@Schema(description = "배정 상태 코드 명")
    private String assnStCdNm;*/
    @Schema(description = "영상 편집자 명")
    private String videoEdtrNm;
    /*@Schema(description = "삭제 여부")
    private String delYn;
    @Schema(description = "삭제 일시")
    private Date delDtm;
    @Schema(description = "등록 일시")
    private Date inputDtm;
    @Schema(description = "수정 일시")
    private Date updtDtm;*/
    /*@Schema(description = "등록자 아이디")
    private String inputrId;
    @Schema(description = "등록자 명")
    private String inputrNm;
    @Schema(description = "수정자 아이디")*/
    private String updtrId;
    /*@Schema(description = "수정자 명")
    private String updtrNm;
    @Schema(description = "삭제자 아이디")
    private String delrId;
    @Schema(description = "삭제자 명")
    private String delrNm;*/
    @Schema(description = "영상 편집자 아이디")
    private String videoEdtrId;
    @Schema(description = "미디어 제목")
    private String artclMediaTitl;
    @Schema(description = "비디오 아이디")
    private String videoId;
    @Schema(description = "기사 아이디")
    private ArticleSimpleDTO article;

    /*private Long artclMediaId;
    private String mediaTypCd;
    //private String mediaTypCdNm;
    private int mediaOrd;
    private int contId;
    private String trnsfFileNm;
    private String mediaDurtn;
    private Date mediaMtchDtm;
    private String trnsfStCd;
    //private String trnsfStCdNm;
    private String assnStCd;
    //private String assnStCdNm;
    private String videoEdtrNm;
    //private String delYn;
    //private Date delDtm;
    //private Date inputDtm;
    private Date updtDtm;
    //private String inputrId;
    //private String inputrNm;
    private String updtrId;
    private String updtrNm;
    //private String delrId;
    //private String delrNm;
    private String videoEdtrId;
    private ArticleSimpleDTO article;*/
}
