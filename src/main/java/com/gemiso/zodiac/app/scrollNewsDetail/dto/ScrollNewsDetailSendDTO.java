package com.gemiso.zodiac.app.scrollNewsDetail.dto;

import com.gemiso.zodiac.app.scrollNewsDetail.ScrollNewsDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScrollNewsDetailSendDTO {

    private List<ScrollNewsDetailSendListDTO> list;
}
