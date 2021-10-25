package com.gemiso.zodiac.app.file.dto;

import com.gemiso.zodiac.app.code.dto.CodeSimpleDTO;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachFileDTO {

    private Long fileId;
    private String fileDivCd;
    private String fileNm;
    private String fileExpl;
    private String fileLoc;
    private Integer fileSize;
    private Date fileUpldDtm;
    private String delYn;
    private Date inputDtm;
    private String orgFileNm;
    private String inputrId;
    private String inputrNm;
}
