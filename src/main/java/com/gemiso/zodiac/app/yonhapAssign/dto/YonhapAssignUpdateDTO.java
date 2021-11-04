package com.gemiso.zodiac.app.yonhapAssign.dto;

import com.gemiso.zodiac.app.yonhap.Yonhap;
import com.gemiso.zodiac.app.yonhapWire.YonhapWire;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YonhapAssignUpdateDTO {

    private Long assignId;
    //private Yonhap yonhap;
    //private YonhapWire yonhapWire;
    //private String designatorId;
    //private String designatorNm;
    private String assignerId; //담당자 지정만 바뀔꺼 같다?.
    //private String assignerNm;
    //private Date inputDtm;
}
