package com.gemiso.zodiac.app.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.userGroupUser.dto.UserGroupUserDTO;
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
public class UserDTO {

    private String userId;
    private String userNm;
    //private String pwd;
    private String emplNo;
    private String freeYn;
    private Long deptId;
    private String deptNm;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date lastLoginDtm;
    private Integer loginErrCnt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date pwdChgDtm;
    private String userStCd;
    private String userStCdNm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date useStartDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date useEndDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date inputDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updtDtm;
    private String useYn;
    private String delYn;
    private String inputrId;
    private String inputrNm;
    private String updtrId;
    private String updtrNm;
    private String delrId;
    private String delrNm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date delDtm;
    private String inphonNo;
    private String deptCd;
    //private String salt;
    private List<UserGroupUserDTO> userGroupUserDTO = new ArrayList<>();

    /*private String userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date inputDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updtDtm;
    private String chiefYn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date delDtm;
    private String delYn;
    private UserSimpleDTO delr;
    private String deptId;
    private CodeSimpleDTO dutyCd;
    private String email;
    private String emplNo;
    private String freeYn;
    private String inphonNo;
    private UserSimpleDTO inputr;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date lastLoginDtm;
    private int loginErrCnt;
    private String memo;
    private String pwd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date pwdChgDtm;
    private String rmk;
    private String salt;
    private String tel;
    private String telPubYn;
    private UserSimpleDTO updtr;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date useEndDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date useStartDtm;
    private String useYn;
    private CodeSimpleDTO userDivCd;
    private String userNm;
    private CodeSimpleDTO userStCd;
    private List<UserGroupUserDTO> userGroupUserDTO = new ArrayList<>();
*/


}
