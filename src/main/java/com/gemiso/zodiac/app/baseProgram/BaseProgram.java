package com.gemiso.zodiac.app.baseProgram;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tb_brdc_pgm_baspgmsch")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@DynamicUpdate
public class BaseProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bas_pgmsch_id",nullable = false )
    private Long basPgmschId;

    @Column(name = "bas_dt", length = 8, nullable = false)
    private String basDt;

    @Column(name = "ch_div_cd", length = 50, nullable = false)
    private String chDivCd;

    @Column(name = "brdc_day", length = 8, nullable = false)
    private String brdcDay;

    @Column(name = "brdc_div_cd", length = 50)
    private String brdcDivCd;

    @Column(name = "brdc_start_clk", length = 50)
    private String brdcStartClk;


}
