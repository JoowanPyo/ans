package com.gemiso.zodiac.app.cueCapTmplt.dto;

import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CueCapTmpltUpdateDTO {

    private Long id;
    private String cgDivCd;
    private CapTemplateSimpleDTO capTemplate;
    //private ProgramSimpleDTO program;
}
