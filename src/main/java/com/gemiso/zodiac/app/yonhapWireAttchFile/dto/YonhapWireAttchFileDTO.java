package com.gemiso.zodiac.app.yonhapWireAttchFile.dto;

import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.yonhapWire.dto.YonhapWireDTO;
import com.gemiso.zodiac.app.yonhapWire.dto.YonhapWireSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YonhapWireAttchFileDTO {

    private  Long id;
    private AttachFileDTO attachFile;
    private YonhapWireSimpleDTO yonhapWire;
    private int fileOrd;
    private String fileTitl;
    private String mimeType;
    private String cap;
    private String yhUrl;
}
