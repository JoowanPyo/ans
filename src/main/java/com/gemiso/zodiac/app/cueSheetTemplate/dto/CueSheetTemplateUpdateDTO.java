package com.gemiso.zodiac.app.cueSheetTemplate.dto;

import com.gemiso.zodiac.app.baseProgram.dto.BaseProgramSimpleDTO;
import com.gemiso.zodiac.app.program.dto.ProgramSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetTemplateUpdateDTO {

    private Long cueTmpltId;
    private String brdcPgmNm;
    private String cueTmpltNm;
    private String newsDivCd;
    //private String newsDivCdNm;
    private String rmk;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    //private Date inputDtm;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    //private Date updtDtm;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    //private Date delDtm;
    private String delYn;
    //private String inputrId;
    //private String inputrNm;
    private String updtrId;
    //private String updtrNm;
    //private String delrId;
    //private String delrNm;
    private ProgramSimpleDTO program;
    private BaseProgramSimpleDTO baseProgram;
    //private List<CueTmplSymbolDTO> cueTmplSymbol = new ArrayList<>();
}
