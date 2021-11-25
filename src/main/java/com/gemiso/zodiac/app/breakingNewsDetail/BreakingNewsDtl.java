package com.gemiso.zodiac.app.breakingNewsDetail;

import com.gemiso.zodiac.app.breakingNews.BreakingNews;
import com.gemiso.zodiac.app.scrollNews.ScrollNews;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tb_breaking_news_dtl")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "breakingNews")
@DynamicUpdate
public class BreakingNewsDtl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable = false )
    private Long id;

    @Column(name = "ord", nullable = false)
    private int ord;

    @Column(name = "ctt", length = 255, nullable = false)
    private String ctt;

    @ManyToOne
    @JoinColumn(name = "breaking_news_id")
    private BreakingNews breakingNews;
}
