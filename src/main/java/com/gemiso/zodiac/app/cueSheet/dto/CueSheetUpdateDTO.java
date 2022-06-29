package com.gemiso.zodiac.app.cueSheet.dto;

import com.gemiso.zodiac.app.baseProgram.dto.BaseProgramSimpleDTO;
import com.gemiso.zodiac.app.program.dto.ProgramSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetUpdateDTO {

    private Long cueId;
    private String cueDivCd;
    //private String cueDivCdNm;
    private String chDivCd;
    //private String chDivCdNm;
    private String brdcDt;
    private String brdcStartTime;
    private String brdcEndTime;
    private String brdcSchTime;
    private String brdcPgmNm;
    private String cueStCd;
    //private String cueStCdNm;
    private Long stdioId;
    private Long subrmId;
    //private Date lckDtm;
    //private String lckYn;
    //private Date delDtm;
    //private Date inputDtm;
    //private String delYn;
    //private String inputrId;
    //private String inputrNm;
    //private String delrId;
    //private String delrNm;
    private String pd1Id;
    //private String pd1Nm;
    private String pd2Id;
    //private String pd2Nm;
    private String anc1Id;
    //private String anc1Nm;
    private String anc2Id;
    //private String anc2Nm;
    //private String lckrId;
    //private String lckrNm;
    private String td1Id;
    //private String td1Nm;
    private String td2Id;
    //private String td2Nm;
    private String remark;
    private String brdcRunTime;
    private Integer cueVer;
    private Integer cueOderVer;
    private Long deptCd;
    //private String deptNm;
    private BaseProgramSimpleDTO baseProgram;
    private ProgramSimpleDTO program;
    //private List<CueSheetItemDTO> cueSheetItem = new ArrayList<>();
}
