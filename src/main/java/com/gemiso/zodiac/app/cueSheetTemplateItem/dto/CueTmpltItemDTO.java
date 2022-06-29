package com.gemiso.zodiac.app.cueSheetTemplateItem.dto;

import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.dto.CueTmpltItemCapSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplateMedia.CueTmpltMedia;
import com.gemiso.zodiac.app.cueSheetTemplateSymbol.dto.CueTmplSymbolDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CueTmpltItemDTO {

    private Long cueTmpltItemId;
    private String cueItemTitl;
    private String cueItemTitlEn;
    private String cueItemCtt;
    private int cueItemOrd;
    private String cueItemOrdCd;
    private int cueItemTime;
    private String cueItemFrmCd;
    private String cueItemFrmCdNm;
    private String cueItemDivCd;
    private String cueItemDivCdNm;
    private String lckYn;
    private String delYn;
    private Date inputDtm;
    private Date updtDtm;
    private Date delDtm;
    private Date lckDtm;
    private String mediaChCd;
    private String mediaDurtn;
    private String inputrId;
    private String inputrNm;
    private String updtrId;
    private String updtrNm;
    private String delrId;
    private String delrNm;
    private String lckrId;
    private String lckrNm;
    private Integer artclExtTime;
    private CueSheetTemplateSimpleDTO cueSheetTemplate;
    private List<CueTmpltItemCapSimpleDTO> cueTmpltItemCap;
    private List<CueTmplSymbolDTO> cueTmplSymbol = new ArrayList<>();
    private List<CueTmpltMedia> cueTmpltMedia = new ArrayList<>();
}
