package com.gemiso.zodiac.app.cueSheetMedia;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.code.Code;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CueSheetMedia extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cue_media_id", nullable = false)
    private Long cueMediaId;

    @Column(name = "media_typ_cd", length = 50)
    private String mediaTypCd;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = media_typ_cd)")
    private String mediaTypCdNm;

    @Column(name = "media_ord")
    private int mediaOrd;

    @Column(name = "cont_id", length = 8)
    private int contId;

    @Column(name = "trnsf_file_nm", length = 255)
    private String trnsfFileNm;

    @Column(name = "media_durtn", length = 20)
    private Integer mediaDurtn;

    @Column(name = "media_mtch_dtm")
    private Date mediaMtchDtm;

    @Column(name = "trnsf_st_cd", length = 50)
    private String trnsfStCd;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = trnsf_st_cd)")
    private String trnsfStCdNm;

    @Column(name = "assn_st_cd", length = 50)
    private String assnStCd;

    /*@Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = assn_st_cd)")
    private String assnStCdNm;*/

    @Column(name = "video_edtr_nm", length = 100)
    private String videoEdtrNm;

    @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'")
    private String delYn;

    @Column(name = "del_dtm")
    private Date delDtm;

    @Column(name = "video_edtr_id", length = 50)
    private String videoEdtrId;

    @Column(name = "inputr_id", length = 50)
    private String inputrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = inputr_id)")
    private String inputrNm;

    @Column(name = "updtr_id", length = 50)
    private String updtrId;

   /* @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = updtr_id)")
    private String updtrNm;*/

    @Column(name = "delr_id", length = 50)
    private String delrId;

   /* @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = delr_id)")
    private String delrNm;*/

    @Column(name = "cue_media_titl", length = 300)
    private String cueMediaTitl;

    @Column(name = "video_id", length = 6)
    private String videoId;

    @Column(name = "trnasf_val")
    private Integer trnasfVal;

    /*@Column(name = "cue_item_id", nullable = false)
    private Long cueItemId;*/

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cue_item_id")
    @JsonBackReference
    private CueSheetItem cueSheetItem;

    @PrePersist
    public void prePersist() {

        if (this.delYn == null || this.delYn == "") {
            this.delYn = "N";
        }
    }

}
