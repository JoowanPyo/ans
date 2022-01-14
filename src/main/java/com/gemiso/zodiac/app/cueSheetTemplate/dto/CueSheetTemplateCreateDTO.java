package com.gemiso.zodiac.app.cueSheetTemplate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.baseProgram.dto.BaseProgramSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplateSymbol.dto.CueTmplSymbolDTO;
import com.gemiso.zodiac.app.program.dto.ProgramSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetTemplateCreateDTO {

    //private Long cueTmpltId;
    private String brdcPgmNm;
    private String cueTmpltNm;
    private String newsDivCd;
    private String rmk;
    private String delYn;
    private String inputrId;
    @NotNull
    private ProgramSimpleDTO program;
    @NotNull
    private BaseProgramSimpleDTO baseProgram;
}
