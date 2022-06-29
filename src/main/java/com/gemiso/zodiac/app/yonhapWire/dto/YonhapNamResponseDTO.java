package com.gemiso.zodiac.app.yonhapWire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YonhapNamResponseDTO {

    private Integer status;
    private Boolean success;
    private NamResponseDTO data;
}
