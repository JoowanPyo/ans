package com.gemiso.zodiac.app.facilityManage.mapper;

import com.gemiso.zodiac.app.facilityManage.FacilityManage;
import com.gemiso.zodiac.app.facilityManage.dto.FacilityManageDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FacilityManageMapper extends GenericMapper<FacilityManageDTO, FacilityManage, FacilityManageDTO> {
}
