package com.gemiso.zodiac.app.cueSheetMedia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetMediaCreateDTO {

    //private Long cueMediaId;
    private Long cueItemId;
    private String mediaTypCd;
    private int mediaOrd;
    private int contId;
    private String trnsfFileNm;
    private String mediaDurtn;
    private Date mediaMtchDtm;
    private String trnsfStCd;
    private String assnStCd;
    private String videoEdtrNm;
    private String delYn;
    //private Date delDtm;
    private Date inputDtm;
    //private Date updtDtm;
    private String videoEdtrId;
    private String inputrId;
    //private String updtrId;
    //private String delrId;
}
