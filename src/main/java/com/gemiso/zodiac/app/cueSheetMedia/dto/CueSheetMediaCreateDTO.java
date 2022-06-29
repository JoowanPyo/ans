package com.gemiso.zodiac.app.cueSheetMedia.dto;

import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetMediaCreateDTO {

    //private Long cueMediaId;
    private Long cueItemId;
    private String mediaTypCd;
    private Integer mediaOrd;
    private Integer contId;
    private String trnsfFileNm;
    private Integer mediaDurtn;
    private Date mediaMtchDtm;
    private String trnsfStCd;
    private String assnStCd;
    private String videoEdtrNm;
    private String videoEdtrId;
    private String inputrId;
    private String cueMediaTitl;
    private String videoId;
    private Integer trnasfVal;
    private CueSheetItemSimpleDTO cueSheetItem;
}
