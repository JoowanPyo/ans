package com.gemiso.zodiac.app.program.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.code.dto.CodeSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplate.CueSheetTemplate;
import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateDTO;
import com.gemiso.zodiac.app.dailyProgram.DailyProgram;
import com.gemiso.zodiac.app.dailyProgram.dto.DailyProgramDTO;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramDTO {

    private String brdcPgmId;
    private String brdcPgmNm;
    private String chDivCd;
    private String chDivCdNm;
    private String brdcPgmDivCd;
    private String brdcPgmDivCdNm;
    private String gneDivCd;
    private String gneDivCdNm;
    private String prdDivCd;
    private String prdDivCdNm;
    private String brdcStartTime;
    private String schTime;
    private String inputDtm;
    private String updtDtm;
    private String delYn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date delDtm;
    private String inputrId;
    private String inputrNm;
    private String updtrId;
    private String updtrNm;
    private String delrId;
    private String delrNm;
    private String scriptYn;
    private String nodYn;
    private String useYn;

    /*private List<CueSheetTemplateDTO> cueSheetTemplate;
    private List<DailyProgramDTO> dailyProgram;*/
}
