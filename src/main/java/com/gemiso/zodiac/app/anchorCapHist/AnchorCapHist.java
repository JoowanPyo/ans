package com.gemiso.zodiac.app.anchorCapHist;

import com.gemiso.zodiac.app.articleHist.ArticleHist;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tb_anc_cap_hist")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "articleHist")
@DynamicUpdate
public class AnchorCapHist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "anc_cap_hist_id", nullable = false)
    private Long ancCapHistId;

    @Column(name = "ln_no")
    private int lnNo;

    @Column(name = "cap_tmplt_id")
    private Long capTmpltId;

    @Column(name = "cap_ctt", columnDefinition = "text")
    private String capCtt;

    @Column(name = "cap_rmk", length = 2000)
    private String capRmk;

    @Column(name = "symbol_id", length = 21)
    private String symbolId;

    @Column(name = "cap_div_cd", length = 50)
    private String capDivCd;

    @ManyToOne
    @JoinColumn(name = "artcl_hist_id")
    private ArticleHist articleHist;


}
