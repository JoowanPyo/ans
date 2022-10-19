package com.gemiso.zodiac.app.stats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.models.auth.In;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tb_stats"
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@DynamicUpdate
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "brdc_dt", length = 10)
    private String brdcDt;

    @Column(name = "broll_count")
    private Integer brollCount;

    @Column(name = "mng_count")
    private Integer mngCount;

    @Column(name = "telephone_count")
    private Integer telephoneCount;

    @Column(name = "news_studio_count")
    private Integer newsStudioCount;

    @Column(name = "smartphone_count")
    private Integer smartphoneCount;

    @Column(name = "empty_count")
    private Integer emptyCount;

    @Column(name = "apk_count")
    private Integer apkCount;

    @Column(name = "pk_count")
    private Integer pkCount;
}
