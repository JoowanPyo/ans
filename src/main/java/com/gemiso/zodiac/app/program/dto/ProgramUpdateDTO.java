package com.gemiso.zodiac.app.program.dto;

import com.gemiso.zodiac.app.code.dto.CodeSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateDTO;
import com.gemiso.zodiac.app.dailyProgram.dto.DailyProgramDTO;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
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

}
