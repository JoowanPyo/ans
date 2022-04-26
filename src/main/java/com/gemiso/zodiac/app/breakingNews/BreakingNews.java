package com.gemiso.zodiac.app.breakingNews;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gemiso.zodiac.app.breakingNewsDetail.BreakingNewsDtl;
import com.gemiso.zodiac.app.scrollNewsDetail.ScrollNewsDetail;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_breaking_news")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "breakingNewsDtls")
@DynamicUpdate
public class BreakingNews extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="breaking_news_id",nullable = false )
    private Long breakingNewsId;

    @Column(name = "brdc_dtm", nullable = false)
    private Date brdcDtm;

    @Column(name = "titl", length = 255, nullable = false)
    private String titl;

    @Column(name = "breaking_news_div", length = 50, nullable = false)
    private String breakingNewsDiv;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = breaking_news_div)")
    private String breakingNewsDivNm;

    @Column(name = "ln_typ_cd", length = 50, nullable = false)
    private String lnTypCd;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = ln_typ_cd)")
    private String lnTypCdNm;

    @Column(name = "trnsf_dtm")
    private Date trnsfDtm;

    @Column(name = "trnsf_st_cd", length = 50, nullable = false)
    private String trnsfStCd;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = trnsf_st_cd)")
    private String trnsfStCdNm;

    @Column(name = "del_dtm")
    private Date delDtm;

    @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String delYn;

    @Column(name = "inputr_id", length = 50, nullable = false)
    private String inputrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = inputr_id)")
    private String inputrNm;

    @Column(name = "updtr_id", length = 50)
    private String updtrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = updtr_id)")
    private String updtrNm;

    @Column(name = "delr_id", length = 50)
    private String delrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = delr_id)")
    private String delrNm;

    @OneToMany(mappedBy = "breakingNews")
    @JsonManagedReference
    private List<BreakingNewsDtl> breakingNewsDtls = new ArrayList<>();

    @PrePersist
    public void prePersist() {

        if (this.delYn == null || this.delYn == "") {
            this.delYn = "N";
        }

    }

}
