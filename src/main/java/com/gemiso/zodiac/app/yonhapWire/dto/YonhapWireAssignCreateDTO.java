package com.gemiso.zodiac.app.yonhapWire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YonhapWireAssignCreateDTO {

    private YonhapWireSimpleDTO yonhapWire;
    private String designatorId;
    //private String designatorNm;
    private String assignerId;
}
