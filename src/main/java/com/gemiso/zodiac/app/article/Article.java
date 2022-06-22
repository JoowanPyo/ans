package com.gemiso.zodiac.app.article;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gemiso.zodiac.app.anchorCap.AnchorCap;
import com.gemiso.zodiac.app.articleCap.ArticleCap;
import com.gemiso.zodiac.app.articleMedia.ArticleMedia;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.issue.Issue;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "tb_artcl",
        indexes = {@Index(name = "index_article_input_dtm", columnList = "input_dtm")
                , @Index(name = "index_article_title", columnList = "artcl_titl")
                , @Index(name = "index_article_title_en", columnList = "artcl_titl_en")
                , @Index(name = "index_article_retrid", columnList = "rptr_id")
        })
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"issue", "cueSheet", "articleMedia", "articleCap", "anchorCap"})
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//@Document(indexName = "articles")
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artcl_id", nullable = false)
    private Long artclId;

    @Column(name = "artcl_seq_id", length = 21)
    private String artclSeqId;

    /*@Column(name = "issu_id", nullable = false)
    private Long issuId;*/

    @Column(name = "ch_div_cd", length = 50)
    private String chDivCd;

   /* @Basic(fetch = FetchType.EAGER)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = ch_div_cd)")
    private String chDivCdNm;*/

    @Column(name = "artcl_kind_cd", length = 50)
    private String artclKindCd;

    /*//@Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = artcl_kind_cd)")
    private String artclKindCdNm;*/

    @Column(name = "artcl_frm_cd", length = 50)
    private String artclFrmCd;

    /*//@Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = artcl_frm_cd)")
    private String artclFrmCdNm;*/

    @Column(name = "artcl_div_cd", length = 50)
    private String artclDivCd;

    //@Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = artcl_div_cd)")
    private String artclDivCdNm;

    @Column(name = "artcl_fld_cd", length = 50)
    private String artclFldCd;

    //@Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = artcl_fld_cd)")
    private String artclFldCdNm;

    @Column(name = "apprv_div_cd", length = 50)
    private String apprvDivCd;

    @Basic(fetch = FetchType.EAGER)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = apprv_div_cd)")
    private String apprvDivCdNm;

    @Column(name = "prd_div_cd", length = 50)
    private String prdDivCd;

    /*//@Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = prd_div_cd)")
    private String prdDivCdNm;*/

    @Column(name = "artcl_typ_cd", length = 50)
    private String artclTypCd;

    //@Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = artcl_typ_cd)")
    private String artclTypCdNm;

    @Column(name = "artcl_typ_dtl_cd", length = 50)
    private String artclTypDtlCd;

    //@Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = artcl_typ_dtl_cd)")
    private String artclTypDtlCdNm;

    @Column(name = "artcl_cate_cd", length = 50)
    private String artclCateCd;

    //@Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = artcl_cate_cd)")
    private String artclCateCdNm;

    @Column(name = "artcl_titl", length = 500)
    private String artclTitl;

    @Column(name = "artcl_titl_en", length = 300)
    private String artclTitlEn;

    @Column(name = "artcl_ctt", columnDefinition = "TEXT")
    private String artclCtt;

    @Column(name = "anc_ment_ctt", columnDefinition = "TEXT")
    private String ancMentCtt;

    //@Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = rptr_id)")
    @Column(name = "rptr_nm", length = 100)
    private String rptrNm;

    @Column(name = "user_grp_id", length = 8)
    private Long userGrpId;

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

    @Column(name = "org_artcl_id", length = 8)
    private Long orgArtclId;

    @Column(name = "urg_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String urgYn;

    @Column(name = "frnoti_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String frnotiYn;

    @Column(name = "embg_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String embgYn;

    @Column(name = "embg_dtm")
    private Date embgDtm;

    @Column(name = "inputr_nm", length = 100)
    @Basic(fetch = FetchType.EAGER)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = inputr_id)")
    private String inputrNm;

    @Column(name = "del_dtm")
    private Date delDtm;

    @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String delYn;

    @Column(name = "noti_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String notiYn;

    @Column(name = "reg_app_typ", length = 50)
    private String regAppTyp;

    @Column(name = "brdc_pgm_id", length = 50)
    private String brdcPgmId;

    @Column(name = "brdc_schd_dtm")
    private Date brdcSchdDtm;

    @Column(name = "inputr_id", length = 50)
    private String inputrId;

    @Column(name = "updtr_id", length = 50)
    private String updtrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = updtr_id)")
    private String updtrNm;

    @Column(name = "delr_id", length = 50)
    private String delrId;

   /* @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = delr_id)")
    private String delrNm;*/

    @Column(name = "apprvr_id", length = 50)
    private String apprvrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = apprvr_id)")
    private String apprvrNm;

    @Column(name = "lckr_id", length = 50)
    private String lckrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = lckr_id)")
    private String lckrNm;

    @Column(name = "rptr_id", length = 50)
    private String rptrId;

    @Column(name = "artcl_ctt_time")
    private Integer artclCttTime;

    @Column(name = "anc_ment_ctt_time")
    private Integer ancMentCttTime;

    @Column(name = "artcl_ext_time")
    private Integer artclExtTime;

    @Column(name = "video_time")
    private Integer videoTime;

    @Column(name = "dept_cd", length = 50)
    private Long deptCd;

    //@Basic(fetch = FetchType.LAZY)
    @Formula("(select a.name from tb_depts a where a.id = dept_cd)")
    private String deptNm;

    @Column(name = "parent_artlc_id", length = 8)
    private Long parentArtlcId;

    @Column(name = "memo", columnDefinition = "text")
    private String memo;

    @Column(name = "editor_id", length = 50)
    private String editorId;

    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = editor_id)")
    private String editorNm;

    @Column(name = "artcl_fix_user", length = 50)
    private String artclFixUser;

    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = artcl_fix_user)")
    private String artclFixUserNm;

    @Column(name = "editor_fix_user", length = 50)
    private String editorFixUser;

    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = editor_fix_user)")
    private String editorFixUserNm;

    @Column(name = "anchor_fix_user", length = 50)
    private String anchorFixUser;

    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = anchor_fix_user)")
    private String anchorFixUserNm;

    @Column(name = "desk_fix_user", length = 50)
    private String deskFixUser;

    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = desk_fix_user)")
    private String deskFixUserNm;

    @Column(name = "artcl_fix_dtm")
    private Date artclFixDtm;

    @Column(name = "editor_fix_dtm")
    private Date editorFixDtm;

    @Column(name = "anchor_fix_dtm")
    private Date anchorFixDtm;

    @Column(name = "desk_fix_dtm")
    private Date deskFixDtm;

    /*@Column(name = "cue_id", length = 50)
    private Long cueId;*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cue_id")
    private CueSheet cueSheet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issu_id")
    private Issue issue;

   /* @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    @JsonManagedReference
    @Where(clause = "del_yn = 'N'")*/

    @BatchSize(size = 100)
    @OrderBy(value = "inputDtm DESC")
    @Where(clause = "del_yn = 'N'")
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<ArticleMedia> articleMedia = Collections.emptySet();

    /*@OneToMany(mappedBy = "article")
    @JsonManagedReference
    private List<ArticleOrder> articleOrder = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    @JsonManagedReference
    private List<ArticleHist> articleHist = new ArrayList<>();*/

    @BatchSize(size = 100)
    @OrderBy(value = "lnOrd ASC")
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<ArticleCap> articleCap =  Collections.emptySet();

    @BatchSize(size = 100)
    @OrderBy(value = "lnOrd ASC")
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<AnchorCap> anchorCap =  Collections.emptySet();

   /* @OneToMany(mappedBy = "article")
    @JsonManagedReference
    private List<ArticleTag> articleTag = new ArrayList<>();*/

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
