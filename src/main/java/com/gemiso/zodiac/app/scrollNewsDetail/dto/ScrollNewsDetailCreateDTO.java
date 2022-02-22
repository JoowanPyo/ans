package com.gemiso.zodiac.app.scrollNewsDetail.dto;

import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScrollNewsDetailCreateDTO {

    private String titl;
    private int cttOrd;
    private String cttJson;
    private ScrollNewsSimpleDTO scrollNews;
}
