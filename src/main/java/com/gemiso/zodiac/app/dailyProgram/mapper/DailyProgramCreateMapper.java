package com.gemiso.zodiac.app.dailyProgram.mapper;

import com.gemiso.zodiac.app.dailyProgram.DailyProgram;
import com.gemiso.zodiac.app.dailyProgram.dto.DailyProgramCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DailyProgramCreateMapper extends GenericMapper<DailyProgramCreateDTO, DailyProgram, DailyProgramCreateDTO> {
}
