package com.gemiso.zodiac.app.scrollNewsDetail.dto;

import com.gemiso.zodiac.app.issue.dto.IssueDTO;
import com.gemiso.zodiac.app.scrollNews.ScrollNews;
import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsDTO;
import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScrollNewsDetailDTO {

    private Long id;
    private String category;
    private String categoryNm;
    private String titl;
    private int cttOrd;
    private String ctt;
    private ScrollNewsSimpleDTO scrollNews;
}
