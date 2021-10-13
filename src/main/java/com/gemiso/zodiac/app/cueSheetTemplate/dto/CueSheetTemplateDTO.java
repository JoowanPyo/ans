package com.gemiso.zodiac.app.cueSheetTemplate.dto;

import com.gemiso.zodiac.app.program.Program;
import com.gemiso.zodiac.app.program.dto.ProgramDTO;
import com.gemiso.zodiac.app.program.dto.ProgramSimpleDTO;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetTemplateDTO {

    private Long cueTmpltId;
    private String brdcPgmNm;
    private String cueTmpltNm;
    private String newsDivCd;
    private String brdcStartTime;
    private String pd1Nm;
    private String pd2Nm;
    private String anc1Nm;
    private String anc2Nm;
    private String rmk;
    private String pgmschTime;
    private String capHilClrRgb1;
    private String capHilClrRgb2;
    private String capHilClrRgb3;
    private Date delDtm;
    private String delYn;
    private UserSimpleDTO pd1;
    private UserSimpleDTO pd2;
    private UserSimpleDTO anc1;
    private UserSimpleDTO anc2;
    private UserSimpleDTO inputr;
    private UserSimpleDTO updtr;
    private UserSimpleDTO delr;
    private ProgramSimpleDTO program;
}
