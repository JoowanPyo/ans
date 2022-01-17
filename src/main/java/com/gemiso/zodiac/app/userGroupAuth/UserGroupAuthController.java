package com.gemiso.zodiac.app.userGroupAuth;

import com.gemiso.zodiac.app.userGroupAuth.dto.UserGroupAuthCreateDTO;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "유저그룹 권한 추가,수정 API")
@RestController
@RequestMapping("/userGroups")
@Slf4j
@RequiredArgsConstructor
public class UserGroupAuthController {

    private final UserGroupAuthService userGroupAuthService;

    @Operation(summary = "사용자그룹 권한 등록", description = "사용자그룹 권한 등록")
    @PostMapping(path = "/{userGrpId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<UserGroupDTO> create(@Parameter(name = "userGroupAuthCreateDTOList", required = true) @RequestBody List<UserGroupAuthCreateDTO> userGroupAuthCreateDTOList,
                                               @Parameter(name = "userGrpId", required = true) @PathVariable("userGrpId") Long userGrpId) {

        userGroupAuthService.create(userGroupAuthCreateDTOList, userGrpId);

        UserGroupDTO userGroupDTO = userGroupAuthService.find(userGrpId);

        return new AnsApiResponse<>(userGroupDTO);
    }
}
