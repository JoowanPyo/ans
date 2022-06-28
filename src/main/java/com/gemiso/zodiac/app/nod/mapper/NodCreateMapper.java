package com.gemiso.zodiac.app.nod.mapper;

import com.gemiso.zodiac.app.nod.Nod;
import com.gemiso.zodiac.app.nod.dto.NodCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NodCreateMapper extends GenericMapper<NodCreateDTO, Nod, NodCreateDTO> {
}
