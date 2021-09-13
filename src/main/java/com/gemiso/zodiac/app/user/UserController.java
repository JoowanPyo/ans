package com.gemiso.zodiac.app.user;

import com.gemiso.zodiac.app.user.dto.UserCreateDTO;
import com.gemiso.zodiac.app.user.dto.UserDTO;
import com.gemiso.zodiac.app.user.dto.UserUpdateDTO;
import com.gemiso.zodiac.core.response.ApiCollectionResponse;
import com.gemiso.zodiac.core.response.ApiResponse;
import com.gemiso.zodiac.exception.UserAlreadyExistException;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Log4j2
@RequiredArgsConstructor
//@Tag(name = "User Controllers", description = "User API")
@Api(description = "사용자 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "사용자 목록 조회", description = "조회조건으로 사용자 목록 조회")
    @GetMapping
    public ApiResponse<List<UserDTO>> findAll(@Parameter(name = "userId", description = "사용자 아이디", in = ParameterIn.QUERY) @RequestParam(value = "userId", required = false) String userId,
                                              @Parameter(name = "userNm", description = "사용자명", in = ParameterIn.QUERY) @RequestParam(value = "userNm", required = false) String userNm,
                                              @Parameter(name = "delYn", description = "삭제 여부", in = ParameterIn.QUERY) @RequestParam(value = "delYn", required = false) String delYn) {

        List<UserDTO> result = userService.findAll(userId, userNm, delYn);


        return new ApiResponse<>(result);
    }

    @Operation(summary = "사용자 상세정보 조회", description = "AccessToken으로 사용자 상세 정보 조회")
    @GetMapping(path = "/{userId}")
    public ApiResponse<UserDTO> find(@Parameter(name = "userId", description = "사용자 아이디") @PathVariable("userId") String userId) {

        UserDTO returnUser = userService.find(userId);

        return new ApiResponse<UserDTO>(returnUser);
    }


    /*@Operation(summary = "사용자 등록", description = "사용자 등록")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserDTO> postUser(
            @Parameter(name = "userDTO", required = true, description= "필수값<br> userNm , password") @RequestBody UserDTO userDTO
    ){
        userService.postUser(userDTO);

        UserDTO userDto= userService.getUser(userDTO.getUserId());

        return new ApiResponse<>(userDto);
    }*/
    @Operation(summary = "사용자 등록", description = "사용자 등록")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserDTO> create(
            @Parameter(name = "userCreateDTO", required = true, description = "필수값<br> userNm , password") @Valid @RequestBody UserCreateDTO userCreateDTO
    ) {

        String userId = userCreateDTO.getUserId();
        if (userService.checkUser(userId)) {
            //return ApiResponse.aleadyExist();
            throw new UserAlreadyExistException("사용자가 이미 존재합니다. UserId : " + userId);
        }

        userService.create(userCreateDTO);

        UserDTO userDto = userService.find(userId);

        return new ApiResponse<>(userDto);
    }

    @Operation(summary = "사용자 정보 수정", description = "사용자 정보 수정")
    @PutMapping(path = "/{userId}")
    public ApiResponse<UserDTO> update(@Parameter(name = "userDto", required = true, description = "필수값<br>") @Valid @RequestBody UserUpdateDTO userUpdateDTO,
                                       @Parameter(name = "userId", required = true) @PathVariable("userId") String userId) {


        userService.update(userUpdateDTO, userId);

        UserDTO userDTO = userService.find(userId);
        //UserDTO userDTO = userService.getUser(userId);

        return new ApiResponse(userDTO);
    }

    @Operation(summary = "사용자 삭제", description = "사용자 삭제")
    @DeleteMapping(path = "/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> delete(
            @Parameter(name = "userId", description = "사용자 아이디") @PathVariable("userId") String userId) {

        userService.delete(userId);

        return ApiResponse.noContent();
    }

}
