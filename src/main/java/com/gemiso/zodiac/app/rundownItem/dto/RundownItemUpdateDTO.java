package com.gemiso.zodiac.app.rundownItem.dto;

import com.gemiso.zodiac.app.rundown.dto.RundownSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RundownItemUpdateDTO {

    private Long rundownItemId;
    private Date brdcDt; // 방송일
    private String rundownTime; // 회의시간 ( 오전 /오후 )
    private String artclFrmCd;// 기사형식
    private String brdcStartTime; // 방송시간 ( 08 두자릿수 )
    private String rptrId; //기자 아이디
    private String rundownTitl; // 제목
    private String rundownAddData; // 추가내용
    private String publicationCd; //기사화
    //private Integer rundownItemOrd; // 순번
    private RundownSimpleDTO rundown;
}
