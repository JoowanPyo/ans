package com.gemiso.zodiac.app.cueSheet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetOrderLockResponsDTO {

    private Long cueId;
    private Date lckDtm;
    private String lckYn;
    private String lckrId;

}
