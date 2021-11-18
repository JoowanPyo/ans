package com.gemiso.zodiac.app.cueCapTmplt.mapper;

import com.gemiso.zodiac.app.cueCapTmplt.CueCapTmplt;
import com.gemiso.zodiac.app.cueCapTmplt.dto.CueCapTmpltDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueCapTmpltMapper extends GenericMapper<CueCapTmpltDTO, CueCapTmplt, CueCapTmpltDTO> {
}
