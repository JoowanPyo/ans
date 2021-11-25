package com.gemiso.zodiac.app.scrollNews;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gemiso.zodiac.app.scrollNewsDetail.ScrollNewsDetail;
import com.gemiso.zodiac.app.tag.ArticleTag;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_scrl_news")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "scrollNewsDetail")
@DynamicUpdate
public class ScrollNews extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scrl_news_id", nullable = false)
    private Long scrlNewsId;

    @Column(name = "brdc_dtm")
    private Date brdcDtm;

    @Column(name = "scrl_div_cd", length = 50)
    private String scrlDivCd;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = scrl_div_cd)")
    private String scrlDivCdNm;

    @Column(name = "scrl_frm_cd", length = 50)
    private String scrlFrmCd;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = scrl_frm_cd)")
    private String scrlFrmCdNm;

    @Column(name = "titl", length = 255, nullable = false)
    private String titl;

    @Column(name = "file_nm", length = 255, nullable = false)
    private String fileNm;

    @Column(name = "trnsf_dtm", nullable = false)
    private Date trnsfDtm;

    @Column(name = "trnsf_st_cd", nullable = false)
    private String trnsfStCd;

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

    @OneToMany(mappedBy = "scrollNews")
    @JsonManagedReference
    private List<ScrollNewsDetail> scrollNewsDetail = new ArrayList<>();


    @PrePersist
    public void prePersist() {

        if(this.delYn == null || this.delYn == ""){
            this.delYn = "N";
        }

    }


}
