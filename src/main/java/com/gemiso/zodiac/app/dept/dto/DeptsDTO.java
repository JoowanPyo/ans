package com.gemiso.zodiac.app.dept.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
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
    private Date inputDtm;
    private Date updtDtm;
    private Date delDtm;
    private String parentCode;
    private Long id2;

}
