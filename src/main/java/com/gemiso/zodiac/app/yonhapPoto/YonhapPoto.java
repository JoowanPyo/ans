package com.gemiso.zodiac.app.yonhapPoto;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        name = "tb_yonhap_photo"
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
@DynamicUpdate
@EntityListeners(value = {AuditingEntityListener.class})
public class YonhapPoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "yonhap_artcl_id", nullable = false)
    private Long yonhapArtclId;

    @Column(name = "cont_id")
    private String contId;

    @Column(name = "imprt", columnDefinition = "bpchar(1)")
    private String imprt;

    @Column(name = "svc_typ", length = 5)
    private String svcTyp;

    @Column(name = "artcl_titl", length = 500)
    private String artclTitl;

    @Column(name = "artcl_smltitl", length = 500)
    private String artclSmltitl;

    @Column(name = "artcl_ctt", columnDefinition = "TEXT")
    private String artclCtt;

    @Column(name = "artcl_cate_cd", length = 50)
    private String artclCateCd;

    @Column(name = "region_cd", length = 50)
    private String regionCd;

    @Column(name = "region_nm", length = 1000)
    private String regionNm;

    @Column(name = "ctt_class_cd", length = 50)
    private String cttClassCd;

    @Column(name = "ctt_class_add_cd", length = 100)
    private String cttClassAddCd;

    @Column(name = "yonhap_photo_div_cd", length = 50)
    private String yonhapPhotoDivCd;

    @Column(name = "yonhap_publ_no", length = 50)
    private String yonhapPublNo;

    @Column(name = "src", length = 100)
    private String src;

    @Column(name = "credit", length = 100)
    private String credit;

    @Column(name = "cmnt", length = 2000)
    private String cmnt;

    @CreatedDate
    @Column(name = "input_dtm")
    private Date inputDtm;

    @Column(name = "trnsf_dtm")
    private Date trnsfDtm;

    @Column(name = "embg_dtm")
    private Date embgDtm;

    @Column(name = "action", columnDefinition = "bpchar(1)")
    private String action;

}
