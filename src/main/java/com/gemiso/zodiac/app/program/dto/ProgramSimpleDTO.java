package com.gemiso.zodiac.app.program.dto;

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
public class ProgramSimpleDTO {

    private String brdcPgmId;
    private String brdcPgmNm;
    private String brdcPgmNmEn; //영문명
    private String chDivCd;
    private String chDivCdNm;
    private String brdcPgmDivCd;
    private String brdcPgmDivCdNm;
    private String gneDivCd;
    private String gneDivCdNm;
    private String prdDivCd;
    private String prdDivCdNm;
    private String brdcStartTime;
    private String schTime;
    private String inputDtm;
    private String updtDtm;
    private String delYn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date delDtm;
    private String inputrId;
    private String inputrNm;
    private String updtrId;
    private String updtrNm;
    private String delrId;
    private String delrNm;
    private String scriptYn;
    private String nodYn;
    private String useYn;
}
