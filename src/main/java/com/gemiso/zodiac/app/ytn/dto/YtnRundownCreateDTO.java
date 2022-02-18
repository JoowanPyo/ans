package com.gemiso.zodiac.app.ytn.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YtnRundownCreateDTO {

    private Long yhArtclId;
    private String contId;
    private String agcyCd;

    private String brdcDate;
    private String startTime;
    private String endTime;

    private List<YtnRowDTO> rowList = new ArrayList<>();
}
