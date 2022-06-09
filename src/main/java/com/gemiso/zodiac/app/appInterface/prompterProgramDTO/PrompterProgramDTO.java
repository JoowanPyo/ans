package com.gemiso.zodiac.app.appInterface.prompterProgramDTO;

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
public class PrompterProgramDTO {

    @XmlElement(name="rd_id")
    private Long rdId; // 방송 상태값

    @XmlElement(name="brdc_pgm_nm")
    private String proNm; //프로그램 명

    @XmlElement(name="brdc_dt")
    private String onAirDate; // 방송일자

    @XmlElement(name = "ch_div_cd")
    private String chDivCd; //채널구분코드

    @XmlElement(name="brdc_start_clk")
    private String startTime; // 시작시간

    @XmlElement(name="brdc_end_clk")
    private String endTime; // 종료시간

    @XmlElement(name="articlecount")
    private Integer aricleCount; // 기사 건수

    @XmlElement(name="rd_st_nm")
    private String brdcStCd; // 방송 상태값

    @XmlElement(name = "rd_edit_yn")
    private String rdEditYn; //수정 여부

    @XmlElement(name="PRO_ID")
    private String brdcPgmId; //프로그램 아이디

    @XmlElement(name="cue_id")
    private Long cueId; // 방송 상태값

    @XmlElement(name="article_count")
    private Integer articleCount; // 방송 상태값




    /*@XmlElement(name="CS_SPEND_TIME")
    private String csSpendTime;

    @XmlElement(name="NODSTATUS_NM")
    private String nodstatusNm;*/

    /*@XmlElement(name="brdc_pgm_id")
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
    private String updtDtm;*/
}
