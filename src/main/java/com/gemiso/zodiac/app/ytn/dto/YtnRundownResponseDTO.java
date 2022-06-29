package com.gemiso.zodiac.app.ytn.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YtnRundownResponseDTO {

    private Long yhArtclId;
    private String agcyCd;
    private String contId;

    private String brdcDate;
    private String startTime;
    private String endTime;

    private List<YtnRowDTO> rowList;
}
