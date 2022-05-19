package com.gemiso.zodiac.app.appInterface.takerCueFindAllDTO;

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
public class TakerCueSheetDTO {

    @XmlElement(name="rd_id")
    private Long rdId;
    @XmlElement(name="rd_seq")
    private int rdSeq;
    @XmlElement(name="ch_div_cd")
    private String chDivCd;
    @XmlElement(name="ch_div_nm")
    private String cueDivCdNm;
    @XmlElement(name="rd_ord")
    private int rdOrd;
    @XmlElement(name="rd_ord_mrk")
    private String rdOrdMrk;
    @XmlElement(name="rd_dtl_div_cd")
    private String rdDtlDivCd;
    @XmlElement(name="mc_st_cd")
    private String mcStCd;
    @XmlElement(name="cm_div_cd")
    private String cmDivCd;
    @XmlElement(name="cm_div_nm")
    private String cmDivNm;
    @XmlElement(name="rd_dtl_div_nm")
    private String rdDtlDivNm;
    @XmlElement(name="mc_st_nm")
    private String mcStNm;
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
    @XmlElement(name = "rptr_nm")
    private String rptrNm;
    @XmlElement(name = "dept_cd")
    private String deptCd;
    @XmlElement(name = "dept_nm")
    private String deptNm;
    @XmlElement(name = "artcl_reqd_sec")
    private Integer artclReqdSec; //기사소요시간
    @XmlElement(name = "anc_reqd_sec")
    private Integer ancReqdSec; //앵커소요시간
    @XmlElement(name = "artcl_smry_ctt")
    private int artclSmryCtt;
    @XmlElement(name = "artcl_div_cd")
    private String artclDivCd;
    @XmlElement(name = "artcl_div_nm")
    private String artclDivNm;
    @XmlElement(name = "issu_id")
    private Long issuId;
    @XmlElement(name = "lck_user_id")
    private String lckrId;
    @XmlElement(name = "lck_user_nm")
    private String lckrNm;
    @XmlElement(name = "lck_dtm")
    private Date lckDtm;
    @XmlElement(name = "apprv_div_cd")
    private String apprvDivCd;
    @XmlElement(name = "apprv_div_nm")
    private String apprvDivNm;
    @XmlElement(name = "apprv_dtm")
    private Date apprvDtm;
    @XmlElement(name = "apprvr_id")
    private String apprvrId;
    @XmlElement(name = "apprvr_nm")
    private String apprvrNm;
    @XmlElement(name = "artcl_ord")
    private Integer artclOrd;
    @XmlElement(name = "brdc_cnt")
    private Integer brdcCnt;
    @XmlElement(name = "org_artcl_id")
    private Long orgArtclId;
    @XmlElement(name = "rpt_pln_id")
    private String rptPlnId;
    @XmlElement(name = "brdc_fnsh_yn")
    private String brdcFnshYn;
    @XmlElement(name = "urg_yn")
    private String urgYn;
    @XmlElement(name = "frnoti_yn")
    private String frnotiYn;
    @XmlElement(name = "embg_yn")
    private String embgYn;
    @XmlElement(name = "updt_lck_yn")
    private String updtLckYn;
    @XmlElement(name = "internet_only_yn")
    private String internetOnlyYn;
    @XmlElement(name = "sns_yn")
    private String snsYn;
    @XmlElement(name = "inputr_id")
    private String inputrId;
    @XmlElement(name = "inputr_nm")
    private String inputrNm;
    @XmlElement(name = "input_dtm")
    private String inputDtm;
    @XmlElement(name = "news_acum_time")
    private Integer newsAcumTime; // 누적시간
    @XmlElement(name = "video_symbols")
    private TakerCueSheetVideoSymbolDTO takerCueSheetVideoSymbolDTO;
    @XmlElement(name = "audio_symbols")
    private TakerCueSheetAudioSymbolDTO takerCueSheetAudioSymbolDTO;
    @XmlElement(name = "video")
    private TakerCueSheetVideoDTO takerCueSheetVideoDTO;
    @XmlElement(name = "cue_id")
    private Long cueId;
    @XmlElement(name = "cue_tmplt_id")
    private Long cueTmpltId;
    @XmlElement(name = "cue_item_id")
    private Long cueItemId;





    /*private String cueDivCd;
    private String chDivCdNm;
    private String brdcDt;
    private String brdcStartTime;
    private String brdcEndTime;
    private String brdcSchTime;
    private String brdcPgmNm;
    private String cueStCd;
    private String cueStCdNm;
    private String stdioId;
    private String subrmId;
    private String lckYn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date delDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date inputDtm;
    private String delYn;
    private String inputrId;
    private String inputrNm;
    private String delrId;
    private String delrNm;
    private String pd1Id;
    private String pd1Nm;
    private String pd2Id;
    private String pd2Nm;
    private String anc1Id;
    private String anc1Nm;
    private String anc2Id;
    private String anc2Nm;
    private String td1Id;
    private String td1Nm;
    private String td2Id;
    private String td2Nm;
    private String remark;
    private ProgramDTO program;
    private List<CueSheetItemDTO> cueSheetItem = new ArrayList<>();*/
}
