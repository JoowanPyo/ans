package com.gemiso.zodiac.app.yonhapWire.dto;

import com.gemiso.zodiac.app.yonhap.dto.YonhapSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YonhapWireAssignCreateDTO {

    private YonhapWireSimpleDTO yonhapWire;
    private String designatorId;
    //private String designatorNm;
    private String assignerId;
}
