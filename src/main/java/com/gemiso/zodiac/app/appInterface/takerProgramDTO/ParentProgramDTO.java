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
    private Long cueId; //큐시트 아이디
    @XmlElement(name = "rd_st_nm")
    private String rdStNm; //방송상태
    @XmlElement(name="ch_div_cd")
    private String chDivCd;//채널 구분 코드
    @XmlElement(name = "ch_div_nm")
    private String chDivNm; //채널 구분 코드 명
    @XmlElement(name="brdc_dt")
    private String brdcDt;//방송일자
    @XmlElement(name = "brdc_seq")
    private Integer brdcSeq;
    @XmlElement(name="brdc_start_clk")
    private String brdcStartTime;//방송시작 시간
    @XmlElement(name="brdc_end_clk")
    private String brdcEndTime;//방송종료 시간
    @XmlElement(name = "brdc_run_time")
    private String brdcRunTime;//방송 길이
    @XmlElement(name = "brdc_pgm_id")
    private String brdcPgmId; // 프로그램 아이디
    @XmlElement(name="brdc_pgm_nm")
    private String brdcPgmNm; //프로그램 명
    @XmlElement(name = "urg_pgmsch_pgm_nm")
    private String urgPgmschPgmNm; // new 몬지모름.
    @XmlElement(name = "brdc_div_cd")
    private String brdcDivCD; //방송 구분 코드
    @XmlElement(name = "cm_div_cd")
    private String cmDivCd; //몬지모름
    @XmlElement(name="rmk")
    private String remark; //비고
    @XmlElement(name="inputr_id")
    private String inputr;//입력자
    @XmlElement(name = "inputr_nm")
    private String inputrNm;//입렵자명
    @XmlElement(name="input_dtm")
    private Date inputDtm;//입력일시
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
    private Long stdioId;
    @XmlElement(name="subrm_id")
    private Long subrmId;
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

}
