package com.gemiso.zodiac.app.appInterface.takerProgramDTO;

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
public class ParentProgramDTO {

    @XmlElement(name="rd_id")
    private Long cueId;
    @XmlElement(name="ch_div_cd")
    private String chDivCd;
    @XmlElement(name = "ch_div_nm")
    private String chDivNm; //new이름같이출력
    @XmlElement(name="brdc_dt")
    private String brdcDt;
    @XmlElement(name = "brdc_seq")
    private Integer brdcSeq;
    @XmlElement(name="brdc_start_clk")
    private String brdcStartTime;
    @XmlElement(name="brdc_end_clk")
    private String brdcEndTime;
    @XmlElement(name = "brdc_pgm_id")
    private Long brdcPgmId; // new 아이디랑 네임 둘다출력
    @XmlElement(name="brdc_pgm_nm")
    private String brdcPgmNm; //아이디랑 네임 둘다출력
    @XmlElement(name = "urg_pgmsch_pgm_nm")
    private String urgPgmschPgmNm; // new 몬지모름.
    @XmlElement(name = "brdc_div_cd")
    private String brdcDivCD; //new
    @XmlElement(name = "cm_div_cd")
    private String cmDivCd; //new
    @XmlElement(name="rmk")
    private String remark;
    @XmlElement(name="inputr_id")
    private String inputr;
    @XmlElement(name = "inputr_nm")
    private String inputrNm;
    @XmlElement(name="input_dtm")
    private Date inputDtm;
    @XmlElement(name="pd_1_id")
    private String pd1;
    @XmlElement(name="pd_2_id")
    private String pd2;
    @XmlElement(name="anc_1_id")
    private String anc1;
    @XmlElement(name="anc_2_id")
    private String anc2;
    @XmlElement(name="td_id")
    private String td1;
    @XmlElement(name="stdio_id")
    private String stdioId;
    @XmlElement(name="subrm_id")
    private String subrmId;
    @XmlElement(name = "cg_id")
    private Long cgId; // new 몬지모름.
    @XmlElement(name = "cg_loc")
    private String cgloc; // new 몬지모름.
    @XmlElement(name = "vf_id")
    private Long vfId; // new 몬지모름.
    @XmlElement(name = "vs_id")
    private Long vsId; // new 몬지모름.
    @XmlElement(name = "pd_1_nm")
    private String pd1Nm; //new
    @XmlElement(name = "pd_2_nm")
    private String pd2Nm; //new
    @XmlElement(name = "anc_1_nm")
    private String anc1Nm; //new
    @XmlElement(name = "anc_2_nm")
    private String anc2Nm; //new
    @XmlElement(name = "td_nm")
    private String tdNm; // new
    @XmlElement(name = "stdio_nm")
    private String stdioNm; // new
    @XmlElement(name = "subrm_nm")
    private String subrmNm; // new 몬지모름.
    @XmlElement(name = "rd_edt_yn")
    private String rdEdtYn; // new 몬지모름.
    @XmlElement(name = "endpgm_yn")
    private String endpgmYn; // new 몬지모름.


    /*//테이블에 있는대 안들어가는 애들
    @XmlElement(name="cue_div_cd")
    private String cueDivCd;
    @XmlElement(name="BRDC_SCH_TIME")
    private String brdcSchTime;
    @XmlElement(name="CUE_ST_CD")
    private String cueStCd;
    @XmlElement(name="LCK_DTM")
    private Date lckDtm;
    @XmlElement(name="LCK_YN")
    private String lckYn;
    @XmlElement(name="DEL_DTM")
    private Date delDtm;
    @XmlElement(name="DEL_YN")
    private String delYn;
    @XmlElement(name="DELR_ID")
    private String delr;
    @XmlElement(name="LCKR_ID")
    private String lckr;
    @XmlElement(name="TD_2_ID")
    private String td2;
    */
    
    //@XmlElement(name="program")
    //private TakerCueProgramDTO program;

    /*@JsonProperty("CUE_ID")
    private Long cueId;
    @JsonProperty("CUE_DIV_CD")
    private String cueDivCd;
    @JsonProperty("CH_DIV_CD")
    private String chDivCd;
    @JsonProperty("BRDC_DT")
    private String brdcDt;
    @JsonProperty("BRDC_START_TIME")
    private String brdcStartTime;
    @JsonProperty("BRDC_END_TIME")
    private String brdcEndTime;
    @JsonProperty("BRDC_SCH_TIME")
    private String brdcSchTime;
    @JsonProperty("BRDC_PGM_NM")
    private String brdcPgmNm;
    @JsonProperty("CUE_ST_CD")
    private String cueStCd;
    @JsonProperty("STDIO_ID")
    private String stdioId;
    @JsonProperty("SUBRM_ID")
    private String subrmId;
    @JsonProperty("LCK_DTM")
    private Date lckDtm;
    @JsonProperty("LCK_YN")
    private String lckYn;
    @JsonProperty("DEL_DTM")
    private Date delDtm;
    @JsonProperty("INPUT_DTM")
    private Date inputDtm;
    @JsonProperty("DEL_YN")
    private String delYn;
    @JsonProperty("INPUTR_ID")
    private String inputr;
    @JsonProperty("DELR_ID")
    private String delr;
    @JsonProperty("PD_1_ID")
    private String pd1;
    @JsonProperty("PD_2_ID")
    private String pd2;
    @JsonProperty("ANC_1_ID")
    private String anc1;
    @JsonProperty("ANC_2_ID")
    private String anc2;
    @JsonProperty("LCKR_ID")
    private String lckr;
    @JsonProperty("TD_1_ID")
    private String td1;
    @JsonProperty("TD_2_ID")
    private String td2;
    @JsonProperty("REMARK")
    private String remark;
    @JsonProperty("PROGRAM")
    private ProgramDTO program;*/
}
