package com.gemiso.zodiac.app.tag.mapper;

import com.gemiso.zodiac.app.tag.Tag;
import com.gemiso.zodiac.app.tag.dto.TagDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper extends GenericMapper<TagDTO, Tag, TagDTO> {
}
