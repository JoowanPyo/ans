package com.gemiso.zodiac.core.topic.interfaceTopicDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TakerToCueTopic2DTO {

    private String eventId;
    private Long cueId;
    private int cueVer;
    private int cueOderVer;
    private Long artclId;
    private Long cueItemId;
    private String status;
}
