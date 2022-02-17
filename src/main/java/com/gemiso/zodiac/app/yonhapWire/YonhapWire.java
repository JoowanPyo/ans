package com.gemiso.zodiac.app.yonhapWire;


import com.gemiso.zodiac.app.yonhapAttchFile.YonhapAttchFile;
import com.gemiso.zodiac.app.yonhapWireAttchFile.YonhapWireAttchFile;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(
        name = "tb_wire"
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
@DynamicUpdate
@EntityListeners(value = {AuditingEntityListener.class})
public class YonhapWire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wire_id", nullable = false)
    private Long wireId;

    @Column(name = "cont_id")
    private String contId;

    @Column(name = "imprt", columnDefinition = "bpchar(1)")
    private String imprt;

    @Column(name = "svc_typ", length = 5)
    private String svcTyp;

    @Column(name = "artcl_titl", length = 500)
    private String artclTitl;

    @Column(name = "artcl_ctt", columnDefinition = "TEXT")
    private String artclCtt;

    @Column(name = "agcy_cd", length = 50)
    private String agcyCd;

    @Column(name = "agcy_nm", length = 50)
    private String agcyNm;

    @Column(name = "credit", length = 100)
    private String credit;

    @Column(name = "artclqnty")
    private int artclqnty;

    @Column(name = "source", length = 100)
    private String source;

    @Column(name = "cmnt", length = 2000)
    private String cmnt;

    @Column(name = "embg_dtm")
    private Date embgDtm;

    @Column(name = "trnsf_dtm")
    private Date trnsfDtm;

    @CreatedDate
    @Column(name = "input_dtm")
    private Date inputDtm;

    @Column(name = "action", columnDefinition = "bpchar(1)")
    private String action;

    @OneToMany(mappedBy="yonhapWire")//cascade = CascadeType.ALL은 부모가 삭제될때 자식도 같이 삭제
    private List<YonhapWireAttchFile> yonhapWireAttchFiles = new ArrayList<>();

}
