package com.gemiso.zodiac.app.issue.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueCreateDTO {

    //private Long issuId;
    private String chDivCd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date issuDtm;
    private Integer issuOrd;
    private String issuKwd;
    private String issuCtt;
    private String issuFnshYn;
    //private String issuDelYn;
    //private Date delDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date  issuFnshDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date inputDtm;
    //private Date updtDtm;
    //private Long issuOrgId;
    private String inputrId;
    //private String updtrId;
    //private String delrId;

}
