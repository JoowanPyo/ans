package com.gemiso.zodiac.app.articleCapHist;

import com.gemiso.zodiac.app.articleHist.ArticleHist;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tb_artcl_cap_hist")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "articleHist")
@DynamicUpdate
public class ArticleCapHist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artcl_cap_hist_id", nullable = false)
    private Long artclCapHistId;

    @Column(name = "ln_no")
    private int lnNo;

    @Column(name = "cap_tmplt_id", length = 21)
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
