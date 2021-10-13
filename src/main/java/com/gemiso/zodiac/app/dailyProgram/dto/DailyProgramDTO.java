package com.gemiso.zodiac.app.dailyProgram.dto;

import com.gemiso.zodiac.app.program.Program;
import com.gemiso.zodiac.app.program.dto.ProgramDTO;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyProgramDTO {

    private Long id;
    private String brdcDt;
    private String brdcSeq;
    private String brdcStartTime;
    private String brdcEndClk;
    private String brdcDivCd;
    private String brdcPgmNm;
    private String srcDivCd;
    private String stdioId;
    private String subrmId;
    private String pgmschYn;
    private String rmk;
    private UserSimpleDTO inputr;
    private UserSimpleDTO updtr;
    private ProgramDTO program;
}
