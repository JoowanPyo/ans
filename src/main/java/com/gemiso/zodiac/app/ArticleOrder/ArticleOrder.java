package com.gemiso.zodiac.app.ArticleOrder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tb_order"/*,
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
public class ArticleOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name ="order_ctt", columnDefinition = "text")
    private String orderCtt;

    @Column(name = "order_div_cd", length = 50)
    private String orderDivCd;

    @Column(name = "order_status", length = 30)
    private String orderStatus;

    @Column(name = "content_id", length = 8)
    private int contentId;

    @Column(name = "title", length = 500)
    private String title;

    @Column(name = "ord_rmk", columnDefinition = "text")
    private String ordRmk;

    @Column(name = "inputr_id", length = 50, nullable = false)
    private String inputrId;

    @Column(name = "updtr_id", length = 50)
    private String updtrId;

    @Column(name = "workr_id", length = 50)
    private String workrId;

    @Column(name = "client_id", length = 50)
    private String clientId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artcl_id")
    @JsonBackReference
    private Article article;




}
