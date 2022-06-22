package com.gemiso.zodiac.app.article.dto;

import com.gemiso.zodiac.app.anchorCap.dto.AnchorCapCreateDTO;
import com.gemiso.zodiac.app.anchorCap.dto.AnchorCapSimpleDTO;
import com.gemiso.zodiac.app.articleCap.dto.ArticleCapCreateDTO;
import com.gemiso.zodiac.app.articleCap.dto.ArticleCapSimpleDTO;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetSimpleDTO;
import com.gemiso.zodiac.app.issue.dto.IssueDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleUpdateDTO {

    private Long artclId;
    private String chDivCd;
    //private String chDivCdNm;
    private String artclKindCd;
    //private String artclKindCdNm;
    private String artclFrmCd;
    //private String artclFrmCdNm;
    private String artclDivCd;
    //private String artclDivCdNm;
    private String artclFldCd;
    //private String artclFldCdNm;
    private String apprvDivCd;
    //private String apprvDivCdNm;
    private String prdDivCd;
    //private String prdDivCdNm;
    private String artclTypCd;
    //private String artclTypCdNm;
    private String artclTypDtlCd;
    //private String artclTypDtlCdNm;
    private String artclCateCd;
    //private String artclCateCdNm;
    private String artclTitl;
    private String artclTitlEn;
    private String artclCtt;
    private String ancMentCtt;
    //private String rptrNm;
    private Long userGrpId;
    private String artclReqdSecDivYn;
    private Integer artclReqdSec;
    //private String lckYn;
    //private Date lckDtm;
    //private Date apprvDtm;
    private Integer artclOrd;
    private Integer brdcCnt;
    private Long orgArtclId;
    private String urgYn;
    private String frnotiYn;
    private String embgYn;
    private Date embgDtm;
    //private String inputrNm;
    //private Date delDtm;
    //private String delYn;
    private String notiYn;
    private String regAppTyp;
    private String brdcPgmId;
    private Date brdcSchdDtm;
    //private Date inputDtm;
    private Date updtDtm;
    //private String inputrId;
    private String updtrId;
    //private String updtrNm;
    //private String delrId;
    //private String delrNm;
    private String apprvrId;
    //private String apprvrNm;
    private String lckrId;
    //private String lckrNm;
    private String rptrId;
    private Integer artclCttTime;
    private Integer ancMentCttTime;
    private Integer artclExtTime;
    private Integer videoTime;
    private Long deptCd;
    private String deviceCd;
    private String memo;
    private IssueDTO issue;
    private Long parentArtlcId;

    @Schema(description = "에디터 아이디")
    private String editorId;
    @Schema(description = "에디터 명")
    private String editorNm;

    @Schema(description = "기사 픽스자 아이디")
    private String artclFixUser;
    @Schema(description = "에디터 픽스자 아이디")
    private String editorFixUser;
    @Schema(description = "앵커 픽스자 아이디")
    private String anchorFixUser;
    @Schema(description = "데스커 픽스자 아이디")
    private String deskFixUser;

    @Schema(description = "기사픽스 일시")
    private Date artclFixDtm;
    @Schema(description = "에디터픽스 일시")
    private Date editorFixDtm;
    @Schema(description = "앵커픽스 일시")
    private Date anchorFixDtm;
    @Schema(description = "데스커픽스 일시")
    private Date deskFixDtm;

    //@Schema(description = "큐시트")
    private CueSheetSimpleDTO cueSheet;
    //private List<ArticleMediaDTO> articleMedia;
    private List<ArticleCapCreateDTO> articleCap = new ArrayList<>();
    private List<AnchorCapCreateDTO> anchorCap = new ArrayList<>();
    //private List<ArticleOrderDTO> articleOrder;
    //private List<ArticleHistDTO> articleHist;
}
