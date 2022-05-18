package com.gemiso.zodiac.core.topic.articleTopicDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TakerToCueMqDTO {

    private String eventId;
    private Long cueId;
    private int cueVer;
    private int cueOderVer;
    private Long artclId;
    private Long cueItemId;
}
