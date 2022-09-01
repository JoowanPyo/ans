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
public class CapTemplateUpdateDTO {

    private Long capTmpltId;
    private String capTmpltNm;
    private String capTmpltFileNm;
    private String capClassCd;
    //private String capClassCdNm;
    private Integer capLnNum;
    private String capLttrNum;
    private String capCellDlmtr;
    private String capTmpltHelp;
    //private Integer capTmpltOrd;
    private Integer varCnt;
    private String varNm;
    private Integer takeCount;
    /*private Long brdcPgmId;
    private String brdcPgmNm;*///맵핑테이블로대처.
    private String prvwYn;
    private String useYn;
    //private Date delDtm;
    //private Date inputDtm;
    private Date updtDtm;
    //private String delYn;
    //private String inputrId;
    //private String inputrNm;
    private String updtrId;
    //private String updtrNm;
    //private String delrId;
    //private String delrNm;
    private CapTemplateGrpDTO capTemplateGrp;
    private AttachFileDTO attachFile;
    //private String url;
}
