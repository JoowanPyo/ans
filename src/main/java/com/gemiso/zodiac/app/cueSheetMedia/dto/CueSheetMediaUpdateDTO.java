package com.gemiso.zodiac.app.cueSheetMedia.dto;

import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSimpleDTO;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetMediaUpdateDTO {

    private Long cueMediaId;
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
    //private Date inputDtm;
    private Date updtDtm;
    private String videoEdtrId;
    //private UserSimpleDTO inputr;
    private UserSimpleDTO updtr;
    //private UserSimpleDTO delr;
    private CueSheetItemSimpleDTO cueSheetItem;
}
