package com.gemiso.zodiac.app.nod.mapper;

import com.gemiso.zodiac.app.issue.Issue;
import com.gemiso.zodiac.app.issue.dto.IssueCreateDTO;
import com.gemiso.zodiac.app.nod.Nod;
import com.gemiso.zodiac.app.nod.dto.NodDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NodMapper extends GenericMapper<NodDTO, Nod, NodDTO> {
}
