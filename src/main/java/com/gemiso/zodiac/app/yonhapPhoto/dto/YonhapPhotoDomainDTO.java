package com.gemiso.zodiac.app.yonhapPhoto.dto;

import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.yonhapAttchFile.dto.YonhapAttachFileDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YonhapPhotoDomainDTO {

    //"연합기사아이디"
    private Long yh_artcl_id;
    //"콘텐트아이디"
    private String cont_id;
    //"콘텐츠 처리 유형"
    private String action;
    //"중요도"
    private String imprt;
    //"서비스유형"
    private String svc_typ;
    //"제목"
    private String artcl_titl;
    //"소제목"
    private String artcl_smltitl;
    //"기사내용"
    private String artcl_ctt;
    //"기사분야코드"
    private String artcl_cate_cd;
    //"기사분야명"
    private String artcl_cate_nm;
    //"지역코드"
    private String region_cd;
    //"지역명"
    private String region_nm;
    //"내용분류코드"
    private String ctt_class_cd;
    //"내용분류명"
    private String ctt_class_nm;
    //"내용분류추가코드"
    private String ctt_class_add_cd;
    //"내용분류추가명"
    private String ctt_class_add_nm;
    //"연합포토구분코드"
    private String yh_photo_div_cd;
    //"연합포토구분명"
    private String yh_photo_div_nm;
    //"연합발행번호"
    private String yh_publ_no;
    //"출처"
    private String source;
    //"저작권소유기관"
    private String credit;
    //"입력일시"
    private String input_dtm;
    //"기사전송일시"
    private String trnsf_dtm;
    //"포토URL"
    private String photo_url;
    //"토탈카운트"
    private String tot_cnt;
    //"카테고리 아이디"
    private String ctgr_id;
    //"카테고리 소분류 아이디"
    private String ctgr_class_id;
    //"요청자아이디"
    private String req_user_id;
    private String req_titl;
    private String media_typ;
    private String embg_dtm;
    private String cmnt;

    private Long mamContId;

    //"파일"
    private List<AttachFileDTO> files = new ArrayList<AttachFileDTO>();
    private List<YonhapAttachFileDTO> upload_files;
}
