package com.gemiso.zodiac.app.auth;


import com.gemiso.zodiac.app.auth.dto.AuthDTO;
import com.gemiso.zodiac.app.auth.dto.JwtDTO;
import com.gemiso.zodiac.app.auth.dto.AuthRequestDTO;
import com.gemiso.zodiac.core.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Log4j2
@RequiredArgsConstructor
@Api(description = "로그인 API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "사용자 로그인", description = "사용자 로그인")
    @PostMapping(path = "/createToken")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<JwtDTO> login(@Parameter(name = "loginDTO", required = true, description = "필수값<br> userId , password")
                                     @Valid @RequestBody AuthRequestDTO authRequestDTO
    ) throws Exception {

        JwtDTO jwtDTO = authService.login(authRequestDTO);

        return new ApiResponse<>(jwtDTO);
    }

    @Operation(summary = "Access Token 재발급", description = "Access Token 재발급")
    @PostMapping(path = "/refresh")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<JwtDTO> reissuance(@Parameter(name = "authorization", description = "엑세스토큰")
                                          @RequestHeader(value = "authorization", required = false) String authorization) throws Exception {

        JwtDTO jwtDTO = authService.reissuance(authorization);

        return new ApiResponse<>(jwtDTO);
    }

    @Operation(summary = "로그아웃", description = "로그아웃")
    @PostMapping(path = "/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> logout(@Parameter(name = "authorization", description = "엑세스토큰")
                                 @RequestHeader(value = "authorization", required = false) String authorization) throws Exception {

        authService.logout(authorization);

        return ApiResponse.noContent();
    }

    @Operation(summary = "사용자 로그인 정보 조회", description = "사용자 로그인 정보 조회")
    @GetMapping(path = "")
    public ApiResponse<AuthDTO> find(@Parameter(name = "authorization", description = "엑세스토큰", in = ParameterIn.PATH)
                                      @RequestHeader(value = "authorization", required = false) String authorization) throws Exception {

        AuthDTO authDTO = authService.find(authorization);

        return new ApiResponse<>(authDTO);
    }
}
