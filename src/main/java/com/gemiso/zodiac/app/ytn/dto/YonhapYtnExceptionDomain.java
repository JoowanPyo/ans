package com.gemiso.zodiac.app.ytn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YonhapYtnExceptionDomain {

    //private Long id;
    private String cont_id;
    private String code;
    private String resource;
    private String message;
    private String data;
}
