package com.gemiso.zodiac.app.yonhapPoto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YonhapPotoDTO {

    private Long yonhapArtclId;
    private String contId;
    private String imprt;
    private String svcTyp;
    private String artclTitl;
    private String artclSmltitl;
    private String artclCtt;
    private String artclCateCd;
    private String regionCd;
    private String regionNm;
    private String cttClassCd;
    private String cttClassAddCd;
    private String yonhapPhotoDivCd;
    private String yonhapPublNo;
    private String src;
    private String credit;
    private String cmnt;
    private Date inputDtm;
    private Date trnsfDtm;
    private Date embgDtm;
    private String action;
}
