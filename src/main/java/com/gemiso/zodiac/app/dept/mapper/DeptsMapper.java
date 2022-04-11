package com.gemiso.zodiac.app.dept.mapper;

import com.gemiso.zodiac.app.dept.Depts;
import com.gemiso.zodiac.app.dept.dto.DeptsDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeptsMapper extends GenericMapper<DeptsDTO, Depts, DeptsDTO> {
}
