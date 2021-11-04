package com.gemiso.zodiac.app.facilityManage.mapper;

import com.gemiso.zodiac.app.facilityManage.FacilityManage;
import com.gemiso.zodiac.app.facilityManage.dto.FacilityManageCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FacilityManageCreateMapper extends GenericMapper<FacilityManageCreateDTO, FacilityManage, FacilityManageCreateDTO> {
}
