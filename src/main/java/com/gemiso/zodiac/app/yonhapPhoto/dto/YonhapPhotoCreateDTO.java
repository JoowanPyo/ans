package com.gemiso.zodiac.app.yonhapPhoto.dto;

import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.yonhapAttchFile.dto.YonhapAttachFileCreateDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YonhapPhotoCreateDTO {

    private String id;
    private String code;
    private String resource;
    private String message;
    private String data;

    private HttpStatus httpStatus;

    private String artcl_cate_cd;
    private String artcl_cate_nm;
    private String action;
    private String artcl_ctt;
    private String artcl_smltitl;
    private String artcl_titl;
    private String artclqnty;
    private String cmnt;
    private String cont_id;
    private String credit;
    private String ctt_class_add_cd;
    private String ctt_class_add_nm;
    private String ctt_class_cd;
    private String ctt_class_nm;
    private String embg_dtm;
    private String imprt;
    private String input_dtm;
    private String issu_cd;
    private String issu_nm;
    private String region_cd;
    private String region_nm;
    private String rel_cont_id;
    private String source;
    private String stock_cd;
    private String stock_nm;
    private String svc_typ;
    private String trnsf_dtm;
    private String yh_artcl_id;

    private String artcl_fld_cd;
    private String artcl_fld_nm;
    private String inst_cd;
    private String inst_nm;

    private String 	yh_photo_div_cd;
    private String yh_publ_no;

    private List<AttachFileDTO> files = new ArrayList<AttachFileDTO>();
    private List<YonhapAttachFileCreateDTO> upload_files;
}
