package com.gemiso.zodiac.app.appInterface.prompterProgram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "record")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrompterProgramDTO {

    @XmlElement(name="brdc_pgm_id")
    private String brdcPgmId;

    @XmlElement(name = "ch_div_cd")
    private String chDivCd;

    @XmlElement(name = "ch_div_cd_nm")
    private String chDivCdNm;

    @XmlElement(name = "brdc_pgm_nm")
    private String brdcPgmNm;

    @XmlElement(name = "brdc_pgm_div_cd")
    private String brdcPgmDivCd;
    
    @XmlElement(name = "brdc_pgm_div_cd_nm")
    private String brdcPgmDivCdNm;

    @XmlElement(name = "gne_div_cd")
    private String gneDivCd;

    @XmlElement(name = "gne_div_cd_nm")
    private String gneDivCdNm;

    @XmlElement(name = "prd_div_cd")
    private String prdDivCd;

    @XmlElement(name = "prd_div_cd_nm")
    private String prdDivCdNm;

    @XmlElement(name = "prd_dept_cd")
    private String prdDeptCd;

    @XmlElement(name = "prd_dept_cd_nm")
    private String prdDeptCdNm;

    @XmlElement(name = "brdc_start_dt")
    private String brdcStartDt;

    @XmlElement(name = "del_yn")
    private String delYn;

    @XmlElement(name = "inputr_id")
    private String inputrId;

    @XmlElement(name = "inputr_id_nm")
    private String inputrIdNm;

    @XmlElement(name = "input_dtm")
    private String inputDtm;

    @XmlElement(name = "updtr_id")
    private String updtrId;

    @XmlElement(name = "updtr_id_nm")
    private String updtrIdNm;

    @XmlElement(name = "updt_dtm")
    private String updtDtm;
}
