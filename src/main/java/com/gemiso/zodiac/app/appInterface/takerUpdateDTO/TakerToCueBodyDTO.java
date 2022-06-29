package com.gemiso.zodiac.app.appInterface.takerUpdateDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TakerToCueBodyDTO {

    private Long cue_id;
    private List<TakerToCueBodyDataDTO> body;
    /*@NotNull
    private Long rd_id;
    private String playout_id;
    private String status; // play and cue*/

}
