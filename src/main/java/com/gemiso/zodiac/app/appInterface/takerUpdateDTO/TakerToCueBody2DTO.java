package com.gemiso.zodiac.app.appInterface.takerUpdateDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TakerToCueBody2DTO {

    private Long cue_id;
    @NotNull
    private Long rd_id;
    private String playout_id;
    private String status; // play and cue
}
