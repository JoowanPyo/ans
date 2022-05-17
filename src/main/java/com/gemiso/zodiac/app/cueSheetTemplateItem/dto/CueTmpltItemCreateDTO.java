package com.gemiso.zodiac.app.cueSheetTemplateItem.dto;

import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.dto.CueTmpltItemCapCreateDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.dto.CueTmpltItemCapDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueTmpltItemCreateDTO {

    //private Long cueTmpltItemId;
    private String cueItemTitl;
    private String cueItemTitlEn;
    private String cueItemCtt;
    private Integer cueItemOrd;
    private String cueItemOrdCd;
    private Integer cueItemTime;
    private String cueItemFrmCd;
    private String cueItemDivCd;
    private String mediaChCd;
    private String mediaDurtn;
    private String inputrId;
    private CueSheetTemplateSimpleDTO cueSheetTemplate;
    private List<CueTmpltItemCapCreateDTO> cueTmpltItemCap;
}
