package com.gemiso.zodiac.app.userGroupAuth.dto;

import com.gemiso.zodiac.app.appAuth.dto.AppAuthDTO;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupAuthDTO {

    private Long id;

    private UserGroupDTO userGroup;

    private AppAuthDTO appAuth;
}
