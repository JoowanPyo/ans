package com.gemiso.zodiac.app.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserToGroupUdateDTO {

    private Long userGrpId;
}
