package com.gemiso.zodiac.app.dictionary.mapper;

import com.gemiso.zodiac.app.dictionary.Dictionary;
import com.gemiso.zodiac.app.dictionary.dto.DictionaryDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DictionaryMapper extends GenericMapper<DictionaryDTO, Dictionary, DictionaryDTO> {
}
