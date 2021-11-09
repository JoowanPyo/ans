package com.gemiso.zodiac.app.articleCap;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.capTemplate.CapTemplate;
import com.gemiso.zodiac.app.code.Code;
import com.gemiso.zodiac.app.symbol.Symbol;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@Table(name = "tb_artcl_cap"/*,
        uniqueConstraints = {
                @UniqueConstraint(name = "file_fileId_unique", columnNames = "file_id")
        }*/)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"article", "capTemplate", "symbol"})
@DynamicUpdate
public class ArticleCap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artcl_cap_id", nullable = false)
    private Long artclCapId;

    @Column(name = "cap_div_cd", length = 50)
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
