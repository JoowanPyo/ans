package com.gemiso.zodiac.app.ytn.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YtnRowDTO {

    private String NO; //순번

    private String MC; //?? ex)MC

    private String TITLE; //제목

    private String FORM; //형식 ex) 리포트

    private String REPORTER; //기자

    private String VIDEO_CNT; //영상? ex) 1

    private String TIME; //기사소요시간

    private String ARTICLE; //기사내용
}
