package com.gemiso.zodiac.app.issue;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_issu"/*,
        uniqueConstraints = {
                @UniqueConstraint(name = "issu_issuId_unique", columnNames = "issu_id")
        }*/)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "article")
@DynamicUpdate
public class Issue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="issu_id",nullable = false )
    private Long issuId;

    @Column(name = "ch_div_cd", length = 50 )
    private String chDivCd;

    @Column(name = "issu_dtm", nullable = false)
    private Date issuDtm;

    @Column(name = "issu_ord")
    private Integer issuOrd;

    @Column(name = "issu_kwd", length = 300, nullable = false)
    private String issuKwd;

    @Column(name = "issu_ctt",length = 3000)
    private String issuCtt;

    @Column(name = "issu_fnsh_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String issuFnshYn;

    @Column(name = "issu_del_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String issuDelYn;

    @Column(name = "del_dtm")
    private Date delDtm;

    @Column(name = "issu_fnsh_dtm")
    private Date  issuFnshDtm;

/*    @Column( name = "input_dtm", updatable = false)
    private Date inputDtm;

    @Column(name = "updt_dtm")
    private Date updtDtm;*/

    @Column(name = "issu_org_id", length = 50)
    private Long issuOrgId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inputr_id", nullable = false)
    private User inputr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updtr_id")
    private User updtr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delr_id")
    private User delr;

    /*@OneToMany(mappedBy="issue")
    @JsonManagedReference
    private List<Article> article;*/

    /**
     * insert 되기전 (persist 되기전) 실행된다.
     * @PrePersist
     * @PostPersist
     * @PreRemove
     * @PostRemove
     * @PreUpdate
     * @PostUpdate
     * @PostLoad
     * */
    @PrePersist
    public void prePersist() {

        if(this.issuFnshYn == null || this.issuFnshYn == ""){
            this.issuFnshYn = "N";
        }
        if(this.issuDelYn == null || this.issuDelYn == ""){
            this.issuDelYn = "N";
        }
    }

    @PreUpdate //사실이건 필요없음
    public void preUpdate(){

        if(this.issuFnshYn == null || this.issuFnshYn == ""){
            this.issuFnshYn = "N";
        }
        if(this.issuDelYn == null || this.issuDelYn == ""){
            this.issuDelYn = "N";
        }

    }
}
