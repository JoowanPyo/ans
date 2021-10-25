package com.gemiso.zodiac.app.userGroup.dto;

import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
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
public class UserGroupDTO {


    private Long userGrpId;
    private String userGrpNm;
    private String memo;
    private int ord;
    private String useYn;
    private String delYn;
    private Date inputDtm;
    private Date updtDtm;
    private Date delDtm;
    private String inputrId;
    private String inputrNm;
    private String updtrId;
    private String updtrNm;
    private String delrId;
    private String delrNm;
    private List<UserGroupAuthDTO> userGroupAuthDTO = new ArrayList<>();


}
