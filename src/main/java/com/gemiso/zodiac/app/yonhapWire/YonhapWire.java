package com.gemiso.zodiac.app.yonhapWire;


import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        name = "tb_wire"
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
@DynamicUpdate
public class YonhapWire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "yh_artcl_id", nullable = false)
    private Long yhArtclId;

    @Column(name = "cont_id")
    private String contId;

    @Column(name = "imprt", columnDefinition = "bpchar(1)")
    private String imprt;

    @Column(name = "svc_typ", length = 5)
    private String svcTyp;

    @Column(name = "artcl_titl", length = 500)
    private String artclTitl;

    @Column(name = "artcl_ctt", columnDefinition = "TEXT")
    private String artclCtt;

    @Column(name = "agcy_cd", length = 50)
    private String agcyCd;

    @Column(name = "agcy_nm", length = 50)
    private String agcyNm;

    @Column(name = "credit", length = 100)
    private String credit;

    @Column(name = "artclqnty")
    private int artclqnty;

    @Column(name = "source", length = 100)
    private String source;

    @Column(name = "cmnt", length = 2000)
    private String cmnt;

    @Column(name = "embg_dtm")
    private Date embgDtm;

    @Column(name = "trnsf_dtm")
    private Date trnsfDtm;

    @Column(name = "input_dtm")
    private Date inputDtm;

    @Column(name = "action", columnDefinition = "bpchar(1)")
    private String action;


}
