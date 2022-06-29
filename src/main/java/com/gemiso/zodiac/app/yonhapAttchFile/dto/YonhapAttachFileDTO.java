package com.gemiso.zodiac.app.yonhapAttchFile.dto;

import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.yonhap.dto.YonhapDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YonhapAttachFileDTO {

    private Long id;
    private AttachFileDTO attachFile;
    private YonhapDTO yonhap;
    private int fileOrd;
    private String fileTitl;
    private String mimeType;
    private String cap;
    private String yhUrl;

}
