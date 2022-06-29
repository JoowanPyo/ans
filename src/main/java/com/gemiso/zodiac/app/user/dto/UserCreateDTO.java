package com.gemiso.zodiac.app.user.dto;

import com.gemiso.zodiac.app.userGroupUser.dto.UserGroupUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {

    @NotBlank
    private String userId;
    @NotBlank
    private String userNm;
    @NotBlank
    private String pwd;
    private String emplNo;
    private String freeYn;
    private Long deptId;
    private String dutyCd;
    private String dutyCdNm;
    private String chiefYn;
    @Email
    private String email;
    private String tel;
    private String telPubYn;
    private String userDivCd;
    private String userDivCdNm;
    private String memo;
    private String rmk;
    //private Date lastLoginDtm;
    //private int loginErrCnt;
    //private Date pwdChgDtm;
    private String userStCd;
    //private String userStCdNm;
    private Date useStartDtm;
    private Date useEndDtm;
    private String useYn;
    //private String delYn;
    private String inputrId;
    //private String inputrNm;
    //private String updtrId;
    //private String updtrNm;
    //private String delrId;
    //private String delrNm;
    //private Date delDtm;
    private String inphonNo;
    private String salt;
    private String deptCd;
    private List<UserGroupUserDTO> userGroupUserDTO = new ArrayList<>();

   /* private String userId;
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
    private CodeSimpleDTO dutyCd;
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
    private CodeSimpleDTO userDivCd;
    private String userNm;
    private CodeSimpleDTO userStCd;
    private List<UserGroupUserDTO> userGroupUserDTO = new ArrayList<>();
*/
}
