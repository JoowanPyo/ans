package com.gemiso.zodiac.app.dictionary.dto;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DictionaryDTO {

    private Long id;
    private String expl;
    private String wordKo;
    private String wordEn;
    private Date delDtm;
    private String inputrId;
    private String inputrNm;
    private String updtrId;
    private String updtrNm;
    private String delrId;
    private String delrNm;
    private String delYn;
}
