package com.gemiso.zodiac.app.yonhapAssign;

import com.gemiso.zodiac.app.issue.Issue;
import com.gemiso.zodiac.app.yonhap.Yonhap;
import com.gemiso.zodiac.app.yonhapWire.YonhapWire;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tb_wire_assign")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
@DynamicUpdate
@EntityListeners(value = {AuditingEntityListener.class})
public class YonhapAssign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assign_id", nullable = false)
    private Long assignId;

    @ManyToOne
    @JoinColumn(name = "yonhap_id")
    private Yonhap yonhap;

    /*@ManyToOne
    @JoinColumn(name = "yh_artcl_id")
    private Yonhap yonhap;*/

    @ManyToOne
    @JoinColumn(name = "wire_id")
    private YonhapWire yonhapWire;

    @Column(name = "designator_id", length = 50)
    private String designatorId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = designator_id)")
    private String designatorNm;

    @Column(name = "assigner_id", length = 50)
    private String assignerId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = assigner_id)")
    private String assignerNm;

    @CreatedDate
    @Column(name = "input_dtm", updatable = false)
    private Date inputDtm;
}
