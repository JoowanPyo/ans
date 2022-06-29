package com.gemiso.zodiac.app.cueSheetItemCap.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSimpleDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetItemCapDTO {

    private Long cueItemCapId;
    private String cueItemCapDivCd;
    private String cueItemCapDivCdNm;
    private String capCtt;
    private int capOrd;
    private int lnNo;
    private String capPrvwId;
    private String capClassCd;
    private String capClassCdNm;
    private String capPrvwUrl;
    private String colorInfo;
    private String capRmk;
    private String orgCueItemCapId;
    private String delYn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date delDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date inputDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updtDtm;
    private String inputrId;
    private String updtrId;
    private String delrId;
    private String inputrNm;
    private String updtrNm;
    private String delrNm;
    private CapTemplateDTO capTemplate;
    private CueSheetItemSimpleDTO cueSheetItem;
    private SymbolDTO symbol;
}
