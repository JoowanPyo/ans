package com.gemiso.zodiac.app.baseProgram.dto;

import com.gemiso.zodiac.app.program.dto.ProgramSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseProgramUpdateDTO {

    private String basePgmschId;
    private String basDt;
    private String chDivCd;
    private String brdcDay;
    private String brdcDivCd;
    private String brdcStartClk;
    private String brdcEndClk;
    private String brdcStartDt;
    private String brdcEndDt;
    private String brdcSubNm;
    private String endpgmDt;
    private String endpgmYn;
    //private Date inputDtm;
    private String updtDtm;
    //private Date delDtm;
    //private String delYn;
    private String pd1Id;
    //private String lckrNm;
    private String pd2Id;
    //private String pd2Nm;
    private String anc1Id;
    //private String anc1Nm;
    private String anc2Id;
    //private String anc2Nm;
    private String td1Id;
    //private String td1Nm;
    private String td2Id;
    //private String td2Nm;
    //private String inputrId;
    //private String inputrNm;
    //private String delrId;
    //private String delrNm;
    private String updtrId;
    //private String updtrNm;
    private String basDtId;
    private String capId;
    private String capLoc;
    private int basDtVer;
    private String vfId;
    private String basDtUseYn;
    private String vsId;
    private String makeYear;
    private ProgramSimpleDTO program;
}
