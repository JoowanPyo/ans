package com.gemiso.zodiac.app.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeSimpleDTO {

    private Long cdId;
    private String cd;
    private String cdNm;
    private String cdNmKo;
/*    private String cdExpl;
    private Long hrnkCdId;
    private String useYn;
    private String delYn;
    private Integer cdOrd;
    private Date inputDtm;
    private Date updtDtm;
    private Date delDtm;
    private UserSimpleDTO inputr;
    private UserSimpleDTO updtr;
    private UserSimpleDTO delr;*/
}
