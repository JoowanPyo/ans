package com.gemiso.zodiac.app.yonhap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YonhapAssignCreateDTO {

    private YonhapSimpleDTO yonhap;
    private String designatorId;
    //private String designatorNm;
    private String assignerId;
}
