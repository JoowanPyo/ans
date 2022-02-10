package com.gemiso.zodiac.app.userGroupAuth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupAuthCreateDTO {

    private Long appAuthId;
}