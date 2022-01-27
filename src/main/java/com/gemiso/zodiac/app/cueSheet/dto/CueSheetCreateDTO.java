package com.gemiso.zodiac.app.cueSheet.dto;

import com.gemiso.zodiac.app.baseProgram.dto.BaseProgramSimpleDTO;
import com.gemiso.zodiac.app.program.dto.ProgramSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetCreateDTO {

    //private Long cueId;
    private String cueDivCd;
    @NotNull
    private String chDivCd;
    private String brdcDt;
    private String brdcStartTime;
    private String brdcEndTime;
    private String brdcSchTime;
    private String brdcPgmNm;
    private String cueStCd;
    private Long stdioId;
    private Long subrmId;
    private Date inputDtm;
    private String inputrId;
    private String pd1Id;
    private String pd2Id;
    private String anc1Id;
    private String anc2Id;
    private String td1Id;
    private String td2Id;
    private String remark;
    private String brdcRunTime;
    private BaseProgramSimpleDTO baseProgram;
    private ProgramSimpleDTO program;
}
