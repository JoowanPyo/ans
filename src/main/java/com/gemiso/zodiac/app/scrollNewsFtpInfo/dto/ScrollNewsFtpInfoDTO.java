package com.gemiso.zodiac.app.scrollNewsFtpInfo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScrollNewsFtpInfoDTO {

    private Long id;
    private String ftpIp;
    private Integer ftpPort;
    private String ftpId;
    private String ftpPw;
    private String ftpPath;
    private String ftpType;
}
