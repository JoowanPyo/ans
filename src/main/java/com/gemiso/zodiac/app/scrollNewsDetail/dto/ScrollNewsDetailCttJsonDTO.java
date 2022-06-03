package com.gemiso.zodiac.app.scrollNewsDetail.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScrollNewsDetailCttJsonDTO {

    private String line;
    private Integer lineOrd;
}
