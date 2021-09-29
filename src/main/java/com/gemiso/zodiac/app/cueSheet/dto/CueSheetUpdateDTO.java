package com.gemiso.zodiac.app.cueSheet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.program.dto.ProgramDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetUpdateDTO {

    private Long cueId;
    private String cueDivCd;
    private String chDivCd;
    private String brdcDt;
    private String brdcStartTime;
    private String brdcEndTime;
    private String brdcSchTime;
    private String brdcPgmNm;
    private String cueStCd;
    private String stdioId;
    private String subrmId;
    private Date lckDtm;
    private String lckYn;
    //private Date delDtm;
    //private Date inputDtm;
    private String delYn;
    //private String inputrId;
    //private String delrId;
    private String pd1Id;
    private String pd2Id;
    private String anc1Id;
    private String anc2Id;
    private String lckrId;
    private String td1Id;
    private String td2Id;
    private String remark;
    private ProgramDTO program;
}
