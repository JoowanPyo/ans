package com.gemiso.zodiac.app.auth;


import com.gemiso.zodiac.app.auth.dto.AuthDTO;
import com.gemiso.zodiac.app.auth.dto.AuthRequestDTO;
import com.gemiso.zodiac.app.auth.dto.JwtDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(description = "로그인 API")
@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    public static final int MAX_ATTEMPTS = 10;

    @Operation(summary = "사용자 로그인", description = "사용자 로그인")
    @PostMapping(path = "/login")//login
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<JwtDTO> login(@Parameter(name = "loginDTO", required = true, description = "필수값<br> userId , password")
                                        @Valid @RequestBody AuthRequestDTO authRequestDTO
    ) throws Exception {

        JwtDTO jwtDTO = new JwtDTO();
        //log.info(" 로그인 : "+ authRequestDTO.toString());
        /********시큐어 코딩 인증 가이드 **********/
        String id = authRequestDTO.getUserId();
        String pw = authRequestDTO.getPassword();
        String ok = "FAIL";
        int count = 0;

        while ("FAIL".equals(ok) && (count < MAX_ATTEMPTS)) {
            ok = authService.verifyUser(id, pw);
            count++;
        }

        if ("OK".equals(ok)) {
            jwtDTO = authService.login(authRequestDTO);
        } else {
            return new AnsApiResponse<>(jwtDTO);
        }


        return new AnsApiResponse<>(jwtDTO);
    }

    @Operation(summary = "사용자 로그인 [모바일]", description = "사용자 로그인 [모바일]")
    @PostMapping(path = "/loginmobile")//login
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<JwtDTO> loginMobile(@Parameter(name = "loginDTO", required = true, description = "필수값<br> userId , password")
                                              @Valid @RequestBody AuthRequestDTO authRequestDTO
    ) throws Exception {

        JwtDTO jwtDTO = new JwtDTO();
        //log.info(" 로그인 : "+ authRequestDTO.toString());
        /********시큐어 코딩 인증 가이드 **********/
        String id = authRequestDTO.getUserId();
        String pw = authRequestDTO.getPassword();
        String ok = "FAIL";
        int count = 0;

        while ("FAIL".equals(ok) && (count < MAX_ATTEMPTS)) {
            ok = authService.verifyUser(id, pw);
            count++;
        }

        if ("OK".equals(ok)) {
            jwtDTO = authService.loginMobile(authRequestDTO);
        } else {
            return new AnsApiResponse<>(jwtDTO);
        }


        return new AnsApiResponse<>(jwtDTO);
    }

    @Operation(summary = "Access Token 재발급", description = "Access Token 재발급")
    @PostMapping(path = "/againlogin")//againlogin
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<JwtDTO> reissuance(@Parameter(name = "authorization", description = "엑세스토큰")
                                             @RequestHeader(value = "authorization", required = false) String authorization) throws Exception {

        JwtDTO jwtDTO = authService.reissuance(authorization);

        return new AnsApiResponse<>(jwtDTO);
    }

    @Operation(summary = "로그아웃", description = "로그아웃")
    @PostMapping(path = "/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> logout(@Parameter(name = "authorization", description = "엑세스토큰")
                                    @RequestHeader(value = "authorization", required = false) String authorization) throws Exception {

        authService.logout(authorization);

        return AnsApiResponse.noContent();
    }

    @Operation(summary = "사용자 로그인 정보 조회", description = "사용자 로그인 정보 조회")
    @GetMapping(path = "/me")
    public AnsApiResponse<AuthDTO> find(@Parameter(name = "authorization", description = "엑세스토큰", in = ParameterIn.PATH)
                                        @RequestHeader(value = "authorization", required = false) String authorization) throws Exception {

        AuthDTO authDTO = authService.find(authorization);

        return new AnsApiResponse<>(authDTO);
    }

    @Operation(summary = "사용자 접속 타입 조회( 모바일 , 웹 )", description = "사용자 접속 타입 조회( 모바일 , 웹 )")
    @GetMapping(path = "/usertype")
    public AnsApiResponse<List<AuthDTO>> findAllUserType(@Parameter(name = "userId", description = "사용자 아이디[아이디 없이 조회할 경우 모든 사용자 조회]")
                                                         @RequestParam(value = "userId", required = false) String userId,
                                                         @Parameter(name = "userLoginTyp", description = "사용자 로그인 타입[web[L01], mobile[L02]]")
                                                         @RequestParam(value = "userLoginTyp", required = false) String userLoginTyp) throws Exception {

        List<AuthDTO> authDTOList = authService.findAllUserType(userId, userLoginTyp);

        return new AnsApiResponse<>(authDTOList);
    }

    @Operation(summary = "모바일 사용자 강제 로그아웃", description = "모바일 사용자 강제 로그아웃")
    //@PutMapping(path = "/mobilelogout")
    @DeleteMapping(path = "/mobilelogout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> mobileLogout(@Parameter(name = "userId", description = "사용자 아이디[아이디 없이 조회할 경우 모든 사용자 조회]")
                                          @RequestParam(value = "userId", required = false) String userId,
                                          @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        authService.mobileLogOut(userId);

        return AnsApiResponse.noContent();
    }
}
