package com.gemiso.zodiac.app.articleMedia.dto;

import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import com.gemiso.zodiac.app.code.dto.CodeSimpleDTO;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleMediaCreateDTO {

    //private Long artclMediaId;
    private String mediaTypCd;
    //private String mediaTypCdNm;
    private int mediaOrd;
    private int contId;
    private String trnsfFileNm;
    private String mediaDurtn;
    private Date mediaMtchDtm;
    private String trnsfStCd;
    //private String trnsfStCdNm;
    private String assnStCd;
    //private String assnStCdNm;
    private String videoEdtrNm;
    //private String delYn;
    //private Date delDtm;
    private Date inputDtm;
    //private Date updtDtm;
    private String inputrId;
    //private String inputrNm;
    //private String updtrId;
    //private String updtrNm;
    //private String delrId;
    //private String delrNm;
    private String videoEdtrId;
    private ArticleSimpleDTO article;
}
