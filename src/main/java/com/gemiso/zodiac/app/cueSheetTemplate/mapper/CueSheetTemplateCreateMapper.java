package com.gemiso.zodiac.app.cueSheetTemplate.mapper;

import com.gemiso.zodiac.app.cueSheetTemplate.CueSheetTemplate;
import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueSheetTemplateCreateMapper extends GenericMapper<CueSheetTemplateCreateDTO,
                                                            CueSheetTemplate, CueSheetTemplateCreateDTO> {
}
