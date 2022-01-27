package com.gemiso.zodiac.app.cueSheetMedia.dto;

import com.gemiso.zodiac.app.code.dto.CodeSimpleDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSimpleDTO;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
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
    private String videoEdtrId;
    private String inputrId;
    private String cueMediaTitl;
    @NotNull
    private CueSheetItemSimpleDTO cueSheetItem;
}
