package com.gemiso.zodiac.app.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
    private Integer cdOrd;
    private String inputrId;

}
