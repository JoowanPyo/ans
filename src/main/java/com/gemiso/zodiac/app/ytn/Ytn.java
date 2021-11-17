package com.gemiso.zodiac.app.ytn;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        name = "tb_ytn"
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
@DynamicUpdate
@EntityListeners(value = {AuditingEntityListener.class})
public class Ytn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "brdc_dtm", nullable = false)
    private Date brdcDtm;

    @Column(name = "brdc_start_dtm", nullable = false)
    private Date brdcStartDtm;

    @Column(name = "brdc_end_dtm", nullable = false)
    private Date brdcEndDtm;

    @Column(name = "number")
    private int number;

    @Column(name = "form", length = 20)
    private String form;

    @Column(name = "mc", length = 20)
    private String mc;

    @Column(name = "reporter", length = 50)
    private String reporter;

    @Column(name = "video")
    private int video;

    @Column(name = "time", length = 20)
    private String time;

    @Column(name = "article", columnDefinition = "text")
    private String article;

    @CreatedDate
    @Column(name = "input_dtm")
    private Date inputDtm;

}
