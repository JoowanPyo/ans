package com.gemiso.zodiac.app.ytn.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YtnRundownDTO {

    private Long id;
    private String contId;
    private String brdcDtm;
    private String brdcStartDtm;
    private String brdcEndDtm;
    private int number;
    private String form;
    private String mc;
    private String reporter;
    private String video;
    private String time;
    private String article;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date inputDtm;
}
