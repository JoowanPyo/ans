package com.gemiso.zodiac.app.code.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeOrdUpdateDTO {

    private String hrnkCdId;
    private Integer cdOrd;
}
