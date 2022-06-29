package com.gemiso.zodiac.app.yonhapWire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YonhapWireSimpleDTO {

    private Long wireId;
    /*private String contId;
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
    private List<YonhapWireAttchFileDTO> yonhapWireAttchFiles = new ArrayList<>();*/
}
