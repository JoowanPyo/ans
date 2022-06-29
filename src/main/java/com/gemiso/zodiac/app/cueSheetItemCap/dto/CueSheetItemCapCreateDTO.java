package com.gemiso.zodiac.app.cueSheetItemCap.dto;

import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSimpleDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetItemCapCreateDTO {

    //private Long cueItemCapId;
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
    private Long capTmpltId;
    private String inputrId;
    private CapTemplateDTO capTemplate;
    @NotNull
    private CueSheetItemSimpleDTO cueSheetItem;
    private SymbolSimpleDTO symbol;
}
