package com.gemiso.zodiac.app.nod;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tb_nod"
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Nod extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nod_id", nullable = false)
    private Long nodId;

    @Column(name = "cue_id", nullable = false)
    private Long cueId;

    @Column(name = "brdc_dt", length = 10, nullable = false)
    private String brdcDt;

    @Column(name = "brdc_start_time", length = 8, nullable = false)
    private String brdcStartTime;

    @Column(name = "brdc_end_time", length = 8, nullable = false)
    private String brdcEndTime;

    @Column(name = "nod_typ", length = 50, nullable = false)
    private String nodTyp;

    @Column(name = "brdc_st", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String brdcSt;

    @Column(name = "file_nm", length = 255, nullable = false)
    private String fileNm;

    @Column(name = "trans_st", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String transSt;

    @Column(name = "brdc_pgm_nm", length = 450)
    private String brdcPgmNm;

    @Column(name = "brdc_pgm_id", length = 50, nullable = false)
    private String brdcPgmId;


    @Column(name = "subrm_id", length = 1, nullable = false)
    private String subrmId;
}
