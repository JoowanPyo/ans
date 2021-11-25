package com.gemiso.zodiac.app.scrollNewsDetail.mapper;

import com.gemiso.zodiac.app.scrollNewsDetail.ScrollNewsDetail;
import com.gemiso.zodiac.app.scrollNewsDetail.dto.ScrollNewsDetailCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScrollNewsDetailCreateMapper extends GenericMapper<ScrollNewsDetailCreateDTO, ScrollNewsDetail, ScrollNewsDetailCreateDTO> {
}
