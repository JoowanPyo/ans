package com.gemiso.zodiac.app.fileFtpInfo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileFtpInfoCreateDTO {

    //private Long id;
    private String ftpIp;
    private Integer ftpPort;
    private String ftpId;
    private String ftpPw;
    private String ftpPath;
    private String ftpType;
    private String ftpDiv;
}
