package com.gemiso.zodiac.app.elasticsearch.articleDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "엘라스틱 기사 mediaDTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElasricSearchArticleMediaDTO {



    private Long artclMediaId;

    private Integer mediaDurtn;
}
