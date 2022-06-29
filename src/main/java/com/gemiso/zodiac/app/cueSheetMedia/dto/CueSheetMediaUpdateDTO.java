package com.gemiso.zodiac.app.cueSheetMedia.dto;

import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetMediaUpdateDTO {

    private Long cueMediaId;
    private Long cueItemId;
    private String mediaTypCd;
    //private String mediaTypCdNm;
    private int mediaOrd;
    private int contId;
    private String trnsfFileNm;
    private Integer mediaDurtn;
    private Date mediaMtchDtm;
    private String trnsfStCd;
    //private String trnsfStCdNm;
    private String assnStCd;
    //private String assnStCdNm;
    private String videoEdtrNm;
    //private String delYn;
    //private Date delDtm;
    //private Date inputDtm;
    private Date updtDtm;
    private String videoEdtrId;
    //private String inputrId;
    private String updtrId;
    //private String delrId;
    //private String inputrNm;
    //private String updtrNm;
    //private String delrNm;
    private String cueMediaTitl;
    private String videoId;
    private Integer trnasfVal;
    private CueSheetItemSimpleDTO cueSheetItem;
}
