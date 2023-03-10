package com.gemiso.zodiac.app.article.dto;

import com.gemiso.zodiac.app.anchorCap.dto.AnchorCapCreateDTO;
import com.gemiso.zodiac.app.articleCap.dto.ArticleCapCreateDTO;
import com.gemiso.zodiac.app.issue.dto.IssueDTO;
import com.gemiso.zodiac.app.rundownItem.RundownItem;
import com.gemiso.zodiac.app.rundownItem.dto.RundownItemSimpleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCreateDTO {

    //private Long artclId;
    private String artclSeqId;
    @NotNull
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
    //private Date updtDtm;
    private String inputrId;
    //private String updtrId;
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
    //private String deptNm;
    //private String deviceCd;
    private String memo;
    private IssueDTO issue;
    private Long parentArtlcId;

    @Schema(description = "????????? ?????????")
    private String editorId;
    @Schema(description = "????????? ???")
    private String editorNm;

    @Schema(description = "?????? ????????? ?????????")
    private String artclFixUser;
    @Schema(description = "????????? ????????? ?????????")
    private String editorFixUser;
    @Schema(description = "?????? ????????? ?????????")
    private String anchorFixUser;
    @Schema(description = "????????? ????????? ?????????")
    private String deskFixUser;

    @Schema(description = "???????????? ??????")
    private Date artclFixDtm;
    @Schema(description = "??????????????? ??????")
    private Date editorFixDtm;
    @Schema(description = "???????????? ??????")
    private Date anchorFixDtm;
    @Schema(description = "??????????????? ??????")
    private Date deskFixDtm;

    private RundownItemSimpleDTO rundownItem;
    //private List<ArticleMediaDTO> articleMedia;
    private List<ArticleCapCreateDTO> articleCap = new ArrayList<>();
    private List<AnchorCapCreateDTO> anchorCap = new ArrayList<>();
    //private List<ArticleOrderDTO> articleOrder;
    //private List<ArticleHistDTO> articleHist;
}
