package com.gemiso.zodiac.app.cueSheetTemplateItemCap.mapper;

import com.gemiso.zodiac.app.cueSheetTemplateItemCap.CueTmpltItemCap;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.dto.CueTmpltItemCapCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueTmpltItemCapCreateMapper extends GenericMapper<CueTmpltItemCapCreateDTO, CueTmpltItemCap, CueTmpltItemCapCreateDTO> {
}
