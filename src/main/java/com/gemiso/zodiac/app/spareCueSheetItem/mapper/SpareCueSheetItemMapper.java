package com.gemiso.zodiac.app.spareCueSheetItem.mapper;

import com.gemiso.zodiac.app.spareCueSheetItem.SpareCueSheetItem;
import com.gemiso.zodiac.app.spareCueSheetItem.dto.SpareCueSheetItemDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpareCueSheetItemMapper extends GenericMapper<SpareCueSheetItemDTO, SpareCueSheetItem, SpareCueSheetItemDTO> {
}
