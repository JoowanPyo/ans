package com.gemiso.zodiac.app.issue.mapper;

import com.gemiso.zodiac.app.issue.Issue;
import com.gemiso.zodiac.app.issue.dto.IssueDTO;
import com.gemiso.zodiac.app.issue.dto.IssueUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface IssueMapper extends GenericMapper<IssueDTO, Issue, IssueDTO> {

}
