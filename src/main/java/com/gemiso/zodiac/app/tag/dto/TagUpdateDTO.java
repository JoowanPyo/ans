package com.gemiso.zodiac.app.tag.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagUpdateDTO {

    private Long tagId;
    private String tag;
    private Integer tagClicked;
}
