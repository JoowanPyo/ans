package com.gemiso.zodiac.app.cueCapTmplt.mapper;

import com.gemiso.zodiac.app.cueCapTmplt.CueCapTmplt;
import com.gemiso.zodiac.app.cueCapTmplt.dto.CueCapTmpltUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueCapTmpltUpdateMapper extends GenericMapper<CueCapTmpltUpdateDTO, CueCapTmplt, CueCapTmpltUpdateDTO> {
}
