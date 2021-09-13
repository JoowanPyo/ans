package com.gemiso.zodiac.app.program.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramCreateDTO {

    //private Long brdcPgmId;
    private String brdcPgmNm;
    private String chDivCd;
    private String brdcPgmDivCd;
    private String gneDivCd;
    private String prdDivCd;
    private String brdcStartTime;
    private String schTime;
    private Date inputDtm;
    //private Date updtDtm;
    //private String delYn;
    //private Date delDtm;
    private String inputrId;
    //private String updtrId;
    //private String delrId;
}
