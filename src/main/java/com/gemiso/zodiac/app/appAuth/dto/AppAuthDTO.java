package com.gemiso.zodiac.app.appAuth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppAuthDTO {

    private Long appAuthId;
    private String appAuthNm;
    private String appAuthCd;
    private int ord;
    private String expl;
    private String memo;
    private String hrnkAppAuthCd;
    private String delYn;
    private String useYn;
    private Date inputDtm;
    private Date updtDtm;
    private Date delDtm;
    private String inputrId;
    private String updtrId;
    private String delrId;

}
