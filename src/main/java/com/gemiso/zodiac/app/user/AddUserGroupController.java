package com.gemiso.zodiac.app.user;

import com.gemiso.zodiac.app.user.dto.UserDTO;
import com.gemiso.zodiac.app.user.dto.UserGroupUserDTO;
import com.gemiso.zodiac.app.user.dto.UserGroupUserDeleteDTO;
import com.gemiso.zodiac.app.user.dto.UserToGroupUdateDTO;
import com.gemiso.zodiac.core.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addUserGroups")
@Log4j2
@RequiredArgsConstructor
//@Tag(name = "User Controllers", description = "User API")
@Api(description = "사용자 그룹 추가 API")
public class AddUserGroupController {

    private final AddUserGroupService addUserGroupService;

    @Operation(summary = "그룹 사용자 조회", description = "그룹에 등록되어 있는 사용자 목록 조회")
    @GetMapping(path = "/{userGrpId}")
    public ApiResponse<List<UserDTO>> find(@Parameter(name = "userGrpId", required = true)@PathVariable("userGrpId")Long userGrpId){

        List<UserDTO> userDTOList = addUserGroupService.find(userGrpId);

        return new ApiResponse<>(userDTOList);
    }

    @Operation(summary = "사용자 그룹등록", description = "사용자 그룹등록")
    @PostMapping(path = "/{userId}")
    public ApiResponse<UserDTO> create(@Parameter(name = "userId", required = true) @PathVariable("userId") String userId,
                                              @Parameter(name = "userGrpId")@RequestParam(value = "userGrpId", required = true)Long userGrpId){

        addUserGroupService.create(userId, userGrpId);

        UserDTO returnUser = addUserGroupService.findUser(userId);

        return new ApiResponse<>(returnUser);
    }
    @Operation(summary = "사용자 그룹수정", description = "사용자 그룹수정")
    @PutMapping(path = "/{userId}")
    public ApiResponse<UserDTO> update(
            @Parameter(name = "userDto", required = true, description = "필수값<br>") @RequestBody List<UserToGroupUdateDTO> userToGroupUdateDTOList,
            @Parameter(name = "userId", required = true) @PathVariable("userId") String userId) {


        addUserGroupService.update(userToGroupUdateDTOList, userId);

        UserDTO returnUser = addUserGroupService.findUser(userId);

        return new ApiResponse<>(returnUser);

    }

    @Operation(summary = "그룹 사용자 수정", description = "그룹에 등록되어 있는 사용자 목록 수정")
    @DeleteMapping(path = "/{userGrpId}")
    public ApiResponse<List<UserDTO>> delete(
            @Parameter(name = "userDto", required = true, description = "필수값<br>") @RequestBody List<UserGroupUserDeleteDTO> userGroupUserDeleteDTOS,
            @Parameter(name = "userGrpId", required = true) @PathVariable("userGrpId") Long userGrpId){

        addUserGroupService.delete(userGroupUserDeleteDTOS, userGrpId);

        return null;
    }
}
