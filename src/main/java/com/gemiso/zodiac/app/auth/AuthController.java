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
import java.io.IOException;

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
        try {

            while ("FAIL".equals(ok) && (count < MAX_ATTEMPTS)){
                ok = authService.verifyUser(id, pw);
                count++;
            }

            if ("OK".equals(ok)){
                jwtDTO = authService.login(authRequestDTO);
            }else {
                return new AnsApiResponse<>(jwtDTO);
            }

        }catch (RuntimeException e){
            log.error("로그인 인증 실패 : userId - "+id);
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
}
