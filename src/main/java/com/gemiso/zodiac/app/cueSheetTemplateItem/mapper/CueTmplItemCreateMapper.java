package com.gemiso.zodiac.app.cueSheetTemplateItem.mapper;

import com.gemiso.zodiac.app.cueSheetTemplateItem.CueTmplItem;
import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmplItemCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueTmplItemCreateMapper extends GenericMapper<CueTmplItemCreateDTO, CueTmplItem, CueTmplItemCreateDTO> {
}
