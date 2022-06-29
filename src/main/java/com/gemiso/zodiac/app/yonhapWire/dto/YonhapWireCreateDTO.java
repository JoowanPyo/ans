package com.gemiso.zodiac.app.yonhapWire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YonhapWireCreateDTO {

    private Long wire_id;
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
    private String embg_dtm;
    private String trnsf_dtm;
    private String input_dtm;
    private String action;
    private Long mam_cont_id;
    private String media_no;
}
