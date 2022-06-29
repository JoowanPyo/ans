package com.gemiso.zodiac.app.capTemplateGrp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CapTemplateGrpDTO {

    private Long tmpltGrpId;
    private String chDivCd;
    private String chDivCdNm;
    private String tmpltGrpNm;
    /*private String brdcPgmId;*/
    private String inputrId;
    private String inputrNm;
    private String updtrId;
    private String updtrNm;
    private String delYn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date inputDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updtDtm;
}
