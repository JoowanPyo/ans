package com.gemiso.zodiac.app.program.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramUpdateDTO {

    private String brdcPgmId;
    private String brdcPgmNm;
    private String chDivCd;
    //private String chDivCdNm;
    private String brdcPgmDivCd;
    //private String brdcPgmDivCdNm;
    private String gneDivCd;
    //private String gneDivCdNm;
    private String prdDivCd;
    //private String prdDivCdNm;
    private String brdcStartTime;
    private String schTime;
    //private Date inputDtm;
    private String updtDtm;
    //private String delYn;
    //private Date delDtm;
    //private String inputrId;
    //private String inputrNm;
    private String updtrId;
    //private String updtrNm;
    //private String delrId;
    //private String delrNm;
    private String scriptYn;
    private String nodYn;
    private String useYn;

}
