package com.gemiso.zodiac.app.capTemplateGrp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapTemplateGrpCreateDTO {

    //private Long tmpltGrpId;
    private String chDivCd;
    @NotNull
    private String tmpltGrpNm;
    private String inputrId;

}
