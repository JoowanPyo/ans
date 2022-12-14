package com.gemiso.zodiac.app.program.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramCreateDTO {

    private String brdcPgmId;
    private String brdcPgmNm;
    private String brdcPgmNmEn; //영문명
    private String chDivCd;
    private String brdcPgmDivCd;
    private String gneDivCd;
    private String prdDivCd;
    private String brdcStartTime;
    private String schTime;
    private String inputDtm;
    private String inputrId;
    private String scriptYn;
    private String nodYn;
    private String useYn;


}
