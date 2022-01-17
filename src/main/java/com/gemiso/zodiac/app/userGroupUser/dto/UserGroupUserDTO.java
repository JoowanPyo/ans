package com.gemiso.zodiac.app.userGroupUser.dto;

import com.gemiso.zodiac.app.user.dto.UserDTO;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupUserDTO {

    private Long id;

    private UserDTO user;

    private UserGroupDTO userGroup;
}
