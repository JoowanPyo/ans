package com.gemiso.zodiac.app.cueSheet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CueSheetSimpleDTO {

    private Long cueId;
    private String brdcPgmNm;
    private Long subrmId; //부조 아이디
/*    private String cueDivCd;
    private String cueDivCdNm;
    private String chDivCd;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date lckDtm;
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
    private String lckrId;
    private String lckrNm;
    private String td1Id;
    private String td1Nm;
    private String td2Id;
    private String td2Nm;
    private String remark;
    private ProgramDTO program;*/
}
