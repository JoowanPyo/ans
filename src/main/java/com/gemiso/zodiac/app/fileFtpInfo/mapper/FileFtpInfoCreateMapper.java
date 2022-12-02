package com.gemiso.zodiac.app.fileFtpInfo.mapper;

import com.gemiso.zodiac.app.fileFtpInfo.FileFtpInfo;
import com.gemiso.zodiac.app.fileFtpInfo.dto.FileFtpInfoCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileFtpInfoCreateMapper extends GenericMapper<FileFtpInfoCreateDTO, FileFtpInfo, FileFtpInfoCreateDTO> {
}
