package com.gemiso.zodiac.app.yonhap.dto;

import com.gemiso.zodiac.app.file.AttachFile;
import com.gemiso.zodiac.app.yonhap.Yonhap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YonhapAttachFileDTO {

    private Long id;
    private AttachFile attachFile;
    private Yonhap yonhap;
    private int fileOrd;
    private String fileTitl;
    private String mimeType;
    private String cap;
    private String yhUrl;

}
