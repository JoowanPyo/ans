package com.gemiso.zodiac.app.yonhapAssign.dto;

import com.gemiso.zodiac.app.yonhap.dto.YonhapSimpleDTO;
import com.gemiso.zodiac.app.yonhapWire.dto.YonhapWireSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YonhapAssignCreateDTO {

    private YonhapSimpleDTO yonhap;
    private YonhapWireSimpleDTO yonhapWire;
    private String designatorId;
    private String assignerId;
}
