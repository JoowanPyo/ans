package com.gemiso.zodiac.app.yonhap.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.yonhapAttchFile.dto.YonhapAttachFileSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YonhapDTO {

    private Long yonhapId;
    private String contId;
    private String imprt;
    private String svcTyp;
    private String artclTitl;
    private String artclSmltitl;
    private String artclCtt;
    private String credit;
    private String source;
    private String artclCateCd;
    private String artclCateNm;
    private String regionCd;
    private String regionNm;
    private String cttClassCd;
    private String cttClassNm;
    private String cttClassAddCd;
    private String issuCd;
    private String stockCd;
    private Integer artclqnty;
    private String cmnt;
    private String relContId;
    private String refContInfo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date embgDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date inputDtm;
    private Date trnsfDtm;
    private String action;

    //private List<AttachFileDTO> files = new ArrayList<AttachFileDTO>();
    private List<YonhapAttachFileSimpleDTO> yonhapAttchFiles;

}
