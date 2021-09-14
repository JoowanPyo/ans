package com.gemiso.zodiac.app.template.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TemplateUpdateDTO {

    private Long tmpltGrpId;
    private String chDivCd;
    private String tmpltGrpNm;
    private String brdcPgmId;
    //private String inputrId;
    private String updtrId;
    private String delYn;
    //private Date inputDtm;
    private Date updtDtm;
}
