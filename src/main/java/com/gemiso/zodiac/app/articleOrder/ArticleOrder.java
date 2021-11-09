package com.gemiso.zodiac.app.articleOrder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.code.Code;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.app.user.UserGroupUser;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
@ToString(exclude = {"article","articleOrderFile"})
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

    @Formula("(select a.cd_nm from tb_cd a where a.cd = order_div_cd)")
    private String orderDivCdNm;

    @Column(name = "order_status", length = 30)
    private String orderStatus;

    @Column(name = "content_id", length = 8)
    private int contentId;

    @Column(name = "title", length = 500)
    private String title;

    @Column(name = "ord_rmk", columnDefinition = "text")
    private String ordRmk;

    @Column(name = "inputr_id", length = 50)
    private String inputrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = inputr_id)")
    private String inputrNm;

    @Column(name = "updtr_id", length = 50)
    private String updtrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = updtr_id)")
    private String updtrNm;

    @Column(name = "workr_id", length = 50)
    private String workrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = workr_id)")
    private String workrNm;

    @Column(name = "client_id", length = 50)
    private String clientId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = client_id)")
    private String clientNm;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artcl_id")
    @JsonBackReference
    private Article article;

    @OneToMany(mappedBy="articleOrder")//cascade = CascadeType.ALL은 부모가 삭제될때 자식도 같이 삭제?
    private List<ArticleOrderFile> articleOrderFile = new ArrayList<>();




}
