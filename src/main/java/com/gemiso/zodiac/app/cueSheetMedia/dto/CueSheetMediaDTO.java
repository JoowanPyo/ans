package com.gemiso.zodiac.app.cueSheetMedia.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import com.gemiso.zodiac.app.code.dto.CodeSimpleDTO;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemDTO;
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
public class CueSheetMediaDTO {

    private Long cueMediaId;
    private Long cueItemId;
    private String mediaTypCd;
    private String mediaTypCdNm;
    private int mediaOrd;
    private int contId;
    private String trnsfFileNm;
    private String mediaDurtn;
    private Date mediaMtchDtm;
    private String trnsfStCd;
    private String trnsfStCdNm;
    private String assnStCd;
    private String assnStCdNm;
    private String videoEdtrNm;
    private String delYn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date delDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date inputDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updtDtm;
    private String videoEdtrId;
    private String inputrId;
    private String updtrId;
    private String delrId;
    private String inputrNm;
    private String updtrNm;
    private String delrNm;
    private String cueMediaTitl;
    private CueSheetItemSimpleDTO cueSheetItem;

}
