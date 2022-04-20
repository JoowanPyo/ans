package com.gemiso.zodiac.app.lbox.userInfoDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoDTO {

    private Long id;
    private String account_id;
    private String name;
    private String dept;
    private String dept_code;
    private String post;
}
