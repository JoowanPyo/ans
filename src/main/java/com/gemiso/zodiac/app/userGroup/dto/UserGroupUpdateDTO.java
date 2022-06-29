package com.gemiso.zodiac.app.userGroup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupUpdateDTO {

    private Long userGrpId;
    private String userGrpNm;
    private String memo;
    private int ord;
    private String useYn;
    //private String delYn;
    //private Date inputDtm;
    //private Date updtDtm;
    //private Date delDtm;
    //private String inputr;
    private String updtrId;
    //private String delrId;
    //private List<UserGroupAuthDTO> userGroupAuthDTOS = new ArrayList<>();
}
