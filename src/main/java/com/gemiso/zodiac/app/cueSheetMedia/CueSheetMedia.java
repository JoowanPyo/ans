package com.gemiso.zodiac.app.cueSheetMedia;

import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        name = "tb_cue_media"/*,
        uniqueConstraints = {
                @UniqueConstraint(name = "user_userId_unique", columnNames = "user_id")
        }*/
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
@DynamicUpdate
public class CueSheetMedia extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cue_media_id", nullable = false)
    private Long cueMediaId;

    @Column(name = "media_typ_cd", length = 50)
    private String mediaTypCd;

    @Column(name = "media_ord")
    private int mediaOrd;

    @Column(name = "cont_id", length = 8)
    private int contId;

    @Column(name = "trnsf_file_nm", length = 1000)
    private String trnsfFileNm;

    @Column(name = "media_durtn", length = 20)
    private String mediaDurtn;

    @Column(name = "media_mtch_dtm")
    private Date mediaMtchDtm;

    @Column(name = "trnsf_st_cd", length = 50)
    private String trnsfStCd;

    @Column(name = "assn_st_cd", length = 50)
    private String assnStCd;

    @Column(name = "video_edtr_nm", length = 100)
    private String videoEdtrNm;

    @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'")
    private String delYn;

    @Column(name = "del_dtm")
    private Date delDtm;

    @Column(name = "video_edtr_id", length = 50)
    private String videoEdtrId;

    @Column(name = "inputr_id", length = 50, nullable = false)
    private String inputrId;

    @Column(name = "updtr_id", length = 50)
    private String updtrId;

    @Column(name = "delr_id", length = 50)
    private String delrId;

    @Column(name = "cue_item_id", nullable = false)
    private Long cueItemId;

    @PrePersist
    public void prePersist() {

        if (this.delYn == null || this.delYn == "") {
            this.delYn = "N";
        }
    }

}
