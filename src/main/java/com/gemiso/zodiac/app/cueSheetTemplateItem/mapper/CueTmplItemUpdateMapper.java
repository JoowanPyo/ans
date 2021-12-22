package com.gemiso.zodiac.app.cueSheetTemplateItem.mapper;

import com.gemiso.zodiac.app.cueSheetTemplateItem.CueTmplItem;
import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmplItemUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueTmplItemUpdateMapper extends GenericMapper<CueTmplItemUpdateDTO, CueTmplItem, CueTmplItemUpdateDTO> {
}
