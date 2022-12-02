package com.gemiso.zodiac.app.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsListDTO {

    private List<StatsTotalDTO> orgStatsList;
    private List<StatsTotalDTO> copyStatsList;
}
