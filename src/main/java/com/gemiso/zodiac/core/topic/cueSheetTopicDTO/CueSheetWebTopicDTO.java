package com.gemiso.zodiac.core.topic.cueSheetTopicDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CueSheetWebTopicDTO {

    private String eventId;
    private Long cueId;
    private int cueVer;
    private int cueOderVer;
    private Long artclId;
    private Long cueItemId;
    private String spareYn;
}
