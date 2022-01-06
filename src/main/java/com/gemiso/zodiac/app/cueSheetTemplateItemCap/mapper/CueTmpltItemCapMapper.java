package com.gemiso.zodiac.app.cueSheetTemplateItemCap.mapper;

import com.gemiso.zodiac.app.cueSheetTemplateItemCap.CueTmpltItemCap;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.dto.CueTmpltItemCapDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueTmpltItemCapMapper extends GenericMapper<CueTmpltItemCapDTO, CueTmpltItemCap, CueTmpltItemCapDTO> {
}
