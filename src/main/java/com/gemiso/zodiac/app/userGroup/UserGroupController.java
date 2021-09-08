package com.gemiso.zodiac.app.userGroup;

import com.gemiso.zodiac.app.userGroup.dto.UserGroupCreateDTO;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupDTO;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupUpdateDTO;
import com.gemiso.zodiac.core.response.ApiResponse;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

//@Tag(name = "userGroup", description = "사용자 그룹 API")
@Api(description = "그룹 API")
@RestController
@RequestMapping("/userGroups")
@Log4j2
@RequiredArgsConstructor
public class UserGroupController {

    private final UserGroupService userGroupService;

    @Operation(summary = "사용자 그룹 목록조회", description = "사용자 그룹 목록조회")
    @GetMapping
    public ApiResponse<List<UserGroupDTO>> findAll(@Parameter(name = "userGrpNm", description = "그룹 명", in = ParameterIn.QUERY) @RequestParam(value = "userGrpNm", required = false) String userGrpNm,
                                                   @Parameter(name = "useYn", description = "사용 여부", in = ParameterIn.QUERY) @RequestParam(value = "useYn", required = false) String useYn) {

        List<UserGroupDTO> userGroupUserDtoList = userGroupService.findAll(userGrpNm, useYn);

        return new ApiResponse<>(userGroupUserDtoList);
    }

    @Operation(summary = "사용자 그룹 상세 조회", description = "사용자 그룹 상세 조회")
    @GetMapping(path = "/{userGrpId}")
    public ApiResponse<UserGroupDTO> find(@PathVariable Long userGrpId) {

        UserGroupDTO userGroupDTO = userGroupService.find(userGrpId);

        if (ObjectUtils.isEmpty(userGroupDTO)) {
            throw new ResourceNotFoundException("UserGroupId not found. userGroupId" + userGrpId);
        }

        return new ApiResponse<>(userGroupDTO);
    }

    @Operation(summary = "사용자 그룹 등록", description = "사용자 그룹 등록")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserGroupDTO> create(
            @Parameter(description = "필수값<br> ", required = true) @RequestBody UserGroupCreateDTO userGroupCreateDTO
    ) {

        UserGroupDTO userGroupDTO = userGroupService.create(userGroupCreateDTO);

        return new ApiResponse<UserGroupDTO>(userGroupDTO);
    }

    @Operation(summary = "사용자 그룹 정보 수정", description = "사용자 그룹 정보 수정")
    @PutMapping(path = "/{userGrpId}")
    public ApiResponse<UserGroupDTO> update(@Parameter(name = "userDto", required = true, description = "필수값<br>")
                                            @Valid @RequestBody UserGroupUpdateDTO userGroupUpdateDTO,
                                            @Parameter(name = "userGrpId", required = true) @PathVariable("userGrpId") Long userGrpId) {

        UserGroupDTO userGroupIdCheck = userGroupService.find(userGrpId);
        if (ObjectUtils.isEmpty(userGroupIdCheck)) {
            throw new ResourceNotFoundException("UserGroupId not found. userGroupId" + userGrpId);
        }

        userGroupService.update(userGroupUpdateDTO, userGrpId);

        UserGroupDTO userGroupDTO = userGroupService.find(userGrpId);

        return new ApiResponse<>(userGroupDTO);
    }

    @Operation(summary = "사용자 삭제", description = "사용자 삭제")
    @DeleteMapping(path = "/{userGrpId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> delete(
            @Parameter(name = "userGrpId", description = "사용자 아이디") @PathVariable("userGrpId") Long userGrpId) {

        UserGroupDTO userGroupIdCheck = userGroupService.find(userGrpId);
        if (ObjectUtils.isEmpty(userGroupIdCheck)) {
            throw new ResourceNotFoundException("UserGroupId not found. userGroupId" + userGrpId);
        }


        userGroupService.delete(userGrpId);

        return ApiResponse.noContent();

    }


}
