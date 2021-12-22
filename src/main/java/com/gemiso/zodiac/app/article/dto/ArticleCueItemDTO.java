package com.gemiso.zodiac.app.article.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.anchorCap.dto.AnchorCapSimpleDTO;
import com.gemiso.zodiac.app.articleCap.dto.ArticleCapSimpleDTO;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaSimpleDTO;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderSimpleDTO;
import com.gemiso.zodiac.app.issue.dto.IssueDTO;
import com.gemiso.zodiac.app.tag.dto.ArticleTagDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Schema(description = "기사 큐시트아이템 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCueItemDTO {

    @Schema(description = "기사 아이디")
    private Long artclId;
    @Schema(description = "채널 구분 코드")
    private String chDivCd;
    @Schema(description = "채널 구분 코드 명")
    private String chDivCdNm;
    @Schema(description = "기사 종류 코드")
    private String artclKindCd;
    @Schema(description = "기사 종류 코드 명")
    private String artclKindCdNm;
    @Schema(description = "기사 형식 코드")
    private String artclFrmCd;
    @Schema(description = "기사 형식 코드 명")
    private String artclFrmCdNm;
    @Schema(description = "기사 구분 코드")
    private String artclDivCd;
    @Schema(description = "기사 구분 코드 명")
    private String artclDivCdNm;
    @Schema(description = "기사 분야 코드")
    private String artclFldCd;
    @Schema(description = "기사 분야 코드 명")
    private String artclFldCdNm;
    @Schema(description = "승인 구분 코드")
    private String apprvDivCd;
    @Schema(description = "승인 구분 코드 명")
    private String apprvDivCdNm;
    @Schema(description = "제작 구분 코드")
    private String prdDivCd;
    @Schema(description = "제작 구분 코드 명")
    private String prdDivCdNm;
    @Schema(description = "기사 유형 코드")
    private String artclTypCd;
    @Schema(description = "기사 유형 코드 명")
    private String artclTypCdNm;
    @Schema(description = "기상 유형 상세 코드")
    private String artclTypDtlCd;
    @Schema(description = "기상 유형 상세 코드 명")
    private String artclTypDtlCdNm;
    @Schema(description = "기사 카테고리 코드")
    private String artclCateCd;
    @Schema(description = "기사 카테고리 코드 명")
    private String artclCateCdNm;
    @Schema(description = "기사 제목")
    private String artclTitl;
    @Schema(description = "영문 기사 제목")
    private String artclTitlEn;
    @Schema(description = "기사 내용")
    private String artclCtt;
    @Schema(description = "앵커 멘트 내용")
    private String ancMentCtt;
    @Schema(description = "기자 명")
    private String rptrNm;
    @Schema(description = "사용자 그룹 아이디")
    private Long userGrpId;
    @Schema(description = "기사 소요시간 구분 여부")
    private String artclReqdSecDivYn;
    @Schema(description = "기사 소요 시간(초)")
    private Integer artclReqdSec;
    @Schema(description = "잠금 여부")
    private String lckYn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "잠금 일시")
    private Date lckDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "승인 일시")
    private Date apprvDtm;
    @Schema(description = "기사 순번")
    private Integer artclOrd;
    @Schema(description = "방송횟수")
    private Integer brdcCnt;
    @Schema(description = "원본 기사 아이디")
    private Long orgArtclId;
    @Schema(description = "긴급 여부")
    private String urgYn;
    @Schema(description = "예고 여부")
    private String frnotiYn;
    @Schema(description = "엠바고 여부")
    private String embgYn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "엠바고 일시")
    private Date embgDtm;
    @Schema(description = "입력자 명")
    private String inputrNm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "삭제 일시")
    private Date delDtm;
    @Schema(description = "삭제 여부")
    private String delYn;
    @Schema(description = "공지 여부")
    private String notiYn;
    @Schema(description = "등록 어플리케이션 유형")
    private String regAppTyp;
    @Schema(description = "방송 프로그램 아이디")
    private Long brdcPgmId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "방송예정 일시")
    private Date brdcSchdDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "입력 일시")
    private Date inputDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "수정 일시")
    private Date updtDtm;
    @Schema(description = "입력자 아이디")
    private String inputrId;
    @Schema(description = "수정자 아이디")
    private String updtrId;
    @Schema(description = "수정자 명")
    private String updtrNm;
    @Schema(description = "삭제자 아이디")
    private String delrId;
    @Schema(description = "삭제자 명")
    private String delrNm;
    @Schema(description = "승인자 아이디")
    private String apprvrId;
    @Schema(description = "승인자 명")
    private String apprvrNm;
    @Schema(description = "잠금자 아이디")
    private String lckrId;
    @Schema(description = "잠금자 명")
    private String lckrNm;
    @Schema(description = "기자 아이디")
    private String rptrId;
    @Schema(description = "기사 시간(초)")
    private Integer artclCttTime;
    @Schema(description = "앵커 기사 시간")
    private Integer ancMentCttTime;
    @Schema(description = "추가 시간")
    private Integer artclExtTime;
    @Schema(description = "영상 시간")
    private Integer videoTime;
    @Schema(description = "부서 코드")
    private String deptCd;
    @Schema(description = "부서 명")
    private String deptNm;
    @Schema(description = "")
    private String deviceCd;
    @Schema(description = "")
    private String memo;
    @Schema(description = "이슈 아이디")
    private IssueDTO issue;
    @Schema(description = "복사된 기사 아이디")
    private Long parentArtlcId;
    //private List<ArticleHistSimpleDTO> articleHistDTO = new ArrayList<>();
    @Schema(description = "기사자막 리스트")
    private List<ArticleCapSimpleDTO> articleCapDTO = new ArrayList<>();
    @Schema(description = "앵커자막 리스트")
    private List<AnchorCapSimpleDTO> anchorCap = new ArrayList<>();
    @Schema(description = "기사영상 리스트")
    private List<ArticleMediaSimpleDTO> articleMediaDTO = new ArrayList<>();
    //@Schema(description = "기사의뢰 리스트")
    //private List<ArticleOrderSimpleDTO> articleOrderDTO = new ArrayList<>();
    //@Schema(description = "기사태그 리스트")
    //private List<ArticleTagDTO> articleTag = new ArrayList<>();

    /*private Long artclId;
    private String chDivCd;
    private String chDivCdNm;
    private String artclKindCd;
    private String artclKindCdNm;
    private String artclFrmCd;
    private String artclFrmCdNm;
    private String artclDivCd;
    private String artclDivCdNm;
    private String artclFldCd;
    private String artclFldCdNm;
    private String apprvDivCd;
    private String apprvDivCdNm;
    private String prdDivCd;
    private String prdDivCdNm;
    private String artclTypCd;
    private String artclTypCdNm;
    private String artclTypDtlCd;
    private String artclTypDtlCdNm;
    private String artclCateCd;
    private String artclCateCdNm;
    private String artclTitl;
    private String artclTitlEn;
    private String artclCtt;
    private String ancMentCtt;
    private String rptrNm;
    private Long userGrpId;
    private String artclReqdSecDivYn;
    private Integer artclReqdSec;
    private String lckYn;
    private Date lckDtm;
    private Date apprvDtm;
    private Integer artclOrd;
    private Integer brdcCnt;
    private Long orgArtclId;
    private String urgYn;
    private String frnotiYn;
    private String embgYn;
    private Date embgDtm;
    private String inputrNm;
    private Date delDtm;
    private String delYn;
    private String notiYn;
    private String regAppTyp;
    private Long brdcPgmId;
    private Date brdcSchdDtm;
    private Date inputDtm;
    private Date updtDtm;
    private String inputrId;
    private String updtrId;
    private String updtrNm;
    private String delrId;
    private String delrNm;
    private String apprvrId;
    private String apprvrNm;
    private String lckrId;
    private String lckrNm;
    private String rptrId;
    private Integer artclCttTime;
    private Integer ancMentCttTime;
    private Integer artclExtTime;
    private Integer videoTime;
    private String deptCd;
    private String deviceCd;
    private String memo;
    private IssueDTO issue;
    private Long parentArtlcId;
    //private List<ArticleHistSimpleDTO> articleHistDTO = new ArrayList<>();
    private List<ArticleCapSimpleDTO> articleCapDTO = new ArrayList<>();
    private List<AnchorCapSimpleDTO> anchorCap = new ArrayList<>();
    private List<ArticleMediaSimpleDTO> articleMediaDTO = new ArrayList<>();
    //private List<ArticleOrderSimpleDTO> articleOrderDTO = new ArrayList<>();*/
}
