package com.gemiso.zodiac.app.file.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
//@ApiModel("statusCodeDTO Entry")
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusCodeFileDTO {


    //@ApiModelProperty(value = "코드", required = false)
    private int code;

    //@ApiModelProperty(value = "메세지", required = false)
    private String message;

    //@ApiModelProperty(value = "파일아이디", required = false)
    private Long fileId;

    //@ApiModelProperty(value = "파일이름", required = false)
    private String filename;
}
