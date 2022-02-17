package com.gemiso.zodiac.app.yonhapWire.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YonhapReuterCreateDTO {

    private String action;
    private String agcy_cd;
    private int brdc_cnt;
    private int grphc_count;
    private String video_nm;

    private String wire_artcl_id;
    private String wire_artcl_titl;
    private String wire_artcl_ctt;

    private int artcl_brdc_count;

    private String artcl_lcal_dtm;
    private String artcl_stnd_dtm;

    private String imprt;
}
