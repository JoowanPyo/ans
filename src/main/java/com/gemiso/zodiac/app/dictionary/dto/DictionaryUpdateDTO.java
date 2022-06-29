package com.gemiso.zodiac.app.dictionary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictionaryUpdateDTO {

    private Long id;
    private String expl;
    private String wordKo;
    private String wordEn;
    //private Date delDtm;
    //private String inputrId;
    //private String inputrNm;
    private String updtrId;
    //private String updtrNm;
    //private String delrId;
    //private String delrNm;
    //private String delYn;
}
