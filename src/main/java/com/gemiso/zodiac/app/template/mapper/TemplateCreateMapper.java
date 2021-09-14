package com.gemiso.zodiac.app.template.mapper;

import com.gemiso.zodiac.app.template.Template;
import com.gemiso.zodiac.app.template.dto.TemplateCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TemplateCreateMapper extends GenericMapper<TemplateCreateDTO, Template, TemplateCreateDTO> {
}
