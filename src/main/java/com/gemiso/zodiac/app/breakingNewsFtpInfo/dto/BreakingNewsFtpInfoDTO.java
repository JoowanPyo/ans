package com.gemiso.zodiac.app.breakingNewsFtpInfo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BreakingNewsFtpInfoDTO {

    private Long id;
    private String ftpIp;
    private Integer ftpPort;
    private String ftpId;
    private String ftpPw;
    private String ftpPath;
    private String ftpType;
}
