package com.gemiso.zodiac.app.article.dto;

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
public class ArticleLockDTO {

    private String lckYn;
    private Date lckDtm;
    private UserSimpleDTO lckr;

}
