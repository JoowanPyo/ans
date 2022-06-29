package com.gemiso.zodiac.app.file.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachFileDTO {

    private Long fileId;
    private String fileDivCd;
    private String fileNm;
    private String fileExpl;
    private String fileLoc;
    private Integer fileSize;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date fileUpldDtm;
    private String delYn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date inputDtm;
    private String orgFileNm;
    private String inputrId;
    private String inputrNm;
}
