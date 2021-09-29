package com.gemiso.zodiac.app.cueSheetMedia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetMediaRequestDTO {

    private String cueId;
    private String cueItemTitl;
    private Long cueItemId;
    private String mediaGrpId;
    private int mediaGrpOrd;
    private String mediaGrpTitl;
    private String mediaGrpTypCd;
    private String mediaTypNm;
    private List<CueSheetMediaCreateDTO> cueSheetMediaCreateDTO;
}
