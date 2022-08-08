package com.gemiso.zodiac.app.articleHist;

import com.gemiso.zodiac.app.article.Article;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tb_artcl_hist",
        indexes = {@Index(name = "index_article_hist_org_artcl_id", columnList = "org_artcl_id")
                , @Index(name = "index_article_hist_artcl_titl", columnList = "artcl_titl")
                , @Index(name = "index_article_hist_artcl_titl_en", columnList = "artcl_titl_en")})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "article")
@DynamicUpdate
public class ArticleHist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artcl_hist_id", nullable = false)
    private Long artclHistId;

    @Column(name = "ch_div_cd", length = 50)
    private String chDivCd;

    @Column(name = "artcl_titl", length = 500)
    private String artclTitl;

    @Column(name = "artcl_titl_en", length = 300)
    private String artclTitlEn;

    @Column(name = "artcl_ctt", columnDefinition = "text")
    private String artclCtt;

    @Column(name = "anc_ment_ctt", columnDefinition = "text")
    private String ancMentCtt;

    @Column(name = "artcl_ord")
    private Integer artclOrd;

    @Column(name = "org_artcl_id", length = 21)
    private Long orgArtclId;

    @Column(name = "input_dtm")
    private Date inputDtm;

    @Column(name = "ver")
    private Integer ver;

    @Column(name = "inputr_id", length = 50)
    private String inputrId;

    @Column(name = "inputr_nm", length = 100)
    @Basic(fetch = FetchType.EAGER)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = inputr_id)")
    private String inputrNm;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artcl_id")
    //@JsonBackReference
    private Article article;


}
