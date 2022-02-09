package com.gemiso.zodiac.app.appInterface.prompterCue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "record")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrompterCueSheetDTO {

    @XmlElement(name="RD_ID")
    private Long cueId;

    @XmlElement(name = "RD_SEQ")
    private String rdSeq; //???

    @XmlElement(name = "CH_DIV_CD")
    private String chDivCd;

    @XmlElement(name = "RD_ORD")
    private Integer rdOrd;

    @XmlElement(name = "PRD_DIV_CD")
    private String prdDivCd;

    @XmlElement(name = "OPEN_YN")
    private String openYn;

    @XmlElement(name = "ARTCL_ID")
    private Long artclId;

    @XmlElement(name = "ARTCL_FRM_CD")
    private String artclFrmCd;

    @XmlElement(name = "ARTCL_FRM_NM")
    private String artclFrmNm;

    @XmlElement(name = "ARTCL_FLD_CD")
    private String artclFldCd;

    @XmlElement(name = "ARTCL_FLD_NM")
    private String artclFldNm;

    @XmlElement(name = "CUE_TITLE")
    private String artclTitl;

    @XmlElement(name = "ECUE_TITLE")
    private String artclTitlEn;

    @XmlElement(name = "ARTCL_CTT")
    private String artclCtt;

    @XmlElement(name = "ANC_CTT")
    private String ancCtt;

    @XmlElement(name = "RPTR_ID")
    private String rptrId;

    @XmlElement(name = "REPORT_NM")
    private String rptrNm;

    @XmlElement(name = "DEPT_CD")
    private String deptCd;

    @XmlElement(name = "ARTCL_REQD_SEC")
    private Integer artclReqdSec;

    @XmlElement(name = "ANC_REQD_SEC")
    private Integer ancReqdSec;

    @XmlElement(name = "NEWS_ACUM_TIME")
    private Integer newsAcumTime;


}
