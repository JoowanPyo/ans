package com.gemiso.zodiac.app.capTemplate.dto;

import com.gemiso.zodiac.app.capTemplateGrp.dto.CapTemplateGrpDTO;
import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapTemplateCreateDTO {

    //private Long capTmpltId;
    private String capTmpltNm;
    private String capTmpltFileNm;
    private String capClassCd;
    private Integer capLnNum;
    private String capLttrNum;
    private String capCellDlmtr;
    private String capTmpltHelp;
    private Integer capTmpltOrd;
    private Integer varCnt;
    private String varNm;
    private Integer takeCount;
    private String prvwYn;
    private String useYn;
    private Date inputDtm;
    private String inputrId;
    private CapTemplateGrpDTO capTemplateGrp;
    private AttachFileDTO attachFile;
    //private String url;
}
