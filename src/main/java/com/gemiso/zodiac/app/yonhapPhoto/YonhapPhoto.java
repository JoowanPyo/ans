package com.gemiso.zodiac.app.yonhapPhoto;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
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
public class YonhapPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "yonhap_artcl_id", nullable = false)
    private Long yonhapArtclId;

    @Column(name = "cont_id", length = 100)
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

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = ctt_class_add_cd)")
    private String cttClassAddNm;

    @Column(name = "yonhap_photo_div_cd", length = 50)
    private String yonhapPhotoDivCd;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = yonhap_photo_div_cd)")
    private String yonhapPhotoDivNm;

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

    @Column(name = "mam_cont_id")
    private Long mamContId;

    /*@OneToMany(mappedBy="yonhapPhoto")//cascade = CascadeType.ALL은 부모가 삭제될때 자식도 같이 삭제
    private List<YonhapPhotoAttchFile> yonhapPhotoAttchFiles = new ArrayList<>();*/
}
