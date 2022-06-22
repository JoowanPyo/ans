package com.gemiso.zodiac.app.yonhapAssign.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.yonhap.Yonhap;
import com.gemiso.zodiac.app.yonhap.dto.YonhapSimpleDTO;
import com.gemiso.zodiac.app.yonhapWire.YonhapWire;
import com.gemiso.zodiac.app.yonhapWire.dto.YonhapWireSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YonhapAssignDTO {

    private Long assignId;
    private YonhapSimpleDTO yonhap;
    private YonhapWireSimpleDTO yonhapWire;
    private String designatorId;
    private String designatorNm;
    private String assignerId;
    private String assignerNm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date inputDtm;
}
