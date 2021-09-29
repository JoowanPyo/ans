package com.gemiso.zodiac.app.cueSheetItemCap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetItemCapDTO {

    private Long cueItemCapId;
    private Long cueItemId;
    private String cueItemCapDivCd;
    private String capCtt;
    private int capOrd;
    private int lnNo;
    private String capPrvwId;
    private String capClassCd;
    private String capPrvwUrl;
    private String colorInfo;
    private String capRmk;
    private String orgCueItemCapId;
    private String delYn;
    private Date delDtm;
    private Date inputDtm;
    private Date updtDtm;
    private Long capTmpltId;
    private String inputrId;
    private String updtrId;
    private String delrId;
}
