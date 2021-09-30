package com.gemiso.zodiac.app.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {

    private String userId;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    //private Date inputDtm;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    //private Date updtDtm;
    private String chiefYn;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    //private Date delDtm;
    //private String delYn;
    //private String delrId;
    private String deptId;
    private String dutyCd;
    private String email;
    private String emplNo;
    private String freeYn;
    private String inphonNo;
    private UserSimpleDTO inputr;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    //private Date lastLoginDtm;
    //private int loginErrCnt;
    private String memo;
    private String pwd;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    //private Date pwdChgDtm;
    private String rmk;
    private String salt;
    private String tel;
    private String telPubYn;
   //private String updtrId;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    //private Date useEndDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date useStartDtm;
    private String useYn;
    private String userDivCd;
    private String userNm;
    private String userStCd;
    private List<UserGroupUserDTO> userGroupUserDTO = new ArrayList<>();

}
