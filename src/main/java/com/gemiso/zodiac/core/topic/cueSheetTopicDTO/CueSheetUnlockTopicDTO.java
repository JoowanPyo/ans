package com.gemiso.zodiac.core.topic.cueSheetTopicDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CueSheetUnlockTopicDTO {

    private String msg;
    private Long cueId;
    private String userId;
    private String userNm;
}
