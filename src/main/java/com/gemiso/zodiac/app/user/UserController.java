package com.gemiso.zodiac.app.user;

import com.gemiso.zodiac.app.user.dto.UserCreateDTO;
import com.gemiso.zodiac.app.user.dto.UserDTO;
import com.gemiso.zodiac.app.user.dto.UserDeleteUpdateDTO;
import com.gemiso.zodiac.app.user.dto.UserUpdateDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.gemiso.zodiac.exception.UserAlreadyExistException;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Api(description = "사용자 API")
@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "사용자 목록 조회", description = "조회조건으로 사용자 목록 조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<UserDTO>> findAll(@Parameter(name = "userId", description = "사용자 아이디", in = ParameterIn.QUERY)
                                                 @RequestParam(value = "userId", required = false) String userId,
                                                 @Parameter(name = "userNm", description = "사용자명", in = ParameterIn.QUERY)
                                                 @RequestParam(value = "userNm", required = false) String userNm,
                                                 @Parameter(name = "searchWord", description = "검색키워드")
                                                 @RequestParam(value = "searchWord", required = false) String searchWord,
                                                 @Parameter(name = "email", description = "이메일")
                                                 @RequestParam(value = "email", required = false) String email,
                                                 @Parameter(name = "delYn", description = "삭제 여부", in = ParameterIn.QUERY)
                                                 @RequestParam(value = "delYn", required = false) String delYn) {

        List<UserDTO> result = userService.findAll(userId, userNm, searchWord, email, delYn);


        return new AnsApiResponse<>(result);
    }

    @Operation(summary = "사용자 상세정보 조회", description = "AccessToken으로 사용자 상세 정보 조회")
    @GetMapping(path = "/{userId}")
    public AnsApiResponse<UserDTO> find(@Parameter(name = "userId", description = "사용자 아이디") @PathVariable("userId") String userId) {

        UserDTO returnUser = userService.find(userId);

        return new AnsApiResponse<UserDTO>(returnUser);
    }


    /*@Operation(summary = "사용자 등록", description = "사용자 등록")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<UserDTO> postUser(
            @Parameter(name = "userDTO", required = true, description= "필수값<br> userNm , password") @RequestBody UserDTO userDTO
    ){
        userService.postUser(userDTO);

        UserDTO userDto= userService.getUser(userDTO.getUserId());

        return new AnsApiResponse<>(userDto);
    }*/
    @Operation(summary = "사용자 등록", description = "사용자 등록")
    @PostMapping(path = "/createuser")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<UserDTO> create(
            @Parameter(name = "userCreateDTO", required = true, description = "필수값<br> userNm , password") @Valid @RequestBody UserCreateDTO userCreateDTO
    ) throws NoSuchAlgorithmException {

        String userId = userCreateDTO.getUserId();
        if (userService.checkUser(userId)) {
            //return AnsApiResponse.aleadyExist();
            User user = userService.deleteChkUserFind(userId);
            
            if (ObjectUtils.isEmpty(user)){
                throw new UserAlreadyExistException("사용자가 이미 존재합니다. 사용자 아이디 : "+userId);

            }else {
                throw new UserAlreadyExistException("삭제된 사용자입니다. 사용자 아이디 : "+userId);
            }
        }

        userService.create(userCreateDTO);

        UserDTO userDto = userService.find(userId);

        return new AnsApiResponse<>(userDto);
    }

    @Operation(summary = "사용자 정보 수정", description = "사용자 정보 수정")
    @PutMapping(path = "/{userId}")
    public AnsApiResponse<UserDTO> update(@Parameter(name = "userDto", required = true, description = "필수값<br>") @Valid @RequestBody UserUpdateDTO userUpdateDTO,
                                          @Parameter(name = "userId", required = true) @PathVariable("userId") String userId) throws NoSuchAlgorithmException {


        userService.update(userUpdateDTO, userId);

        UserDTO userDTO = userService.find(userId);
        //UserDTO userDTO = userService.getUser(userId);

        return new AnsApiResponse(userDTO);
    }

    @Operation(summary = "사용자 삭제", description = "사용자 삭제")
    @DeleteMapping(path = "/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(
            @Parameter(name = "userId", description = "사용자 아이디") @PathVariable("userId") String userId) {

        userService.delete(userId);

        return AnsApiResponse.noContent();
    }

    @Operation(summary = "사용자 비밀번호 확인", description = "사용자 비밀번호 확인")
    @GetMapping(path = "/confirm")
    public AnsApiResponse<?> passwordConfirm(@Parameter(name = "comfirmPwd", description = "확인 패스워드", in = ParameterIn.QUERY)
                                             @RequestParam(value = "comfirmPwd", required = false) String comfirmPwd) throws NoSuchAlgorithmException {

        userService.passwordConfirm(comfirmPwd);

        return AnsApiResponse.ok();
    }

    @Operation(summary = "삭제된 사용자 복구", description = "삭제된 사용자 복구")
    @PutMapping(path = "/{userId}/delete")
    public AnsApiResponse<UserDTO> deleteUserUpdate(@Parameter(name = "userDto", required = true, description = "필수값<br>") @Valid @RequestBody UserDeleteUpdateDTO userDeleteUpdateDTO,
                                                    @Parameter(name = "userId", required = true) @PathVariable("userId") String userId) throws NoSuchAlgorithmException {

        userService.deleteUserUpdate(userDeleteUpdateDTO, userId);

        UserDTO userDto = userService.find(userId);

        return new AnsApiResponse<>(userDto);
    }

}
