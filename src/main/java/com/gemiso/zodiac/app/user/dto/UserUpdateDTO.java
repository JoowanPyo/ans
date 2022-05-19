package com.gemiso.zodiac.app.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.code.dto.CodeSimpleDTO;
import com.gemiso.zodiac.app.userGroupUser.UserGroupUser;
import com.gemiso.zodiac.app.userGroupUser.dto.UserToGroupUdateDTO;
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
public class UserUpdateDTO {

    private String userId;
    private String userNm;
    private String pwd;
    private String emplNo;
    private String freeYn;
    private Long deptId;
    private String dutyCd;
    private String dutyCdNm;
    private String chiefYn;
    private String email;
    private String tel;
    private String telPubYn;
    private String userDivCd;
    private String userDivCdNm;
    private String memo;
    private String rmk;
    //private Date lastLoginDtm;
    //private int loginErrCnt;
    private Date pwdChgDtm;
    private String userStCd;
    //private String userStCdNm;
    //private Date useStartDtm;
    //private Date useEndDtm;
    private String useYn;
    //private String delYn;
    //private String inputrId;
    //private String inputrNm;
    private String updtrId;
    //private String updtrNm;
    //private String delrId;
    //private String delrNm;
    //private Date delDtm;
    private String inphonNo;
    //private String salt;
    private String deptCd;
    /*private List<UserGroupUserDTO> userGroupUserDTO = new ArrayList<>();*/

    private List<UserToGroupUdateDTO> userGroupUser = new ArrayList<>();
}
