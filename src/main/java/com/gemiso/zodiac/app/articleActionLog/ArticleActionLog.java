package com.gemiso.zodiac.app.articleActionLog;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.issue.Issue;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "artcl_action_log")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@DynamicUpdate
public class ArticleActionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "message", length = 50)
    private String message;

    @Column(name = "action", length = 50)
    private String action;

    @Column(name = "artcl_info", columnDefinition = "json")
    private String artclInfo;

    @Column(name = "anchor_cap_info", columnDefinition = "json")
    private String anchorCapInfo;

    @Column(name = "artcl_cap_info", columnDefinition = "json")
    private String artclCapInfo;

    @ManyToOne
    @JoinColumn(name = "artcl_id")
    private Article article;
}
