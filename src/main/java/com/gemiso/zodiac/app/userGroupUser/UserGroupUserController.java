package com.gemiso.zodiac.app.userGroupUser;

import com.gemiso.zodiac.app.user.dto.UserDTO;
import com.gemiso.zodiac.app.userGroupUser.dto.UserGroupUserDTO;
import com.gemiso.zodiac.app.userGroupUser.dto.UserGroupUserDeleteDTO;
import com.gemiso.zodiac.app.userGroupUser.dto.UserToGroupUdateDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Tag(name = "MisUser Controllers", description = "MisUser API")
@Api(description = "사용자 그룹 추가 API")
@RestController
@RequestMapping("/addUserGroups")
@Slf4j
@RequiredArgsConstructor
public class UserGroupUserController {

    private final UserGroupUserService userGroupUserService;

    @Operation(summary = "그룹 사용자 조회", description = "그룹에 등록되어 있는 사용자 목록 조회")
    @GetMapping(path = "/{userGrpId}")
    public AnsApiResponse<List<UserDTO>> find(@Parameter(name = "userGrpId", required = true) @PathVariable("userGrpId") Long userGrpId) {

        List<UserDTO> userDTOList = userGroupUserService.find(userGrpId);

        return new AnsApiResponse<>(userDTOList);
    }

    @Operation(summary = "그룹 사용자 조회", description = "그룹에 등록되어 있는 사용자 목록 조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<UserGroupUserDTO>> findAll(@Parameter(name = "userId", required = true)
                                                       @RequestParam(value = "userId", required = true) Long userGrpId) {

        List<UserGroupUserDTO> userDTOList = userGroupUserService.findAll(userGrpId);

        return new AnsApiResponse<>(userDTOList);

    }

    @Operation(summary = "사용자 그룹등록", description = "사용자 그룹등록")
    @PostMapping(path = "/{userGrpId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<List<UserGroupUserDTO>> create(@Parameter(name = "userGrpId", required = true) @PathVariable("userGrpId") Long userGrpId,
                                                         @Parameter(name = "userId", required = true) @RequestParam(value = "userId", required = true) List<String> userIds) {

        userGroupUserService.create(userIds, userGrpId);

        List<UserGroupUserDTO> returnUser = userGroupUserService.findAll(userGrpId);

        return new AnsApiResponse<>(returnUser);
    }

    @Operation(summary = "사용자 그룹수정", description = "사용자 그룹수정")
    @PutMapping(path = "/{userId}")
    public AnsApiResponse<UserDTO> update(
            @Parameter(name = "userDto", required = true, description = "필수값<br>") @RequestBody List<UserToGroupUdateDTO> userToGroupUdateDTOList,
            @Parameter(name = "userId", required = true) @PathVariable("userId") String userId) {


        userGroupUserService.update(userToGroupUdateDTOList, userId);

        UserDTO returnUser = userGroupUserService.findUser(userId);

        return new AnsApiResponse<>(returnUser);

    }

    @Operation(summary = "그룹 사용자 삭제", description = "그룹에 등록되어 있는 사용자 삭제")
    @DeleteMapping(path = "/{userGrpId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<List<UserDTO>> delete(
            @Parameter(name = "userDto", required = true, description = "필수값<br>") @RequestBody List<UserGroupUserDeleteDTO> userGroupUserDeleteDTOS,
            @Parameter(name = "userGrpId", required = true) @PathVariable("userGrpId") Long userGrpId) {

        userGroupUserService.delete(userGroupUserDeleteDTOS, userGrpId);

        return AnsApiResponse.noContent();
    }
}
