package com.gemiso.zodiac.app.symbol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gemiso.zodiac.app.file.AttachFile;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        name = "tb_symbol",
        indexes = { @Index(name = "index_symbol_nm", columnList = "symbol_nm")},
        uniqueConstraints = {
                @UniqueConstraint(name = "symbol_symbolId_unique", columnNames = "symbol_id")
        }
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"attachFile"})
@Setter
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //json 파싱때 필드가 없는 클래스를 Serialize문제 설정
public class Symbol extends BaseEntity {

    @Id
    @Column(name = "symbol_id", nullable = false, length = 50)
    private String symbolId;

    @Column(name = "symbol_nm", length = 50)
    private String symbolNm;

    @Column(name = "expl", length = 2000)
    private String expl;

    @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String delYn;

    @Column(name = "use_yn", columnDefinition = "bpchar(1) default 'Y'", nullable = false)
    private String useYn;

    @Column(name = "cap_tmplt_yn", columnDefinition = "bpchar(1) default 'N'")
    private String cap_tmplt_yn;

    //@Column(name = "input_dtm")
    //private Date inputDtm;

    //@Column(name = "updt_dtm")
    //private Date updtDtm;

    @Column(name = "del_dtm")
    private Date delDtm;

    @Column(name = "inputr_id", length = 50)
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

    @Column(name = "typ_cd", length = 50)
    private String typCd;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = typ_cd)")
    private String typCdNm;

    @Column(name = "symbol_ord")
    private Integer symbolOrd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private AttachFile attachFile;

    /*@OneToMany(mappedBy = "symbol")
    private List<ArticleCap> articleCap;*/

    @PrePersist
    public void prePersist() {

        if (this.useYn == null || this.useYn == ""){
            this.useYn = "Y";
        }
        if (this.delYn == null || this.delYn == ""){
            this.delYn = "N";
        }
        if (this.cap_tmplt_yn == null || this.cap_tmplt_yn == ""){
            this.cap_tmplt_yn = "N";
        }
    }
}
