package com.gemiso.zodiac.app.yonhapPoto.mapper;

import com.gemiso.zodiac.app.yonhapPoto.YonhapPoto;
import com.gemiso.zodiac.app.yonhapPoto.dto.YonhapPotoDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface YonhapPotoMapper extends GenericMapper<YonhapPotoDTO, YonhapPoto, YonhapPotoDTO> {
}
