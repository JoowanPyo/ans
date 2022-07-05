package com.gemiso.zodiac.core.topic.interfaceTopicDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TakerCueSheetTopicDTO {

    private String event_id;
    private Long cue_id;
    private int cue_ver;
    private int cue_oder_ver;
    private Long artcl_id;
    private Long cue_item_id;
    private Long cue_tmplt_id;
    private String spare_yn;
    private String prompter;
    private String video_taker;
}
