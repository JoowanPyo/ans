package com.gemiso.zodiac.core.mis;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "VI_TOTAL")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class MisUser {

    @Id
    @Column(name = "USER_IDXX", length = 40)
    private String userIdxx;

    @Column(name = "USDN_CODE", length = 2)
    private String usdnCode;

    @Column(name = "EMPL_NUMB", length = 10)
    private String emplNumb;

    @Column(name = "EMPL_NAME", length = 90)
    private String emplName;

    @Column(name = "DEPT_CODE", length = 10)
    private String deptCode;

    @Column(name = "OPOS_CODE", length = 6)
    private String opos_code;

    @Column(name = "CPOS_CODE", length = 6)
    private String cposCode;

    @Column(name = "DURS_CODE", length = 6)
    private String dursCode;

    @Column(name = "ELTR_MLAD", length = 500)
    private String eltrMlad;

    @Column(name = "WODN_CODE", length = 6)
    private String wodnCode;

    @Column(name = "USEX_CUST", length = 100)
    private String usexCust;

    @Column(name = "MISX_YSNO", length = 1)
    private String misxYsno;

    @Column(name = "BISX_YSNO", length = 1)
    private String bisxYsno;

    @Column(name = "ANSX_YSNO", length = 1)
    private String ansxYsno;

    @Column(name = "PMAM_YSNO", length = 1)
    private String pmamYsno;

    @Column(name = "BMAM_YSNO", length = 1)
    private String bmamYsno;

    @Column(name = "AMAM_YSNO", length = 1)
    private String amamYsno;

    @Column(name = "SMAM_YSNO", length = 1)
    private String smamYsno;

    @Column(name = "GWXX_YSNO", length = 1)
    private String gwxxYsno;

    @Column(name = "MAIL_YSNO", length = 1)
    private String mailYsno;

    @Column(name = "BIXX_YSNO", length = 1)
    private String bixxYsno;

    @Column(name = "APPL_PROW", length = 1000)
    private String applProw;

    @Column(name = "SCRT_NUMB", length = 150)
    private String scrtNumb;

    @Column(name = "SCRT_FNAL", length = 8)
    private String scrtFnal;

    @Column(name = "BGIN_DATE", length = 8)
    private String bginDate;

    @Column(name = "ENDX_DATE", length = 8)
    private String endxDate;

    @Column(name = "BEFO_SCRT", length = 150)
    private String befoScrt;

    //@Column(name = "BMAM_YSNO")


}
