package com.gemiso.zodiac.app.dictionary.mapper;

import com.gemiso.zodiac.app.dictionary.Dictionary;
import com.gemiso.zodiac.app.dictionary.dto.DictionaryUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DictionaryUpdateMapper extends GenericMapper<DictionaryUpdateDTO, Dictionary, DictionaryUpdateDTO> {
}
