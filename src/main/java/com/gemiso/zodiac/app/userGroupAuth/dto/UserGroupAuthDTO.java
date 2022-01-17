package com.gemiso.zodiac.app.userGroupAuth.dto;

import com.gemiso.zodiac.app.appAuth.dto.AppAuthDTO;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupAuthDTO {

    private Long id;

    private UserGroupDTO userGroup;

    private AppAuthDTO appAuth;
}
