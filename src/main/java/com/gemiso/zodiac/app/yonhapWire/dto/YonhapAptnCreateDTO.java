package com.gemiso.zodiac.app.yonhapWire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YonhapAptnCreateDTO {

    private String cont_id;
    private String artcl_titl;
    private String artcl_ctt;
    private String mam_cont_id;

    /*private List<AttachFileDTO> files = new ArrayList<AttachFileDTO>();
    private List<YonhapAttachFileCreateDTO> upload_files;*/
}
