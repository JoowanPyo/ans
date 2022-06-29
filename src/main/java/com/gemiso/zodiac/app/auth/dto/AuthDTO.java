package com.gemiso.zodiac.app.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.appAuth.dto.AppAuthUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthDTO {

    private Long id;
    private String token;
    private String userNm;
    private Long deptId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date loginDtm;
    private String loginIp;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date logoutDtm;
    private String stCd;
    private String stCdNm;
    private String clientVer;
    private String userId;

    List<AppAuthUserDTO> appAuthUser = new ArrayList<>();
    /*private List<UserGroupDTO> userGroupDTO = new ArrayList<>();
    private List<AppAuthDTO> appAuthDTO = new ArrayList<>();*/
}
