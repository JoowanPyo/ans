package com.gemiso.zodiac.app.article.dto;

import com.gemiso.zodiac.app.cueSheet.dto.CueSheetSimpleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "기사 맵핑 articleDTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSimpleDTO {

    @Schema(description = "기사 아이디")
    private Long artclId;
    @Schema(description = "기사 시퀀스 아이디")
    private String artclSeqId;
   /* private String chDivCd;
    private String artclKindCd;
    private String artclFrmCd;
    private String artclDivCd;
    private String artclFldCd;
    private String apprvDivCd;
    private String prdDivCd;
    private String artclTypCd;
    private String artclTypDtlCd;
    private String artclCateCd;*/
    @Schema(description = "기사 제목")
    private String artclTitl;
    @Schema(description = "영문 기사 제목")
    private String artclTitlEn;
    @Schema(description = "원본 기사 아이디")
    private Long orgArtclId;
    @Schema(description = "기사 순번")
    private Integer artclOrd;
    @Schema(description = "큐시트")
    private CueSheetSimpleDTO cueSheet;
    /*private String artclCtt;
    private String ancMentCtt;
    private String rptrNm;
    private String userGrpId;
    private String artclReqdSecDivYn;
    private Integer artclReqdSec;
    private String lckYn;
    private Date lckDtm;
    private Date apprvDtm;

    private Integer brdcCnt;

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
    private UserSimpleDTO inputr;
    //private String updtrId;
    private UserSimpleDTO updtr;
    private String delrId;
    private String apprvrId;
    private String lckrId;
    private String rptrId;
    private Integer artclCttTime;
    private Integer ancMentCttTime;
    private Integer artclExtTime;
    private Integer videoTime;
    private String deptCd;
    private String deviceCd;
    private IssueDTO issue;
    private List<ArticleHistSimpleDTO> articleHistDTO = new ArrayList<>();
    private List<ArticleCapSimpleDTO> articleCapDTO = new ArrayList<>();
    private List<ArticleMediaSimpleDTO> articleMediaDTO = new ArrayList<>();
    private List<ArticleOrderSimpleDTO> articleOrderDTO = new ArrayList<>();*/
}
