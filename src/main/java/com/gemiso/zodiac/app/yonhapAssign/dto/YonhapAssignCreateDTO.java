package com.gemiso.zodiac.app.yonhapAssign.dto;

import com.gemiso.zodiac.app.yonhap.Yonhap;
import com.gemiso.zodiac.app.yonhapWire.YonhapWire;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YonhapAssignCreateDTO {

    //private Long assignId;
    private Yonhap yonhap;
    private YonhapWire yonhapWire;
    private String designatorId;
    //private String designatorNm;
    private String assignerId;
    //private String assignerNm;
    //private Date inputDtm;
}
