package com.gemiso.zodiac.app.cueCapTmplt.dto;

import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateSimpleDTO;
import com.gemiso.zodiac.app.program.dto.ProgramSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueCapTmpltCreateDTO {

    //private Long id;
    private String cgDivCd;
    private CapTemplateSimpleDTO capTemplate;
    private ProgramSimpleDTO program;
}
