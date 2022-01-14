package com.gemiso.zodiac.app.program.dto;

import com.gemiso.zodiac.app.code.dto.CodeSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateDTO;
import com.gemiso.zodiac.app.dailyProgram.dto.DailyProgramDTO;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramCreateDTO {

    private Long brdcPgmId;
    private String brdcPgmNm;
    private String chDivCd;
    private String brdcPgmDivCd;
    private String gneDivCd;
    private String prdDivCd;
    private String brdcStartTime;
    private String schTime;
    private String inputDtm;
    private String inputrId;


}
