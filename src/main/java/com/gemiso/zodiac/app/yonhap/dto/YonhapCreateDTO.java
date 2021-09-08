package com.gemiso.zodiac.app.yonhap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YonhapCreateDTO {

    private String yh_artcl_id;
    private String cont_id;
    private String imprt;
    private String svc_typ;
    private String artcl_titl;
    private String artcl_smltitl;
    private String artcl_ctt;
    private String credit;
    private String source;
    private String artcl_cate_cd;
    private String region_cd;
    private String region_nm;
    private String ctt_class_cd;
    private String ctt_class_add_cd;
    private String issu_cd;
    private String stock_cd;
    private String artclqnty;
    private String cmnt;
    private String rel_cont_id;
    private String ref_cont_info;
    private String embg_dtm;
    private String input_dtm;
    private String trnsf_dtm;
    private String action;

    private List<YonhapAttachFileCreateDTO> upload_files;
}
