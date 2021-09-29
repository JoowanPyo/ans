package com.gemiso.zodiac.app.cueSheetMedia.mapper;

import com.gemiso.zodiac.app.cueSheetMedia.CueSheetMedia;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueSheetMediaMapper extends GenericMapper<CueSheetMediaDTO, CueSheetMedia, CueSheetMediaDTO> {
}
