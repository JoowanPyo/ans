package com.gemiso.zodiac.app.capTemplate.dto;

import com.gemiso.zodiac.app.capTemplateGrp.dto.CapTemplateGrpDTO;
import com.gemiso.zodiac.app.code.dto.CodeSimpleDTO;
import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CapTemplateCreateDTO {

    //private Long capTmpltId;
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
    private String prvwYn;
    private String useYn;
    private Date inputDtm;
    private String inputrId;
    private CapTemplateGrpDTO capTemplateGrp;
    private AttachFileDTO attachFile;
    //private String url;
}
