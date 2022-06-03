package com.gemiso.zodiac.app.scrollNewsDetail.dto;

import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScrollNewsDetailCreateDTO {

    private String titl;
    private Integer cttOrd;
    private List<ScrollNewsDetailCttJsonDTO> cttJsons;
    private ScrollNewsSimpleDTO scrollNews;
}
