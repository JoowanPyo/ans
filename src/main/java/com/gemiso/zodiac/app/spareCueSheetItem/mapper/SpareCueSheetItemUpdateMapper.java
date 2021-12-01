package com.gemiso.zodiac.app.spareCueSheetItem.mapper;

import com.gemiso.zodiac.app.spareCueSheetItem.SpareCueSheetItem;
import com.gemiso.zodiac.app.spareCueSheetItem.dto.SpareCueSheetItemUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpareCueSheetItemUpdateMapper extends GenericMapper<SpareCueSheetItemUpdateDTO, SpareCueSheetItem, SpareCueSheetItemUpdateDTO> {
}
