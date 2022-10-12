package com.gemiso.zodiac.app.rundownItem;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gemiso.zodiac.app.rundown.Rundown;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tb_rundown_item"
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //json 파싱때 필드가 없는 클래스를 Serialize문제 설정
public class RundownItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rundown_item_id", nullable = false)
    private Long rundownItemId;

    @Column(name = "brdc_dt")
    private Date brdcDt; // 방송일

    @Column(name = "rundown_time")
    private String rundownTime; // 회의시간 ( 오전 /오후 )

    @Column(name = "artcl_frm_cd", length = 50)
    private String artclFrmCd;// 기사형식

    //@Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = artcl_frm_cd)")
    private String artclFrmCdNm;

    @Column(name = "brdc_start_time", length = 8)
    private String brdcStartTime; // 방송시간 ( 08 두자릿수 )

    @Column(name = "rptr_id", length = 50)
    private String rptrId; //기자 아이디

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = rptr_id)")
    private String rptrNm;

    @Column(name = "rundown_titl", length = 500)
    private String rundownTitl; // 제목

    @Column(name = "rundown_add_data", columnDefinition = "text")
    private String rundownAddData; // 추가내용

    @Column(name = "publication_cd", length = 50)
    private String publicationCd; //기사화

    @Column(name = "rundown_item_ord")
    private Integer rundownItemOrd; // 순번

   /* @Column(name = "inputr_id", length = 50)
    private String inputrId; // 작성자

    @Column(name = "inputr_nm", length = 100)
    @Basic(fetch = FetchType.EAGER)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = inputr_id)")
    private String inputrNm;*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "rundown_id")
    private Rundown rundown;

}
