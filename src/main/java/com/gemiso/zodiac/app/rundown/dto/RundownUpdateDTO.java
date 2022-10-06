package com.gemiso.zodiac.app.rundown.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RundownUpdateDTO {

    private Long rundownId;
    private Date rundownDt; // 런다운 일시
    private String rundownTime; // 회의시간 ( 오전 /오후 )
    private String rundownData; // 회의자료
    private String headline; // 헤드라인
    //private String inputrId; // 작성자
    //private String inputrNm;
    private String updtrId;
    //private String updtrNm;
}
