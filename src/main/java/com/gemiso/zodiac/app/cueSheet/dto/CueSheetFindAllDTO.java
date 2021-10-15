package com.gemiso.zodiac.app.cueSheet.dto;

import com.gemiso.zodiac.app.program.dto.ProgramDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetFindAllDTO {

    private List<CueSheetDTO> cueSheetDTO = new ArrayList<>();
    private List<ProgramDTO> programDTO = new ArrayList<>();
}
