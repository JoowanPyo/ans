package com.gemiso.zodiac.app.articleMedia.dto;

import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleMediaSimpleDTO {

    private Long artclMediaId;
    private String mediaTypCd;
    private int mediaOrd;
    private int contId;
    private String trnsfFileNm;
    private String mediaDurtn;
    private Date mediaMtchDtm;
    private String trnsfStCd;
    private String assnStCd;
    private String videoEdtrNm;
    private String delYn;
    private Date delDtm;
    private Date inputDtm;
    private Date updtDtm;
    private String inputrId;
    private String updtrId;
    private String delrId;
    private String videoEdtrId;
    //private ArticleDTO article;
}
