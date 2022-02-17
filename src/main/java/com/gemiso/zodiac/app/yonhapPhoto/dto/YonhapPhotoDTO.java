package com.gemiso.zodiac.app.yonhapPhoto.dto;

import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.yonhapAttchFile.dto.YonhapAttachFileCreateDTO;
import com.gemiso.zodiac.app.yonhapPotoAttchFile.YonhapPhotoAttchFile;
import com.gemiso.zodiac.app.yonhapPotoAttchFile.dto.YonhapPhotoAttchFileDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YonhapPhotoDTO {

    private Long yonhapArtclId;
    private String contId;
    private String imprt;
    private String svcTyp;
    private String artclTitl;
    private String artclSmltitl;
    private String artclCtt;
    private String artclCateCd;
    private String artclCateNm;
    private String regionCd;
    private String regionNm;
    private String cttClassCd;
    private String cttClassNm;
    private String cttClassAddCd;
    private String cttClassAddNm;
    private String yonhapPhotoDivCd;
    private String yonhapPhotoDivNm;
    private String yonhapPublNo;
    private String src;
    private String credit;
    private String cmnt;
    private Date inputDtm;
    private Date trnsfDtm;
    private Date embgDtm;
    private String action;

    private List<AttachFileDTO> files = new ArrayList<AttachFileDTO>();
    private List<YonhapPhotoAttchFileDTO> yonhapPhotoAttchFiles = new ArrayList<>();
}
