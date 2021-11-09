package com.gemiso.zodiac.app.articleHist;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.code.Code;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tb_artcl_hist"/*,
        uniqueConstraints = {
                @UniqueConstraint(name = "file_fileId_unique", columnNames = "file_id")
        }*/)
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
    private int artclOrd;

    @Column(name = "org_artcl_id", length = 21)
    private Long orgArtclId;

    @Column(name = "input_dtm")
    private Date inputDtm;

    @Column(name = "ver")
    private int ver;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artcl_id")
    @JsonBackReference
    private Article article;


}
