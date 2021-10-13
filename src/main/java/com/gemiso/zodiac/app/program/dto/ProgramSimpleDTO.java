package com.gemiso.zodiac.app.program.dto;

import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramSimpleDTO {

    private Long brdcPgmId;
    private String brdcPgmNm;
    private String chDivCd;
    private String brdcPgmDivCd;
    private String gneDivCd;
    private String prdDivCd;
    private String brdcStartTime;
    private String schTime;
    private Date inputDtm;
    private Date updtDtm;
    private String delYn;
    private Date delDtm;
    private UserSimpleDTO inputr;
    private UserSimpleDTO updtr;
    private UserSimpleDTO delr;
}
