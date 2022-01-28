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
    private ProgramSimpleDTO program;
    private BaseProgramSimpleDTO baseProgram;
    //private List<CueTmplSymbolDTO> cueTmplSymbol = new ArrayList<>();
}
