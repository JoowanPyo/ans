package com.gemiso.zodiac.app.cueCapTmplt.dto;

import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateSimpleDTO;
import com.gemiso.zodiac.app.program.dto.ProgramSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CueCapTmpltDTO {

    private Long id;
    private String cgDivCd;
    private CapTemplateSimpleDTO capTemplate;
    private ProgramSimpleDTO program;
}
