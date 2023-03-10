package com.gemiso.zodiac.app.userGroup;

import com.gemiso.zodiac.app.userGroup.dto.UserGroupCreateDTO;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupDTO;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupUpdateDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.gemiso.zodiac.core.service.JwtGetUserService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;

//@Tag(name = "userGroup", description = "사용자 그룹 API")
@Api(description = "그룹 API")
@RestController
@RequestMapping("/usergroups")
@Slf4j
@RequiredArgsConstructor
/*@CrossOrigin(origins = "*", allowedHeaders = "*")*/
public class UserGroupController {

    private final UserGroupService userGroupService;

    private final JwtGetUserService jwtGetUserService;

    @Operation(summary = "사용자 그룹 목록조회", description = "사용자 그룹 목록조회")
    @GetMapping(path = "")
    @CrossOrigin(origins = "*")
    public AnsApiResponse<List<UserGroupDTO>> findAll(@Parameter(name = "userGrpNm", description = "그룹 명")
                                                      @RequestParam(value = "userGrpNm", required = false) String userGrpNm,
                                                      @Parameter(name = "useYn", description = "사용 여부")
                                                      @RequestParam(value = "useYn", required = false) String useYn) {

        List<UserGroupDTO> userGroupUserDtoList = userGroupService.findAll(userGrpNm, useYn);

        return new AnsApiResponse<>(userGroupUserDtoList);
    }

    @Operation(summary = "사용자 그룹 상세 조회", description = "사용자 그룹 상세 조회")
    @GetMapping(path = "/{userGrpId}")
    public AnsApiResponse<UserGroupDTO> find(@Parameter(name = "userGrpId", description = "그룹 아이디")
                                             @PathVariable("userGrpId") Long userGrpId) {

        UserGroupDTO userGroupDTO = userGroupService.find(userGrpId);

        if (ObjectUtils.isEmpty(userGroupDTO)) {
            throw new ResourceNotFoundException("유저 그룹을 찾을 수 없습니다. 유저 그룹 아이디 : " + userGrpId);
        }

        return new AnsApiResponse<>(userGroupDTO);
    }

    @Operation(summary = "사용자 그룹 등록", description = "사용자 그룹 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<UserGroupDTO> create(
            @Parameter(description = "필수값<br> ", required = true) @RequestBody @Valid UserGroupCreateDTO userGroupCreateDTO,
            @RequestHeader(value = "Authorization", required = false) String Authorization
    ) throws Exception {

        String userId = jwtGetUserService.getUser(Authorization);

        UserGroupDTO userGroupDTO = userGroupService.create(userGroupCreateDTO, userId);

        return new AnsApiResponse<UserGroupDTO>(userGroupDTO);
    }

    @Operation(summary = "사용자 그룹 정보 수정", description = "사용자 그룹 정보 수정")
    @PutMapping(path = "/{userGrpId}")
    public AnsApiResponse<UserGroupDTO> update(@Parameter(name = "userDto", required = true, description = "필수값<br>")
                                               @Valid @RequestBody UserGroupUpdateDTO userGroupUpdateDTO,
                                               @Parameter(name = "userGrpId", required = true) @PathVariable("userGrpId") Long userGrpId,
                                               @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        String userId =jwtGetUserService.getUser(Authorization);

        UserGroupDTO userGroupIdCheck = userGroupService.find(userGrpId);
        if (ObjectUtils.isEmpty(userGroupIdCheck)) {
            throw new ResourceNotFoundException("유저 그룹을 찾을 수 없습니다. 유저 그룹 아이디 : " + userGrpId);
        }

        userGroupService.update(userGroupUpdateDTO, userGrpId, userId);

        UserGroupDTO userGroupDTO = userGroupService.find(userGrpId);

        return new AnsApiResponse<>(userGroupDTO);
    }

    @Operation(summary = "사용자 삭제", description = "사용자 삭제")
    @DeleteMapping(path = "/{userGrpId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(
            @Parameter(name = "userGrpId", description = "사용자 아이디") @PathVariable("userGrpId") Long userGrpId,
            @RequestHeader(value = "Authorization", required = false)String Authorization) throws Exception {

        String userId =jwtGetUserService.getUser(Authorization);

        UserGroupDTO userGroupIdCheck = userGroupService.find(userGrpId);
        if (ObjectUtils.isEmpty(userGroupIdCheck)) {
            throw new ResourceNotFoundException("유저 그룹을 찾을 수 없습니다. 유저 그룹 아이디 : " + userGrpId);
        }

        userGroupService.delete(userGrpId, userId);

        return AnsApiResponse.noContent();

    }


}
