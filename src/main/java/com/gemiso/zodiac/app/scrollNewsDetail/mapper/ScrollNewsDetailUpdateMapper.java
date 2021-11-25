package com.gemiso.zodiac.app.scrollNewsDetail.mapper;

import com.gemiso.zodiac.app.scrollNewsDetail.ScrollNewsDetail;
import com.gemiso.zodiac.app.scrollNewsDetail.dto.ScrollNewsDetailUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScrollNewsDetailUpdateMapper extends GenericMapper<ScrollNewsDetailUpdateDTO, ScrollNewsDetail, ScrollNewsDetailUpdateDTO> {
}
