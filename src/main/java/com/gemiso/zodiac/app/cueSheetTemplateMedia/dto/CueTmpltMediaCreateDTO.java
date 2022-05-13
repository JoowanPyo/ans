package com.gemiso.zodiac.app.cueSheetTemplateMedia.dto;

import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmpltItemSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueTmpltMediaCreateDTO {

    //private Long cueTmpltMediaId;
    private String mediaTypCd;
    //private String mediaTypCdNm;
    private Integer mediaOrd;
    private Integer contId;
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
    private String inputrId;
    //private String inputrNm;
    //private String updtrId;
    //private String updtrNm;
    //private String delrId;
    //private String delrNm;
    private String videoEdtrId;
    private String cueTmpltMediaTitl;
    private String videoId;
    private Integer trnasfVal;
    private CueTmpltItemSimpleDTO cueTmpltItem;
}
