package com.gemiso.zodiac.app.lbox.contentDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataDTO {

    private Integer status;
    private Boolean success;
    private List<ContentsDTO> data = new ArrayList<>();
    private PageDTO pagination;
}
