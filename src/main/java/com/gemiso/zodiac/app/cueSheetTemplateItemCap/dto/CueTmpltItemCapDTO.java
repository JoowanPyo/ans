package com.gemiso.zodiac.app.cueSheetTemplateItemCap.dto;

import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmpltItemSimpleDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CueTmpltItemCapDTO {

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
    private String delYn;
    private Date delDtm;
    private String inputrId;
    private String inputrNm;
    private String updtrId;
    private String updtrNm;
    private String delrId;
    private String delrNm;
    private CapTemplateSimpleDTO capTemplate;
    private CueTmpltItemSimpleDTO cueTmpltItem;
    private SymbolDTO symbol;
}
