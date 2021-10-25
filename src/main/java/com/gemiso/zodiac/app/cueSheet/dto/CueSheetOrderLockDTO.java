package com.gemiso.zodiac.app.cueSheet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.program.dto.ProgramDTO;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CueSheetOrderLockDTO {

    private Long cueId;
    /*private String cueDivCd;
    private String chDivCd;
    private String brdcDt;
    private String brdcStartTime;
    private String brdcEndTime;
    private String brdcSchTime;
    private String brdcPgmNm;
    private String cueStCd;
    private String stdioId;
    private String subrmId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")*/
    private Date lckDtm;
    private String lckYn;
    /*@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date delDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date inputDtm;
    private String delYn;
    private UserSimpleDTO inputr;
    private UserSimpleDTO delr;
    private UserSimpleDTO pd1;
    private UserSimpleDTO pd2;
    private UserSimpleDTO anc1;
    private UserSimpleDTO anc2;*/
    private String lckrId;
    /*private UserSimpleDTO td1;
    private UserSimpleDTO td2;
    private String remark;
    private ProgramDTO program;*/
}
