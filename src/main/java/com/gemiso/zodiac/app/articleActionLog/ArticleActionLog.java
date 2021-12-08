package com.gemiso.zodiac.app.articleActionLog;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.issue.Issue;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tb_artcl_action_log")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@DynamicUpdate
@EntityListeners(value = {AuditingEntityListener.class})
@TypeDef(name = "json", typeClass = JsonBinaryType.class)
public class ArticleActionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "message", length = 255)
    private String message;

    @Column(name = "action", length = 50)
    private String action;

    @CreatedDate
    @Column(name = "input_dtm")
    private Date inputDtm;

    @Column(name = "inputr_id", length = 50, nullable = false)
    private String inputrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = inputr_id)")
    private String inputrNm;

    @Type(type = "json")
    @Column(name = "artcl_info", columnDefinition = "json")
    private String artclInfo;

    @Type(type = "json")
    @Column(name = "anchor_cap_info", columnDefinition = "json")
    private String anchorCapInfo;

    @Type(type = "json")
    @Column(name = "artcl_cap_info", columnDefinition = "json")
    private String artclCapInfo;

    @ManyToOne
    @JoinColumn(name = "artcl_id")
    private Article article;
}
