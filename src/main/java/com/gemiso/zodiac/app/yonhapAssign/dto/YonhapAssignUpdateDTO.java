package com.gemiso.zodiac.app.yonhapAssign.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
