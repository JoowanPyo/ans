package com.gemiso.zodiac.app.scrollNewsDetail.dto;

import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScrollNewsDetailUpdateDTO {

    private Long id;
    private String titl;
    private Integer cttOrd;
    private String cttJson;
    private ScrollNewsSimpleDTO scrollNews;
}
