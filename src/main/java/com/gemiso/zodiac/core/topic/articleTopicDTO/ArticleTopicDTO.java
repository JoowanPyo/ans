package com.gemiso.zodiac.core.topic.articleTopicDTO;

import com.gemiso.zodiac.app.article.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTopicDTO {

    private String eventId;
    private Long artclId;
    private Object article;
}
