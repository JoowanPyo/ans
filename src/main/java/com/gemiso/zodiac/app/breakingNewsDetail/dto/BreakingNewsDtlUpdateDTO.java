package com.gemiso.zodiac.app.breakingNewsDetail.dto;

import com.gemiso.zodiac.app.breakingNews.dto.BreakingNewsSimplerDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreakingNewsDtlUpdateDTO {

    private Long id;
    private int ord;
    private String ctt;
    private BreakingNewsSimplerDTO breakingNews;
}
