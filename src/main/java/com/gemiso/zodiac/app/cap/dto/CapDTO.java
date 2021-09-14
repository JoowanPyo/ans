package com.gemiso.zodiac.app.cap.dto;

import com.gemiso.zodiac.app.template.dto.TemplateDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CapDTO {

    private Long capTmpltId;
    private String capTmpltNm;
    private String capTmpltFileNm;
    private String capClassCd;
    private int capLnNum;
    private int capLttrNum;
    private String capCellDlmtr;
    private String capTmpltHelp;
    private int capTmpltOrd;
    private int varCnt;
    private String varNm;
    private int takeCount;
    private Long brdcPgmId;
    private String brdcPgmNm;
    private String prvwYn;
    private String useYn;
    private Date delDtm;
    private Date inputDtm;
    private Date updtDtm;
    private String delYn;
    private String inputrId;
    private String updtrId;
    private String delrId;
    private TemplateDTO template;
}
