package com.gemiso.zodiac.app.yonhapPoto.mapper;

import com.gemiso.zodiac.app.yonhapPoto.YonhapPoto;
import com.gemiso.zodiac.app.yonhapPoto.dto.YonhapPotoUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface YonhapPotoUpdateMapper extends GenericMapper<YonhapPotoUpdateDTO, YonhapPoto, YonhapPotoUpdateDTO> {
}
