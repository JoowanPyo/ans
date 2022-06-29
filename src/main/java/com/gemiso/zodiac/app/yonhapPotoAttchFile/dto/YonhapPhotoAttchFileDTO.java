package com.gemiso.zodiac.app.yonhapPotoAttchFile.dto;

import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.yonhapPhoto.dto.YonhapPhotoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YonhapPhotoAttchFileDTO {

    private Long id;
    private int fileOrd;
    private String fileTypCd;
    private String mimeTyp;
    private String yonhapUrl;
    private String expl;
    private AttachFileDTO attachFile;
    private YonhapPhotoDTO yonhapPhoto;
}
