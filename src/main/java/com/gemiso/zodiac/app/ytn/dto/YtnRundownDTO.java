package com.gemiso.zodiac.app.ytn.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YtnRundownDTO {

    private Long id;
    private String contId;
    private String brdcDtm;
    private String brdcStartDtm;
    private String brdcEndDtm;
    private String ord;
    private String frm;
    private String mc;
    private String rprt;
    private String video;
    private String time;
    private String article;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date inputDtm;
    private String title;
}
