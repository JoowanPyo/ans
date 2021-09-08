package com.gemiso.zodiac.app.issue.mapper;

import com.gemiso.zodiac.app.issue.Issue;
import com.gemiso.zodiac.app.issue.dto.IssueUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IssueUpdateMapper extends GenericMapper<IssueUpdateDTO, Issue, IssueUpdateDTO> {


}
