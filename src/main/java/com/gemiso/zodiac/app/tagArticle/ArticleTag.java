package com.gemiso.zodiac.app.tagArticle;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.tag.Tag;
import com.gemiso.zodiac.app.user.User;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(
        name = "tb_artcl_tag"
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "article")
@Setter
@DynamicUpdate
public class ArticleTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_id")
    private Tag tag;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artcl_id")
    @JsonBackReference
    private Article article;
}
