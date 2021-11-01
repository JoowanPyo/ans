package com.gemiso.zodiac.app.tag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagUpdateDTO {

    private Long tagId;
    private String tag;
    private int tagClicked;
}
