package com.gemiso.zodiac.app.dept.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeptsDTO {

    private Long id;
    private String name;
    private String code;
    private String isEnabled;
    private Date inputDtm;
    private Date updtDtm;
    private Date delDtm;
    private String parentCode;
    private Long id2;

}
