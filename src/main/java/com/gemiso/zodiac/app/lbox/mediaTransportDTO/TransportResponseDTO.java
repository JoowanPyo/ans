package com.gemiso.zodiac.app.lbox.mediaTransportDTO;

import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportResponseDTO {


    private ArticleMediaDTO articleMediaDTO;
    private List<TransportFaildDTO> transportFaild;
    private Boolean clipInfo;
}
