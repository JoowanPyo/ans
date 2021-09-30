package com.gemiso.zodiac.app.code;

import com.gemiso.zodiac.app.user.User;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        name = "tb_cd",
        uniqueConstraints = {
                @UniqueConstraint(name = "code_cdId_unique", columnNames = "cd_id")
        }
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
@DynamicUpdate
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_id", nullable = false)
    private Long cdId;

    @Column(name = "cd", length = 50)
    private String cd;

    @Column(name = "cd_nm", length = 100)
    private String cdNm;

    @Column(name = "cd_expl", length = 500)
    private String cdExpl;

    @Column(name = "hrnk_cd_id", length = 21)
    private Long hrnkCdId;

    @Column(name = "use_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String useYn;

    @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String delYn;

    @Column(name = "cd_ord")
    private Integer cdOrd;

    @Column(name = "input_dtm")
    private Date inputDtm;

    @Column(name = "updt_dtm")
    private Date updtDtm;

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

    @PrePersist
    public void prePersist() {

        if (this.useYn == null || this.useYn == "") {
            this.useYn = "Y";
        }
        if (this.delYn == null || this.delYn == "") {
            this.delYn = "N";
        }

    }

}
