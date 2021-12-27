package com.gemiso.zodiac.app.dailyProgram.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.code.dto.CodeSimpleDTO;
import com.gemiso.zodiac.app.program.Program;
import com.gemiso.zodiac.app.program.dto.ProgramDTO;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyProgramDTO {

    private Long dailyPgmId;
    private String brdcDt;
    private int brdcSeq;
    private String brdcStartTime;
    private String brdcEndClk;
    private String brdcDivCd;
    private String brdcDivCdNm;
    private String brdcPgmNm;
    private String srcDivCd;
    private String srcDivCdNm;
    private String stdioId;
    private String subrmId;
    private String pgmschYn;
    private String rmk;
    private String inputDtm;
    private String updtDtm;
    private String inputrId;
    private String inputrNm;
    private String updtrId;
    private String updtrNm;
    private ProgramDTO program;
}
