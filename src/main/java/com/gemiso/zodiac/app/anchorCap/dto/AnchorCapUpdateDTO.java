package com.gemiso.zodiac.app.anchorCap.dto;

import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnchorCapUpdateDTO {

    private Long anchorCapId;
    private String capDivCd;
    //private String capDivCdNm;
    private int lnNo;
    private String capCtt;
    private String capRmk;
    //private Long articleId;
    private Long capTemplateId;
    private String symbolId;
}
