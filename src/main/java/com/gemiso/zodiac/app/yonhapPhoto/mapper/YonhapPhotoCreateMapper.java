package com.gemiso.zodiac.app.yonhapPhoto.mapper;

import com.gemiso.zodiac.app.yonhapPhoto.YonhapPhoto;
import com.gemiso.zodiac.app.yonhapPhoto.dto.YonhapPhotoCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface YonhapPhotoCreateMapper extends GenericMapper<YonhapPhotoCreateDTO, YonhapPhoto, YonhapPhotoCreateDTO> {
}
