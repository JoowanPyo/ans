package com.gemiso.zodiac.app.appAuth;

import com.gemiso.zodiac.app.appAuth.dto.AppAuthCreateDTO;
import com.gemiso.zodiac.app.appAuth.dto.AppAuthDTO;
import com.gemiso.zodiac.app.appAuth.dto.AppAuthUpdateDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.gemiso.zodiac.core.service.JwtGetUserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;

//@Tag(name = "auths", description = "권한 API")
@Api(description = "권한 API")
@RestController
@RequestMapping("/auths")
@Slf4j
@RequiredArgsConstructor
public class AppAuthController {

    private final AppAuthService appAuthService;

    private final JwtGetUserService jwtGetUserService;

    @Operation(summary = "권한 상세조회", description = "권한 상세조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<AppAuthDTO>> findAll(@Parameter(name = "useYn", description = "사용 여부", in = ParameterIn.QUERY) @RequestParam(value = "useYn", required = false) String useYn,
                                                    @Parameter(name = "delYn", description = "삭제 여부", in = ParameterIn.QUERY) @RequestParam(value = "delYn", required = false) String delYn,
                                                    @Parameter(name = "hrnkAppAuthCd", description = "상위코드", in = ParameterIn.QUERY) @RequestParam(value = "hrnkAppAuthCd", required = false) String hrnkAppAuthCd,
                                                    @Parameter(name = "searchWord", description = "검색어", in = ParameterIn.QUERY) @RequestParam(value = "searchWord", required = false) String searchWord) {

        List<AppAuthDTO> appAuthDTO = appAuthService.findAll(useYn, delYn, hrnkAppAuthCd, searchWord);

        return new AnsApiResponse<>(appAuthDTO);
    }

    @Operation(summary = "권한 상세조회", description = "권한 상세조회")
    @GetMapping(path = "/{appAuthId}")
    public AnsApiResponse<AppAuthDTO> find(@Parameter(name = "appAuthId", description = "권한 아이디") @PathVariable Long appAuthId) {

        AppAuthDTO appAuthDTO = appAuthService.find(appAuthId);

        return new AnsApiResponse<>(appAuthDTO);
    }

    @Operation(summary = "권한 등록", description = "권한 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<AppAuthDTO> create(@Parameter(name = "appAuthCreatDTO", required = true, description = "필수값<br> 권한 명")
                                          @Valid @RequestBody AppAuthCreateDTO appAuthCreatDTO,
                                             @RequestHeader(value = "Authorization", required = false)String Authorization) throws Exception {

        String userId =jwtGetUserService.getUser(Authorization);

        Long appAuthId = appAuthService.create(appAuthCreatDTO, userId);

        //권한 등록 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        AppAuthDTO appAuthDTO = new AppAuthDTO();
        appAuthDTO.setAppAuthId(appAuthId);
        //AppAuthDTO appAuthDto = appAuthService.getAppAuth(appAuthId);

        return new AnsApiResponse<>(appAuthDTO);
    }

    @Operation(summary = "권한 수정", description = "권한 수정")
    @PutMapping(path = "/{appAuthId}")
    public AnsApiResponse<AppAuthDTO> update(@Parameter(name = "appAuthUpdateDTO", required = true) @Valid @RequestBody AppAuthUpdateDTO appAuthUpdateDTO,
                                             @Parameter(name = "appAuthId", required = true) @PathVariable("appAuthId") Long appAuthId,
                                             @RequestHeader(value = "Authorization", required = false)String Authorization) throws Exception {

        String userId =jwtGetUserService.getUser(Authorization);

        appAuthService.update(appAuthUpdateDTO, appAuthId, userId);

        //권한 수정 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        AppAuthDTO appAuthDTO = new AppAuthDTO();
        appAuthDTO.setAppAuthId(appAuthId);

        return new AnsApiResponse<>(appAuthDTO);

    }

    @Operation(summary = "권한 삭제", description = "권한 삭제")
    @DeleteMapping(path = "/{appAuthId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private AnsApiResponse<?> delete(@Parameter(name = "appAuthId", description = "권한 아이디") @PathVariable("appAuthId") Long appAuthId,
                                     @RequestHeader(value = "Authorization", required = false)String Authorization) throws Exception {

        String userId =jwtGetUserService.getUser(Authorization);

        appAuthService.delete(appAuthId, userId);

        return AnsApiResponse.noContent();
    }


}
