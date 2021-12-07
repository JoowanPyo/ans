package com.gemiso.zodiac.app.issue;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Date;


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
@ToString
@DynamicUpdate
/*@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)*/
public class Issue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="issu_id",nullable = false )
    private Long issuId;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "ch_div_cd", length = 50)
    private String chDivCd;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = ch_div_cd)")
    private String chDivCdNm;

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

    @Column(name = "issu_org_id", length = 50)
    private Long issuOrgId;

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
