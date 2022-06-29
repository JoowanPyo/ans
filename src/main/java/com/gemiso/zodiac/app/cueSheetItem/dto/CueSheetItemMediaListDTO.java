package com.gemiso.zodiac.app.cueSheetItem.dto;

import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaDTO;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CueSheetItemMediaListDTO {

    private List<ArticleMediaDTO> articleMediaDTO;
    private List<CueSheetMediaDTO> cueSheetMediaDTO;
}
