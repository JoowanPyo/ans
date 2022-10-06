package com.gemiso.zodiac.app.rundownItem.mapper;

import com.gemiso.zodiac.app.rundownItem.RundownItem;
import com.gemiso.zodiac.app.rundownItem.dto.RundownItemCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RundownItemCreateMapper extends GenericMapper<RundownItemCreateDTO, RundownItem, RundownItemCreateDTO> {
}
