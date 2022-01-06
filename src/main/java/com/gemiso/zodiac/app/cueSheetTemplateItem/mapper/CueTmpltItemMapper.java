package com.gemiso.zodiac.app.cueSheetTemplateItem.mapper;

import com.gemiso.zodiac.app.cueSheetTemplateItem.CueTmpltItem;
import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmpltItemDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueTmpltItemMapper extends GenericMapper<CueTmpltItemDTO, CueTmpltItem, CueTmpltItemDTO> {
}
