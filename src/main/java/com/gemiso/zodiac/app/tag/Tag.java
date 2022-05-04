package com.gemiso.zodiac.app.tag;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(
        name = "tb_tags",
        indexes = { @Index(name = "index_tags_tag", columnList = "tag")}
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //json 파싱때 필드가 없는 클래스를 Serialize문제 설정
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id", nullable = false)
    private Long tagId;

    @Column(name = "tag", length = 100 ,nullable = false)
    private String tag;

    @Column(name = "tag_clicked")
    private int tagClicked;
}
