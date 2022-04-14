package com.gemiso.zodiac.core.mis;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MisDeptId implements Serializable {

    @Column(name = "ORGC_DATE", length = 8, nullable = false)
    private String drgcDate;

    @Column(name = "DEPT_CODE", length = 10, nullable = false)
    private String deptCode;

    @Column(name = "DEPT_NAME", length = 100)
    private String deptName;

}
