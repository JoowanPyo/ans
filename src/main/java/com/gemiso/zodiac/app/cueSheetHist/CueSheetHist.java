package com.gemiso.zodiac.app.cueSheetHist;

import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.issue.Issue;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tb_cue_hist",
        indexes = { @Index(name = "index_cue_hist_action", columnList = "cue_action")})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@DynamicUpdate
@EntityListeners(value = {AuditingEntityListener.class})
public class CueSheetHist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cue_hist_id", nullable = false)
    private Long cueHistId;

    @Column(name = "cue_ver")
    private int cueVer;

    @Column(name = "cue_action", length = 100)
    private String cueAction;

    @Column(name = "cue_item_data", columnDefinition = "text")
    private String cueItemData;

    @CreatedDate
    @Column(name = "input_dtm")
    private Date inputDtm;

    @Column(name = "inputr_id")
    private String inputrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = inputr_id)")
    private String inputrNm;

    @ManyToOne
    @JoinColumn(name = "cue_id")
    private CueSheet cueSheet;
}
