package com.gemiso.zodiac.app.capTemplateGrp.dto;

import com.gemiso.zodiac.app.code.dto.CodeSimpleDTO;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CapTemplateGrpCreateDTO {

    //private Long tmpltGrpId;
    private String chDivCd;
    @NotNull
    private String tmpltGrpNm;
    private String inputrId;

}
