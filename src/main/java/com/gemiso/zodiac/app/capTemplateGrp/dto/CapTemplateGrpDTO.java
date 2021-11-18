package com.gemiso.zodiac.app.capTemplateGrp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CapTemplateGrpDTO {

    private Long tmpltGrpId;
    private String chDivCd;
    private String chDivCdNm;
    private String tmpltGrpNm;
    /*private String brdcPgmId;*/
    private String inputrId;
    private String inputrNm;
    private String updtrId;
    private String updtrNm;
    private String delYn;
    private Date inputDtm;
    private Date updtDtm;
}
