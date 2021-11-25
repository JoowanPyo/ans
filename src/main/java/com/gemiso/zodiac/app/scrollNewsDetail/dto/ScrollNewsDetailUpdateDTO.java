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
public class ScrollNewsDetailUpdateDTO {

    private Long id;
    private String category;
    private String titl;
    private int cttOrd;
    private String ctt;
    private ScrollNewsSimpleDTO scrollNews;
}
