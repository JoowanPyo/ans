package com.gemiso.zodiac.app.capTemplate.dto;

import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapTemplateSimpleDTO {

    private Long capTmpltId;
    private String capTmpltNm;
    private String capTmpltFileNm;
    private String capClassCd;
    private String capClassCdNm;
    private int capLnNum;
    private int capLttrNum;
    private String capCellDlmtr;
    private String capTmpltHelp;
    private int capTmpltOrd;
    private int varCnt;
    private String varNm;
    private int takeCount;
    /*private Long brdcPgmId;
    private String brdcPgmNm;*/ //맵핑테이블로대처.
    private String prvwYn;
    private String useYn;
    private Date delDtm;
    private Date inputDtm;
    private Date updtDtm;
    private String delYn;
    private String inputrId;
    private String inputrNm;
    private String updtrId;
    private String updtrNm;
    private String delrId;
    private String delrNm;
    private AttachFileDTO attachFile;
    private String url;
    //private CapTemplateGrpDTO capTemplateGrp;
}
