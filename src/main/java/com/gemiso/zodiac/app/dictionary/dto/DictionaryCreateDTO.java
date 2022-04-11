package com.gemiso.zodiac.app.dictionary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DictionaryCreateDTO {

    //private Long id;
    private String expl;
    private String wordKo;
    private String wordEn;
    private String inputrId;
}
