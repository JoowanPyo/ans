package com.gemiso.zodiac.app.yonhapWire.dto;

import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.yonhapAttchFile.dto.YonhapAttachFileCreateDTO;
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
public class YonhapAptnCreateDTO {

    private String cont_id;
    private String artcl_titl;
    private String artcl_ctt;

    private List<AttachFileDTO> files = new ArrayList<AttachFileDTO>();
    private List<YonhapAttachFileCreateDTO> upload_files;
}
