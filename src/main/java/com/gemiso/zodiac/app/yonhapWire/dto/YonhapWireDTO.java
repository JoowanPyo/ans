package com.gemiso.zodiac.app.yonhapWire.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.yonhapAttchFile.dto.YonhapAttachFileDTO;
import com.gemiso.zodiac.app.yonhapWireAttchFile.YonhapWireAttchFile;
import com.gemiso.zodiac.app.yonhapWireAttchFile.dto.YonhapWireAttchFileDTO;
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
public class YonhapWireDTO {

    private Long wireId;
    private String contId;
    private String imprt;
    private String svcTyp;
    private String artclTitl;
    private String artclCtt;
    private String agcyCd;
    private String agcyNm;
    private String credit;
    private int artclqnty;
    private String source;
    private String cmnt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date embgDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date trnsfDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date inputDtm;
    private String action;

    private List<AttachFileDTO> files = new ArrayList<AttachFileDTO>();
    private List<YonhapWireAttchFileDTO> yonhapWireAttchFiles;
}
