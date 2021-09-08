package com.gemiso.zodiac.app.yonhapWire.mapper;

import com.gemiso.zodiac.app.yonhapWire.YonhapWire;
import com.gemiso.zodiac.app.yonhapWire.dto.YonhapCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface YonhapCreateMapper extends GenericMapper<YonhapCreateDTO, YonhapWire, YonhapCreateDTO> {
}
