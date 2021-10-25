package com.gemiso.zodiac.app.article.dto;

import com.gemiso.zodiac.app.articleCap.dto.ArticleCapDTO;
import com.gemiso.zodiac.app.articleCap.dto.ArticleCapSimpleDTO;
import com.gemiso.zodiac.app.code.dto.CodeSimpleDTO;
import com.gemiso.zodiac.app.issue.dto.IssueDTO;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCreateDTO {

    //private Long artclId;
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
    private Long brdcPgmId;
    private Date brdcSchdDtm;
    private Date inputDtm;
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
    private String deptCd;
    private String deviceCd;
    private IssueDTO issue;
    private Long parentArtlcId;

    //private List<ArticleMediaDTO> articleMedia;
    private List<ArticleCapSimpleDTO> articleCap;
    //private List<ArticleOrderDTO> articleOrder;
    //private List<ArticleHistDTO> articleHist;
}
