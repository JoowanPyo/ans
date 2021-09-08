package com.gemiso.zodiac.app.file.mapper;

import com.gemiso.zodiac.app.file.AttachFile;
import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttachFileMapper extends GenericMapper<AttachFileDTO, AttachFile, AttachFileDTO> {
}
