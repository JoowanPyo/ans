package com.gemiso.zodiac.app.tag.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagCreateDTO {

    //private Long tagId;
    private String tag;
    private Integer tagClicked;
}
