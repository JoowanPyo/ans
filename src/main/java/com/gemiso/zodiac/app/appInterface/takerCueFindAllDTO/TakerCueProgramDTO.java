package com.gemiso.zodiac.app.appInterface.takerCueFindAllDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "program")
@XmlAccessorType(XmlAccessType.FIELD)
public class TakerCueProgramDTO {

    @XmlElement(name="BRDC_PGM_ID")
    private Long brdcPgmId;
    @XmlElement(name="BRDC_PGM_NM")
    private String brdcPgmNm;
    @XmlElement(name="CH_DIV_CD")
    private String chDivCd;
    @XmlElement(name="BRDC_PGM_DIV_CD")
    private String brdcPgmDivCd;
    @XmlElement(name="GNE_DIV_CD")
    private String gneDivCd;
    @XmlElement(name="PRD_DIV_CD")
    private String prdDivCd;
    @XmlElement(name="BRDC_START_TIME")
    private String brdcStartTime;
    @XmlElement(name="SCH_TIME")
    private String schTime;
    @XmlElement(name="INPUT_DTM")
    private Date inputDtm;
    @XmlElement(name="UPDT_DTM")
    private Date updtDtm;
    @XmlElement(name="DEL_YN")
    private String delYn;
    @XmlElement(name="DEL_DTM")
    private Date delDtm;
    @XmlElement(name="INPUTR_ID")
    private String inputr;
    @XmlElement(name="UPDTR_ID")
    private String updtr;
    @XmlElement(name="DELR_ID")
    private String delr;
}
