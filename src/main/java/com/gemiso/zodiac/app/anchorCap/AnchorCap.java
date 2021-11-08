package com.gemiso.zodiac.app.anchorCap;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.capTemplate.CapTemplate;
import com.gemiso.zodiac.app.symbol.Symbol;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@Table(name = "tb_anchor_cap")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"article", "capTemplate", "symbol"})
@DynamicUpdate
public class AnchorCap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "anchor_cap_id", nullable = false)
    private Long anchorCapId;

    @Column(name = "cap_div_cd")
    private String capDivCd;

    @Formula("(select a.cd_nm from tb_cd a where a.cd = cap_div_cd)")
    private String capDivCdNm;

    @Column(name = "ln_no")
    private int lnNo;

    @Column(name = "cap_ctt", columnDefinition = "text")
    private String capCtt;

    @Column(name = "cap_rmk", length = 2000)
    private String capRmk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artcl_id")
    @JsonBackReference
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cap_tmplt_id")
    private CapTemplate capTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "symbol_id")
    private Symbol symbol;
}
