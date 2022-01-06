package com.gemiso.zodiac.app.cueSheetTemplateItem.mapper;

import com.gemiso.zodiac.app.cueSheetTemplateItem.CueTmpltItem;
import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmpltItemUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueTmpltItemUpdateMapper extends GenericMapper<CueTmpltItemUpdateDTO, CueTmpltItem, CueTmpltItemUpdateDTO> {
}
