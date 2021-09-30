package com.gemiso.zodiac.app.capTemplateGrp.dto;

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
public class CapTemplateGrpCreateDTO {

    //private Long tmpltGrpId;
    private String chDivCd;
    private String tmpltGrpNm;
    private String brdcPgmId;
    private UserSimpleDTO inputr;
    //private String updtrId;
    private String delYn;
    private Date inputDtm;
    //private Date updtDtm;
}
