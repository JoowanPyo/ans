package com.gemiso.zodiac.core.mis;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "VI_DEPTXM")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class MisDept {

    @Id
    @Column(name = "DEPT_CODE", length = 10, nullable = false)
    private String deptCode;

    @Column(name = "ORGC_DATE", length = 8, nullable = false)
    private String drgcDate;

    @Column(name = "DEPT_NAME", length = 100)
    private String deptName;

    @Column(name = "ORGN_CODE")
    private String orgnCode;

    @Column(name = "ENGL_DTNM", length = 100)
    private String englDtnm;

    @Column(name = "DEPT_THNM", length = 400)
    private String deptThnm;

    @Column(name = "UPPR_DTCD", length = 10)
    private String upprDtcd;

    @Column(name = "UPPR_NAME", length = 4000)
    private String upprName;

    @Column(name = "DEPT_LEVL", length = 5)
    private String deptLevl;

    @Column(name = "SORT_ORDR")
    private Long sortOrdr;

    @Column(name = "REGR_DATE", length = 8)
    private String regrDate;

    @Column(name = "WAST_DATE", length = 8)
    private String wastDate;

    @Column(name = "USEX_YSNO", columnDefinition = "bpchar(1) default 'N'")
    private String usexYsno;

    @Column(name = "CPFX_YSNO", length = 1)
    private String cpfxYsno;

    @Column(name = "REDT_YSNO", length = 1)
    private String redtYsno;

    @Column(name = "STDS_DATE", length = 8, nullable = false)
    private String stdsDate;

    @Column(name = "DEPT_FCOD", length = 100)
    private String deptFcod;

    @Column(name = "DTGN_CODE", length = 10)
    private String dtgnCode;

    @Column(name = "DTGN_NAME", length = 200)
    private String dtgnName;

    @Column(name = "ENFC_CODE", length = 10)
    private String enfcCode;

    @Column(name = "ENFC_NAME", length = 200)
    private String enfcName;

    @Column(name = "SECT_CODE", length = 10)
    private String sectCode;

    @Column(name = "SECT_NAME", length = 4000)
    private String sectName;

    @Column(name = "DEPT_SORT", length = 100)
    private String deptSort;

}
