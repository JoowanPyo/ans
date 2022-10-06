package com.gemiso.zodiac.app.rundownItem.mapper;

import com.gemiso.zodiac.app.rundownItem.RundownItem;
import com.gemiso.zodiac.app.rundownItem.dto.RundownItemUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RundownItemUpdateMapper extends GenericMapper<RundownItemUpdateDTO, RundownItem, RundownItemUpdateDTO> {
}
