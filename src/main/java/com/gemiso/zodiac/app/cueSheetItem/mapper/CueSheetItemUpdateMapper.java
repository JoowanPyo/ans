package com.gemiso.zodiac.app.cueSheetItem.mapper;

import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueSheetItemUpdateMapper extends GenericMapper<CueSheetItemUpdateDTO, CueSheetItem, CueSheetItemUpdateDTO> {
}
