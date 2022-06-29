package com.gemiso.zodiac.app.cueSheet.dto;

import com.gemiso.zodiac.app.dailyProgram.dto.DailyProgramDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetFindAllDTO {

    private List<CueSheetDTO> cueSheetDTO = new ArrayList<>();
    private List<DailyProgramDTO> dailyProgramDTO = new ArrayList<>();
}
