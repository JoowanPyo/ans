package com.gemiso.zodiac.core.sendHomePage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Clob;
import java.util.Date;

@Entity
@Table(name = "ENG_NEWS")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EndNews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NSEQ", nullable = false)
    private Long nseq;

    @Column(name = "CATEGORY", columnDefinition ="char(2)", nullable = false)
    private Long category;

    @Column(name = "RUN_DATE")
    private Date runDate;

    @Column(name = "PROG_TITLE", length = 200)
    private String progTitle;

    @Column(name = "WRITER", length = 60)
    private String writer;

    @Column(name = "EMAIL", length = 150)
    private String email;

    @Column(name = "MAIN_IMG", length = 100)
    private String mainImg;

    @Column(name = "VOD_LINK", length = 200)
    private String vodLink;

    @Column(name = "TOP_IMG", length = 100)
    private String topImg;

    @Column(name = "NEWS_DEGREE")
    private Integer newsDegree;

    @Column(name = "SUB_TITLE", length = 200)
    private String subTitle;

    @Lob
    @Column(name = "CONTENTS")
    private Clob contents;

    @Column(name = "REGDATE", nullable = false)
    private Date regdate;

    @Column(name = "HITCNT")
    private Integer hitcnt;

    @Column(name = "OTHER_TOP", columnDefinition ="char(1)")
    private Long otherTop;

    @Column(name = "ITEM_ID", length = 30)
    private String itemId;

    @Column(name = "VOD_HIT", columnDefinition ="char(2)")
    private Integer vodHit;

    @Column(name = "VOD_MOBILE", length = 200)
    private String vodMobile;

    @Column(name = "NEWS_TIME", length = 200)
    private String newsTime;

    @Column(name = "FIRST_DATE")
    private Date firstDate;

    @Column(name = "TOPIC_WORDS", length = 1000)
    private String topicWords;

    @Column(name = "TITLE_KOR", length = 1000)
    private String titleKor;

    @Column(name = "VOD_URL500K", length = 600)
    private String vodUrl500k;

    @Column(name = "VOD_URL1500K", length = 600)
    private String vodUrl1500k;

    @Column(name = "DURATION", length = 60)
    private String duration;






}
