package com.gemiso.zodiac.app.template.mapper;

import com.gemiso.zodiac.app.template.Template;
import com.gemiso.zodiac.app.template.dto.TemplateUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TemplateUpdateMapper extends GenericMapper<TemplateUpdateDTO, Template, TemplateUpdateDTO> {
}
