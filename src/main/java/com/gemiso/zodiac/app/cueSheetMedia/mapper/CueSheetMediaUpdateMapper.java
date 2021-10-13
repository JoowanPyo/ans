package com.gemiso.zodiac.app.cueSheetMedia.mapper;

import com.gemiso.zodiac.app.cueSheetMedia.CueSheetMedia;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaDTO;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueSheetMediaUpdateMapper extends GenericMapper<CueSheetMediaUpdateDTO, CueSheetMedia, CueSheetMediaUpdateDTO> {
}
