package com.gemiso.zodiac.app.cueSheet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.program.dto.ProgramDTO;
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
public class CueSheetUpdateDTO {

    private Long cueId;
    private String cueDivCd;
    private String chDivCd;
    private String brdcDt;
    private String brdcStartTime;
    private String brdcEndTime;
    private String brdcSchTime;
    private String brdcPgmNm;
    private String cueStCd;
    private String stdioId;
    private String subrmId;
    private Date lckDtm;
    private String lckYn;
    //private Date delDtm;
    //private Date inputDtm;
    private String delYn;
    //private UserSimpleDTO inputr;
    //private UserSimpleDTO delr;
    private UserSimpleDTO pd1;
    private UserSimpleDTO pd2;
    private UserSimpleDTO anc1;
    private UserSimpleDTO anc2;
    private UserSimpleDTO lckr;
    private UserSimpleDTO td1;
    private UserSimpleDTO td2;
    private ProgramDTO program;
}
