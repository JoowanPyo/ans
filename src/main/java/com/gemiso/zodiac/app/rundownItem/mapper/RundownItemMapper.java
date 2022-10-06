package com.gemiso.zodiac.app.rundownItem.mapper;

import com.gemiso.zodiac.app.rundownItem.RundownItem;
import com.gemiso.zodiac.app.rundownItem.dto.RundownItemDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RundownItemMapper extends GenericMapper<RundownItemDTO, RundownItem, RundownItemDTO> {
}
