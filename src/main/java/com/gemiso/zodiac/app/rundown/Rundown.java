package com.gemiso.zodiac.app.rundown;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gemiso.zodiac.app.articleCap.ArticleCap;
import com.gemiso.zodiac.app.rundownItem.RundownItem;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "tb_rundown"
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@DynamicUpdate
public class Rundown {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rundown_id", nullable = false)
    private Long rundownId;

    @Column(name = "rundown_dt")
    private Date rundownDt; // 런다운 일시

    @Column(name = "rundown_time")
    private String rundownTime; // 회의시간 ( 오전 /오후 )

    @Column(name = "rundown_data", columnDefinition = "text")
    private String rundownData; // 회의자료

    @Column(name = "headline", columnDefinition = "text")
    private String headline; // 헤드라인

    @Column(name = "inputr_id", length = 50)
    private String inputrId; // 작성자

    @Column(name = "inputr_nm", length = 100)
    @Basic(fetch = FetchType.EAGER)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = inputr_id)")
    private String inputrNm;

    @Column(name = "updtr_id", length = 50)
    private String updtrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = updtr_id)")
    private String updtrNm;

    @BatchSize(size = 100)
    @OrderBy(value = "rundownItemOrd ASC")
    @OneToMany(mappedBy = "rundown", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<RundownItem> rundownItems =  Collections.emptySet();
}
