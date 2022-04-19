package com.gemiso.zodiac.app.lbox.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageDTO {

    private Integer count;
    private Integer total;
    private Integer perPage;
    private Integer currentPage;
    private Integer totalPages;
}
