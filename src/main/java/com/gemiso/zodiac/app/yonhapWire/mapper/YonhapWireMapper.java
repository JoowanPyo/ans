package com.gemiso.zodiac.app.yonhapWire.mapper;

import com.gemiso.zodiac.app.yonhapWire.YonhapWire;
import com.gemiso.zodiac.app.yonhapWire.dto.YonhapWireCreateDTO;
import com.gemiso.zodiac.app.yonhapWire.dto.YonhapWireDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface YonhapWireMapper extends GenericMapper<YonhapWireDTO, YonhapWire, YonhapWireDTO> {

    List<YonhapWireDTO> toDtoList(List<YonhapWire> yonhapWireList);
}
