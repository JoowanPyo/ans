package com.gemiso.zodiac.app.yonhap;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(
        name = "tb_yonhap"
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "yonhapAttchFiles")
@Setter
@DynamicUpdate
@EntityListeners(value = {AuditingEntityListener.class})
public class Yonhap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "yonhap_id", nullable = false)
    private Long yonhapId;

  /*  @Column(name = "yonhap_id", length = 21 ,nullable = false)
    private String yonhapId;*/

    @Column(name = "cont_id", length = 21, nullable = false)
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

    @Column(name = "credit", length = 100)
    private String credit;

    @Column(name = "source", length = 100)
    private String source;

    @Column(name = "artcl_cate_cd", length = 50)
    private String artclCateCd;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = artcl_cate_cd)")
    private String artclCateNm;

    @Column(name = "region_cd", length = 50)
    private String regionCd;

    @Column(name = "region_nm", length = 1000)
    private String regionNm;

    @Column(name = "ctt_class_cd", length = 50)
    private String cttClassCd;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = ctt_class_cd)")
    private String cttClassNm;

    @Column(name = "ctt_class_add_cd", length = 100)
    private String cttClassAddCd;

    @Column(name = "issu_cd", length = 50)
    private String issuCd;

    @Column(name = "stock_cd", length = 50)
    private String stockCd;

    @Column(name = "artclqnty", length = 8)
    private int artclqnty;

    @Column(name = "cmnt", length = 2000)
    private String cmnt;

    @Column(name = "rel_cont_id", length = 1000)
    private String relContId;

    @Column(name = "ref_cont_info", length = 4000)
    private String refContInfo;

    @Column(name = "embg_dtm")
    private Date embgDtm;

    @CreatedDate
    @Column(name = "input_dtm")
    private Date inputDtm;

    @Column(name = "trnsf_dtm")
    private Date trnsfDtm;

    @Column(name = "action", columnDefinition = "bpchar(1)")
    private String action;

    @OneToMany(mappedBy="yonhap")//cascade = CascadeType.ALL은 부모가 삭제될때 자식도 같이 삭제
    private List<YonhapAttchFile> yonhapAttchFiles = new ArrayList<>();

    @PrePersist
    public void prePersist() {

        if (this.imprt == null || this.imprt == "") {
            this.imprt = "N";
        }

    }
}
