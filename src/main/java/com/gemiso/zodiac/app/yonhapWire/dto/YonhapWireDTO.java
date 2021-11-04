package com.gemiso.zodiac.app.yonhapWire.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YonhapWireDTO {

    private Long wireId;
    private String contId;
    private String imprt;
    private String svcTyp;
    private String artclTitl;
    private String artclCtt;
    private String agcyCd;
    private String agcyNm;
    private String credit;
    private int artclqnty;
    private String source;
    private String cmnt;
    private Date embgDtm;
    private Date trnsfDtm;
    private Date inputDtm;
    private String action;
}
