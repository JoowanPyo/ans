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
public class DsProgramDTO {

    private String pgmCd;
    private String pgmNm;
    private String chanTp; //01:TV 02:라디오
    private String jenreClf1;//100 보도, 200:교양, 300:오락
    private String productClf;//100:자체제작, 200:외주제작, 300:국내구매, 400:해외구매, 500리패키지, 999:기타
    private String regDt; //입력일시
    private String updDt; //수정일시
    private String newsYn; //뉴스 여부
    /*private String chanId;*/
}
