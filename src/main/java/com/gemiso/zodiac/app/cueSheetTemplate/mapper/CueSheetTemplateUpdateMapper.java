package com.gemiso.zodiac.app.cueSheetTemplate.mapper;

import com.gemiso.zodiac.app.cueSheetTemplate.CueSheetTemplate;
import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueSheetTemplateUpdateMapper extends GenericMapper<CueSheetTemplateUpdateDTO, CueSheetTemplate, CueSheetTemplateUpdateDTO> {
}
