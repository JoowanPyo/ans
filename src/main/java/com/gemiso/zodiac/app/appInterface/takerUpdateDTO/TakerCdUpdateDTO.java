package com.gemiso.zodiac.app.appInterface.takerUpdateDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TakerCdUpdateDTO {

    @NotNull
    private String cue_st_cd;
}
