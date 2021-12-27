package com.gemiso.zodiac.core.scheduling.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DschWeekDTO {

    private String broadYmd; //방송일자 -brdc_dt
    private String broadHm; //방송시각 -brdc_start_time
    private String broadRun; //방송길이 -brdc_end_clk
    private String pgmCd; //프로그램코드 -brdc_pgm_id
    private String regDt; //입력일시
    private String updDt; //수정일시
}
