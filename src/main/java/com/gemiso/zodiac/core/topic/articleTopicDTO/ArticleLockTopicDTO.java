package com.gemiso.zodiac.core.topic.articleTopicDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLockTopicDTO extends ArticleTopicDTO{

    //private ArticleTopicDTO header;
    private String msg;
}