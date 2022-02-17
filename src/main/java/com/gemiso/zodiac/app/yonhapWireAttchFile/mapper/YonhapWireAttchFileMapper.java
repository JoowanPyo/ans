package com.gemiso.zodiac.app.yonhapWireAttchFile.mapper;

import com.gemiso.zodiac.app.yonhapWireAttchFile.YonhapWireAttchFile;
import com.gemiso.zodiac.app.yonhapWireAttchFile.dto.YonhapWireAttchFileDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface YonhapWireAttchFileMapper extends GenericMapper<YonhapWireAttchFileDTO, YonhapWireAttchFile, YonhapWireAttchFileDTO> {
}
