package com.gemiso.zodiac.app.lbox.categoriesDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gemiso.zodiac.app.lbox.contentDTO.ContentsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoriesDataDTO {

    private Integer status;
    private Boolean success;
    private List<CategoriesDTO> data = new ArrayList<>();
}
