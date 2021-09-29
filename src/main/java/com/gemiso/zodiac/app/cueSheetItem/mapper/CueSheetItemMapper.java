package com.gemiso.zodiac.app.cueSheetItem.mapper;

import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueSheetItemMapper extends GenericMapper<CueSheetItemDTO, CueSheetItem, CueSheetItemDTO> {
}
