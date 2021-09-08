package com.gemiso.zodiac.app.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthDTO {

    private Long id;
    private String token;
    private String userNm;
    private String deptId;
    private Date loginDtm;
    private String loginIp;
    private Date logoutDtm;
    private String stCd;
    private String clientVer;
    private String userId;

}
