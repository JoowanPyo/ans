package com.gemiso.zodiac.app.cueSheetMedia.mapper;

import com.gemiso.zodiac.app.cueSheetMedia.CueSheetMedia;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueSheetMediaCreateMapper extends GenericMapper<CueSheetMediaCreateDTO, CueSheetMedia, CueSheetMediaCreateDTO> {
}
