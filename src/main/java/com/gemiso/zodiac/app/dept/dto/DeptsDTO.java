package com.gemiso.zodiac.app.dept.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeptsDTO {

    private Long id;
    private String name;
    private String code;
    private String isEnabled;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private String parentCode;
    private Long id2;
}
