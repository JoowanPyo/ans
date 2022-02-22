package com.gemiso.zodiac.app.scrollNewsDetail;

import com.gemiso.zodiac.app.scrollNews.ScrollNews;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@Table(name = "tb_scrl_news_dtl")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "scrollNews")
@DynamicUpdate
public class ScrollNewsDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /*@Column(name = "category", length = 50)
    private String category;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = category)")
    private String categoryNm;*/

    @Column(name = "titl", length = 255)
    private String titl;

    @Column(name = "ctt_ord")
    private int cttOrd;

    @Column(name = "ctt_json", columnDefinition = "json")
    private String cttJson;

    @ManyToOne
    @JoinColumn(name = "scrl_news_id")
    private ScrollNews scrollNews;
}
