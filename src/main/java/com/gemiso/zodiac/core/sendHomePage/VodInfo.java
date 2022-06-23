package com.gemiso.zodiac.core.sendHomePage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Date;
import java.sql.NClob;

@Entity
@Table(name = "VOD_INFO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class VodInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VSEQ", nullable = false)
    private Integer nseq;

    @Column(name = "PROG_CODE", columnDefinition ="char(2)", nullable = false)
    private Long progCode;

    @Column(name = "ROUND_COUNT")
    private Integer roundCount;

    @Column(name = "VOD_TITLE_KOR", length = 400)
    private String vodTileKor;

    @Column(name = "VOD_TITLE_ENG", length = 400)
    private String vodTitleEng;

    @Column(name = "VOD_LINK_URL", length = 100)
    private String vodLinkUrl;

    @Column(name = "VOD_SALE_CHK", length = 100)
    private String vodSaleChk;

    @Column(name = "VOD_ONAIR_DATE", length = 30)
    private String vodOnairDate;

    @Column(name = "VOD_COST")
    private Integer vodCost;

    @Lob
    @Column(name = "VOD_SCRIPT")
    private NClob vodScript;

    @Column(name = "REGDATE", nullable = false)
    private Date regdate;

    @Column(name = "VOD_56K_LINK_URL", length = 100)
    private String vod56kLinkUrl;

    @Column(name = "RUNTIME", length = 20)
    private String runtime;

    @Column(name = "P_NUM", length = 16)
    private String pNum;

    @Column(name = "HIT_NUM")
    private Integer hitNum;

    //@Column(name = "VOD_500K_LINK", length = 100)
    //private String vod500kLink;

    @Column(name = "CATEGORY", length = 1)
    private String category;

    @Column(name = "MOBILE_URL", length = 100)
    private String mobileUrl;

    @Column(name = "THUMBNAIL", length = 50)
    private String thumbnail;

    @Column(name = "SMIL_FILE", length = 100)
    private String smilFile;
}
