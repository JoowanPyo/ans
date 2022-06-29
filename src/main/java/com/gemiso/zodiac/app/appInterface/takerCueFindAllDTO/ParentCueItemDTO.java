package com.gemiso.zodiac.app.appInterface.takerCueFindAllDTO;

import com.gemiso.zodiac.app.cueSheetItemSymbol.dto.CueSheetItemSymbolDTO;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "record")
@XmlAccessorType(XmlAccessType.FIELD)
public class ParentCueItemDTO {

    @XmlElement(name = "rd_id")
    private Long cueItemId;
    @XmlElement(name = "rd_seq")
    private Integer rdSeq; // 수정. 몬지모름
    private String cueItemTitl;
    private String cueItemCtt;
    private int cueItemOrd;
    private String cueItemOrdCd;
    private int cueItemTime;
    private String cueItemFrmCd;
    private String cueItemDivCd;
    private String brdcStCd;
    private String brdcClk;
    private String chrgrId;
    private String chrgrNm;
    private String artclCapStCd;
    private String cueArtclCapChgYn;
    private Date cueArtclCapChgDtm;
    private String cueArtclCapStCd;
    private String rmk;
    private String lckYn;
    private String delYn;
    private Date delDtm;
    private Date lckDtm;
    private Date inputDtm;
    private Date updtDtm;
    private String cueItemTypCd;
    private Date cueItemBrdcDtm;
    private String capChgYn;
    private Date capChgDtm;
    private String capStCd;
    private String artclStCd;
    private String mediaDurtn;
    private String newsBreakYn;
    private UserSimpleDTO inputr;
    private UserSimpleDTO updtr;
    private UserSimpleDTO delr;
    private UserSimpleDTO lckr;
    private Long cueId;
    private Long article;
    private List<CueSheetItemSymbolDTO> cueSheetItemSymbolDTO = new ArrayList<>();
}
