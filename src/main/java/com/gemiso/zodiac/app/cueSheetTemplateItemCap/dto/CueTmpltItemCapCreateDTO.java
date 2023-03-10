package com.gemiso.zodiac.app.cueSheetTemplateItemCap.dto;

import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmpltItemSimpleDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolSimpleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CueTmpltItemCapCreateDTO {

    //private Long cueItemCapId;
    private String cueItemCapDivCd;
    private String capCtt;
    private int capOrd;
    private int lnNo;
    private String capPrvwId;
    private String capClassCd;
    private String capPrvwUrl;
    private String colorInfo;
    private String capRmk;
    private String inputrId;
    @Schema(description = "자막 템플릿 아이디")
    private Long capTmpltId;
    @NotNull
    private CueTmpltItemSimpleDTO cueTmpltItem;
    private SymbolSimpleDTO symbol;
}
