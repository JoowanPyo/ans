package com.gemiso.zodiac.app.appAuth;

import com.gemiso.zodiac.app.appAuth.dto.AppAuthCreateDTO;
import com.gemiso.zodiac.app.appAuth.dto.AppAuthDTO;
import com.gemiso.zodiac.app.appAuth.dto.AppAuthUpdateDTO;
import com.gemiso.zodiac.core.response.ApiResponse;
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

//@Tag(name = "auths", description = "권한 API")
@Api(description = "권한 API")
@RestController
@RequestMapping("/auths")
@Slf4j
@RequiredArgsConstructor
public class AppAuthController {

    private final AppAuthService appAuthService;

    @Operation(summary = "권한 상세조회", description = "권한 상세조회")
    @GetMapping(path = "")
    public ApiResponse<List<AppAuthDTO>> findAll(@Parameter(name = "useYn", description = "사용 여부", in = ParameterIn.QUERY) @RequestParam(value = "useYn", required = false) String useYn,
                                                 @Parameter(name = "delYn", description = "삭제 여부", in = ParameterIn.QUERY) @RequestParam(value = "delYn", required = false) String delYn,
                                                 @Parameter(name = "hrnkAppAuthCd", description = "상위코드", in = ParameterIn.QUERY) @RequestParam(value = "hrnkAppAuthCd", required = false) String hrnkAppAuthCd,
                                                 @Parameter(name = "searchWord", description = "검색어", in = ParameterIn.QUERY) @RequestParam(value = "searchWord", required = false) String searchWord) {

        List<AppAuthDTO> appAuthDTO = appAuthService.findAll(useYn, delYn, hrnkAppAuthCd, searchWord);

        return new ApiResponse<>(appAuthDTO);
    }

    @Operation(summary = "권한 상세조회", description = "권한 상세조회")
    @GetMapping(path = "/{appAuthId}")
    public ApiResponse<AppAuthDTO> find(@Parameter(name = "appAuthId", description = "권한 아이디") @PathVariable Long appAuthId) {

        AppAuthDTO appAuthDTO = appAuthService.find(appAuthId);

        return new ApiResponse<>(appAuthDTO);
    }

    @Operation(summary = "권한 등록", description = "권한 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AppAuthDTO> create(@Parameter(name = "appAuthCreatDTO", required = true, description = "필수값<br> 권한 명")
                                          @Valid @RequestBody AppAuthCreateDTO appAuthCreatDTO) {

        Long appAuthId = appAuthService.create(appAuthCreatDTO);

        //권한 등록 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        AppAuthDTO appAuthDTO = new AppAuthDTO();
        appAuthDTO.setAppAuthId(appAuthId);
        //AppAuthDTO appAuthDto = appAuthService.getAppAuth(appAuthId);

        return new ApiResponse<>(appAuthDTO);
    }

    @Operation(summary = "권한 수정", description = "권한 수정")
    @PutMapping(path = "/{appAuthId}")
    public ApiResponse<AppAuthDTO> update(@Parameter(name = "appAuthUpdateDTO", required = true) @Valid @RequestBody AppAuthUpdateDTO appAuthUpdateDTO,
                                          @Parameter(name = "appAuthId", required = true) @PathVariable("appAuthId") Long appAuthId) {

        appAuthService.update(appAuthUpdateDTO, appAuthId);

        //권한 수정 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        AppAuthDTO appAuthDTO = new AppAuthDTO();
        appAuthDTO.setAppAuthId(appAuthId);

        return new ApiResponse<>(appAuthDTO);

    }

    @Operation(summary = "권한 삭제", description = "권한 삭제")
    @DeleteMapping(path = "/{appAuthId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private ApiResponse<?> delete(@Parameter(name = "appAuthId", description = "권한 아이디") @PathVariable("appAuthId") Long appAuthId) {

        appAuthService.delete(appAuthId);

        return ApiResponse.noContent();
    }


}
