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
public class DsBasicScheduleListDTO {

    private String basicScheduleId; //기본편성표아이디 bas_pgmsch_id
    private String broadBeginYmd; //방송시작일자 brdc_start_dt
    private String broadEndYmd; //방송종료일자 brdc_end_dt
    private String broadHm; //방송시각 brdc_start_clk
    private String broadRun; //방송길이 brdc_end_clk
    private String pgmCd; //방송프로그램 코드 brdc_pgm_id
}
