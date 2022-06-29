package com.gemiso.zodiac.app.dailyProgram.dto;

import com.gemiso.zodiac.app.baseProgram.dto.BaseProgramSimpleDTO;
import com.gemiso.zodiac.app.program.dto.ProgramDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
    private Long stdioId;
    private Long subrmId;
    private String pgmschYn;
    private String rmk;
    private String inputDtm;
    private String updtDtm;
    private String inputrId;
    private String inputrNm;
    private String updtrId;
    private String updtrNm;
    private String brdcRunTime;
    private ProgramDTO program;
    private BaseProgramSimpleDTO baseProgram;
}
