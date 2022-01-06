package com.gemiso.zodiac.app.cueSheetTemplateItemCap.mapper;

import com.gemiso.zodiac.app.cueSheetTemplateItemCap.CueTmpltItemCap;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.dto.CueTmpltItemCapUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueTmpltItemCapUpdateMapper extends GenericMapper<CueTmpltItemCapUpdateDTO, CueTmpltItemCap, CueTmpltItemCapUpdateDTO> {
}
