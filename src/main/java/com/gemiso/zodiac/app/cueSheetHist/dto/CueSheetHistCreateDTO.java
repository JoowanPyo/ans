package com.gemiso.zodiac.app.cueSheetHist.dto;

import com.gemiso.zodiac.app.cueSheet.dto.CueSheetSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetHistCreateDTO {

    //private Long cueHistId;
    private int cueVer;
    private String cueAction;
    private String cueItemData;
    private Date inputDtm;
    private String inputrId;
    //private String inputrNm;
    private CueSheetSimpleDTO cueSheet;
}
