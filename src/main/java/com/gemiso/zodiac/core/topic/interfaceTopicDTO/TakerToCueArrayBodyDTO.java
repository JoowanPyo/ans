package com.gemiso.zodiac.core.topic.interfaceTopicDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TakerToCueArrayBodyDTO {

    private Long cueItemId;
    private String status;
}
