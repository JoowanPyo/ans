package com.gemiso.zodiac.app.yonhapPhoto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YonhapExceptionDomain {

    private Long id;
    private String code;
    private String resource;
    private String message;
    private String data;

}
