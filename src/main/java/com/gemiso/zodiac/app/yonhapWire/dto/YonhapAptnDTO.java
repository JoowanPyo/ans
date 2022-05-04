package com.gemiso.zodiac.app.yonhapWire.dto;

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
public class YonhapAptnDTO {

    private Long yh_artcl_id;
    private String cont_id;
    private String action;
    private String imprt;
    private String svc_typ;
    private String artcl_titl;
    private String artcl_ctt;
    private int artclqnty;
    private String agcy_cd;
    private String agcy_nm;
    private String source;
    private String credit;
    private String input_dtm;
    private String trnsf_dtm;
    private Long mamContId;

    private List<AttachFileDTO> files = new ArrayList<AttachFileDTO>();
    private List<YonhapAttachFileDTO> yonhapAttchFiles;
}
