package com.gemiso.zodiac.app.symbol;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gemiso.zodiac.app.articleCap.ArticleCap;
import com.gemiso.zodiac.app.file.AttachFile;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.app.user.UserGroupUser;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(
        name = "tb_symbol"
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"attachFile", "articleCap"})
@Setter
@DynamicUpdate
public class Symbol extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "symbol_id", nullable = false)
    private Long symbolId;

    @Column(name = "symbol_nm", length = 50)
    private String symbolNm;

    @Column(name = "expl", length = 200)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inputr_id", nullable = false)
    private User inputr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updtr_id")
    private User updtr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delr_id")
    private User delr;

    @Column(name = "typ_cd", length = 50)
    private String typCd;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "file_id")
    private AttachFile attachFile;

    @OneToMany(mappedBy = "symbol")
    private List<ArticleCap> articleCap;

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
