package com.gemiso.zodiac.app.appInterface.prompterCue;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemDTO;
import com.gemiso.zodiac.app.program.dto.ProgramDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "record")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrompterCueSheetDTO {

    @XmlElement(name="rd_id")
    private String cueId;

    @XmlElement(name = "rd_seq")
    private String rdSeq; //???

    @XmlElement(name = "ch_div_cd")
    private String chDivCd;

    @XmlElement(name = "rd_ord")
    private Integer rd_ord;

    @XmlElement(name = "prd_div_cd")
    private String prdDivCd;

    @XmlElement(name = "open_yn")
    private String openYn;

    @XmlElement(name = "artcl_id")
    private Long artclId;

    @XmlElement(name = "artcl_frm_cd")
    private String artclFrmCd;

    @XmlElement(name = "artcl_frm_nm")
    private String artclFrmNm;

    @XmlElement(name = "artcl_fld_cd")
    private String artclFldCd;

    @XmlElement(name = "artcl_fld_nm")
    private String artclFldNm;

    @XmlElement(name = "artcl_titl")
    private String artclTitl;

    @XmlElement(name = "artcl_titl_en")
    private String artclTitlEn;

    @XmlElement(name = "artcl_ctt")
    private String artclCtt;

    @XmlElement(name = "anc_ctt")
    private String ancCtt;

    @XmlElement(name = "rptr_id")
    private String rptrId;

    @XmlElement(name = "rptr_nm")
    private String rptrNm;

    @XmlElement(name = "dept_cd")
    private String deptCd;

    @XmlElement(name = "artcl_reqd_sec")
    private Integer artclReqdSec;

    @XmlElement(name = "anc_reqd_sec")
    private Integer ancReqdSec;



}
