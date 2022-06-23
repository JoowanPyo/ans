package com.gemiso.zodiac.app.nod.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodDTO {

    private Long nodId;
    private Long cueId;
    private String brdcDt;
    private String brdcStartTime;
    private String brdcEndTime;
    private String nodTyp;
    private String brdcSt;
    private String fileNm;
    private String transSt;
    private String brdcPgmNm;
    private String brdcPgmId;
    private Date inputDtm;
    private Date updtDtm;
}
