package com.gemiso.zodiac.app.code.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeCreateDTO {

    //private Long cdId;
    private String cd;
    private String cdNm;
    private String cdNmKo;
    private String cdExpl;
    private String hrnkCdId;
    private String useYn;
    //private String delYn;
    private Integer cdOrd;
    private Date inputDtm;
    //private Date updtDtm;
    //private Date delDtm;
    private String inputrId;
    //private String inputrNm;
    //private String updtrId;
    //private String updtrNm;
    //private String delrId;
    //private String delrNm;
}
