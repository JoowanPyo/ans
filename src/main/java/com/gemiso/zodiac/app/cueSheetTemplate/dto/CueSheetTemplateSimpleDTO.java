package com.gemiso.zodiac.app.cueSheetTemplate.dto;

import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmpltItemSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetTemplateSimpleDTO {

    private Long cueTmpltId;
    //private String brdcPgmNm;
    private String cueTmpltNm;
    /*private String newsDivCd;
    private String newsDivCdNm;
    private String brdcStartTime;
    private String rmk;
    private String pgmschTime;
    private String capHilClrRgb1;
    private String capHilClrRgb2;
    private String capHilClrRgb3;
    private Date inputDtm;
    private Date updtDtm;
    private Date delDtm;
    private String delYn;
    private String pd1Id;
    private String pd1Nm;
    private String pd2Id;
    private String pd2Nm;
    private String anc1Id;
    private String anc1Nm;
    private String anc2Id;
    private String anc2Nm;
    private String inputrId;
    private String inputrNm;
    private String updtrId;
    private String updtrNm;
    private String delrId;
    private String delrNm;
    private ProgramSimpleDTO program;*/
    //private List<CueTmpltItemSimpleDTO> cueTmpltItem;
}
