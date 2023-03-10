package com.gemiso.zodiac.app.cueSheet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.baseProgram.dto.BaseProgramSimpleDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemDTO;
import com.gemiso.zodiac.app.program.dto.ProgramDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetNodDTO {

    private Long cueId;
    private String cueDivCd;
    private String cueDivCdNm;
    private String chDivCd;
    private String chDivCdNm;
    private String brdcDt;
    private String brdcStartTime;
    private String brdcEndTime;
    private String brdcSchTime;
    private String brdcPgmNm;
    private String cueStCd;
    private String cueStCdNm;
    private Long stdioId;
    private String stdioNm;
    private Long subrmId;
    private String subrmNm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date lckDtm;
    private String lckYn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date delDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date inputDtm;
    private String delYn;
    private String inputrId;
    private String inputrNm;
    private String delrId;
    private String delrNm;
    private String pd1Id;
    private String pd1Nm;
    private String pd2Id;
    private String pd2Nm;
    private String anc1Id;
    private String anc1Nm;
    private String anc2Id;
    private String anc2Nm;
    private String lckrId;
    private String lckrNm;
    private String td1Id;
    private String td1Nm;
    private String td2Id;
    private String td2Nm;
    private String remark;
    private String brdcRunTime;
    private Integer cueVer;
    private Integer cueOderVer;
    private Long deptCd;
    private String deptNm;
    private ProgramDTO program;
    private BaseProgramSimpleDTO baseProgram;
    private List<CueSheetItemDTO> cueSheetItem = new ArrayList<>();
    private Integer articleCount; //?????????[??????????????? ?????????]

    private String brdcSt;
}
