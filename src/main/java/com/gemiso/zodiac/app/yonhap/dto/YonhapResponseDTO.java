package com.gemiso.zodiac.app.yonhap.dto;

import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.yonhapAttchFile.dto.YonhapAttachFileDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YonhapResponseDTO {

    private Long yh_artcl_id;
    private String cont_id;
    private String imprt;
    private String svc_typ;
    private String artcl_titl;
    private String artcl_smltitl;
    private String artcl_ctt;
    private String credit;
    private String source;
    private String artcl_cate_cd;
    private String artcl_cate_nm; //코드네임
    private String region_cd;
    private String region_nm;
    private String ctt_class_cd;
    private String ctt_class_nm;
    private String ctt_class_add_cd;
    private String issu_cd;
    private String stock_cd;
    private Integer artclqnty;
    private String cmnt;
    private String rel_cont_id;
    private String ref_cont_info;
    private String embg_dtm;
    private String input_dtm;
    private String trnsf_dtm;
    private String action;

    private List<AttachFileDTO> files = new ArrayList<AttachFileDTO>();
    private List<YonhapAttachFileDTO> yonhapAttchFiles;
}
