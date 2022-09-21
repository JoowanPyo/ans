package com.gemiso.zodiac.app.cueSheetTemplateItemCap.dto;

import com.gemiso.zodiac.app.symbol.dto.SymbolSimpleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CueTmpltItemCapUpdateDTO {

    private Long cueItemCapId;
    private String cueItemCapDivCd;
    //private String cueItemCapDivCdNm;
    private String capCtt;
    private int capOrd;
    private int lnNo;
    private String capPrvwId;
    private String capClassCd;
    //private String capClassCdNm;
    private String capPrvwUrl;
    private String colorInfo;
    private String capRmk;
    //private String delYn;
    //private String inputrId;
    //private String inputrNm;
    private String updtrId;
    @Schema(description = "자막 템플릿 아이디")
    private Long capTmpltId;
    //private String updtrNm;
    //private String delrId;
    //private String delrNm;
    //private CapTemplateSimpleDTO capTemplate;
    //private CueTmpltItemSimpleDTO cueTmpltItem;
    private SymbolSimpleDTO symbol;
}
