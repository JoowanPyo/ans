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
public class YonhapCreateDTO {

    private String cont_id;
    private String imprt;
    private String svc_typ;
    private String artcl_titl;
    private String artcl_ctt;
    private String agcy_cd;
    private String agcy_nm;
    private String credit;
    private String artclqnty;
    private String source;
    private String cmnt;
    private String embgDtm;
    private String trnsf_dtm;
    private String input_dtm;
    private String action;
    private Long mamContId;

}
