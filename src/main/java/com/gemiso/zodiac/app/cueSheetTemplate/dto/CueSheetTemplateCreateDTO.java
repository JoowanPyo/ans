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
    private String pd1Id;
    private String pd2Id;
    private String anc1Id;
    //private String anc1Nm;
    private String anc2Id;
    //private String anc2Nm;
    private String td1Id;
    //private String td1Nm;
    private String td2Id;
    //private String td2Nm;
    private Long stdioId;
    //private String stdioNm;
    private Long subrmId;
    //private String subrmNm;
    @NotNull
    private ProgramSimpleDTO program;
    @NotNull
    private BaseProgramSimpleDTO baseProgram;
}
