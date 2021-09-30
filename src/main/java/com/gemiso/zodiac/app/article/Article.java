package com.gemiso.zodiac.app.article;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gemiso.zodiac.app.articleCap.ArticleCap;
import com.gemiso.zodiac.app.articleHist.ArticleHist;
import com.gemiso.zodiac.app.articleMedia.ArticleMedia;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.issue.Issue;
import com.gemiso.zodiac.app.articleOrder.ArticleOrder;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_artcl")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"cueSheetItem", "articleMedia", "articleOrder", "articleHist", "articleCap"})
@DynamicUpdate
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artcl_id", nullable = false)
    private Long artclId;

    /*@Column(name = "issu_id", nullable = false)
    private Long issuId;*/

    @Column(name = "ch_div_cd", length = 50)
    private String chDivCd;

    @Column(name = "artcl_kind_cd", length = 50)
    private String artclKindCd;

    @Column(name = "artcl_frm_cd", length = 50)
    private String artclFrmCd;

    @Column(name = "artcl_div_cd", length = 50)
    private String artclDivCd;

    @Column(name = "artcl_fld_cd", length = 50)
    private String artclFldCd;

    @Column(name = "apprv_div_cd", length = 50)
    private String apprvDivCd;

    @Column(name = "prd_div_cd", length = 50)
    private String prdDivCd;

    @Column(name = "artcl_typ_cd", length = 50)
    private String artclTypCd;

    @Column(name = "artcl_typ_dtl_cd", length = 50)
    private String artclTypDtlCd;

    @Column(name = "artcl_cate_cd", length = 50)
    private String artclCateCd;

    @Column(name = "artcl_titl", length = 500)
    private String artclTitl;

    @Column(name = "artcl_titl_en", length = 300)
    private String artclTitlEn;

    @Column(name = "artcl_ctt", columnDefinition = "TEXT")
    private String artclCtt;

    @Column(name = "anc_ment_ctt", columnDefinition = "TEXT")
    private String ancMentCtt;

    @Column(name = "rptr_nm", length = 100)
    private String rptrNm;

    @Column(name = "user_grp_id", length = 21)
    private String userGrpId;

    @Column(name = "artcl_reqd_sec_div_yn", columnDefinition = "bpchar(1) default 'N'")
    private String artclReqdSecDivYn;

    @Column(name = "artcl_reqd_sec", columnDefinition = "numeric(10)")
    private Integer artclReqdSec;

    @Column(name = "lck_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String lckYn;

    @Column(name = "lck_dtm")
    private Date lckDtm;

    @Column(name = "apprv_dtm")
    private Date apprvDtm;

    @Column(name = "artcl_ord")
    private Integer artclOrd;

    @Column(name = "brdc_cnt")
    private Integer brdcCnt;

    @Column(name = "org_artcl_id")
    private Long orgArtclId;

    @Column(name = "urg_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String urgYn;

    @Column(name = "frnoti_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String frnotiYn;

    @Column(name = "embg_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String embgYn;

    @Column(name = "embg_dtm")
    private Date embgDtm;

    @Column(name = "inputr_nm", length = 50)
    private String inputrNm;

    @Column(name = "del_dtm")
    private Date delDtm;

    @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String delYn;

    @Column(name = "noti_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String notiYn;

    @Column(name = "reg_app_typ", length = 50)
    private String regAppTyp;

    @Column(name = "brdc_pgm_id")
    private Long brdcPgmId;

    @Column(name = "brdc_schd_dtm")
    private Date brdcSchdDtm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inputr_id")
    private User inputr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updtr_id")
    private User updtr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delr_id")
    private User delr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apprvr_id")
    private User apprvr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lckr_id")
    private User lckr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rptr_id")
    private User rptr;

    @Column(name = "artcl_ctt_time")
    private Integer artclCttTime;

    @Column(name = "anc_ment_ctt_time")
    private Integer ancMentCttTime;

    @Column(name = "artcl_ext_time")
    private Integer artclExtTime;

    @Column(name = "video_time")
    private Integer videoTime;

    @Column(name = "dept_cd", length = 50, nullable = false)
    private String deptCd;

    @Column(name = "device_cd", length = 50, nullable = false)
    private String deviceCd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issu_id")
    private Issue issue;

    @OneToMany(mappedBy="article")
    @JsonManagedReference
    private List<CueSheetItem> cueSheetItem = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    @JsonManagedReference
    private List<ArticleMedia> articleMedia = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    @JsonManagedReference
    private List<ArticleOrder> articleOrder = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    @JsonManagedReference
    private List<ArticleHist> articleHist = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    @JsonManagedReference
    private List<ArticleCap> articleCap = new ArrayList<>();

    @PrePersist
    public void prePersist() {

        if (this.artclReqdSecDivYn == null || this.artclReqdSecDivYn == "") {
            this.artclReqdSecDivYn = "N";
        }
        if (this.lckYn == null || this.lckYn == "") {
            this.lckYn = "N";
        }
        if (this.urgYn == null || this.urgYn == "") {
            this.urgYn = "N";
        }
        if (this.frnotiYn == null || this.frnotiYn == "") {
            this.frnotiYn = "N";
        }
        if (this.embgYn == null || this.embgYn == "") {
            this.embgYn = "N";
        }
        if (this.delYn == null || this.delYn == "") {
            this.delYn = "N";
        }
        if (this.notiYn == null || this.notiYn == "") {
            this.notiYn = "N";
        }

    }

}
