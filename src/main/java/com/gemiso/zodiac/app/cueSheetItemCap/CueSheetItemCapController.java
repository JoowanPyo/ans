package com.gemiso.zodiac.app.cueSheetItemCap;

import com.gemiso.zodiac.app.cueSheetItemCap.dto.CueSheetItemCapCreateDTO;
import com.gemiso.zodiac.app.cueSheetItemCap.dto.CueSheetItemCapDTO;
import com.gemiso.zodiac.app.cueSheetItemCap.dto.CueSheetItemCapUpdateDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.gemiso.zodiac.core.service.JwtGetUserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "큐시트 아이템 자막 API")
@RestController
@RequestMapping("/cueitemcaps")
@Slf4j
@RequiredArgsConstructor
public class CueSheetItemCapController {

    private final CueSheetItemCapService cueSheetItemCapService;

    private final JwtGetUserService jwtGetUserService;
    //private final UserAuthService userAuthService;

    @Operation(summary = "큐시트 아이템 자막 조회", description = "큐시트 아이템 자막 조회")
    @GetMapping(path = "/{cueId}/item/{cueItemId}/caption")
    public AnsApiResponse<List<CueSheetItemCapDTO>> findAll(@Parameter(name = "cueId", description = "큐시트 아이디")
                                                            @PathVariable("cueId") Long cueId,
                                                            @Parameter(name = "cueItemId", description = "큐시트 아이템 아이디")
                                                            @PathVariable("cueItemId") Long cueItemId,
                                                            @Parameter(name = "cueItemCapDivCd", description = "큐시트 아이템 자막 구분 코드")
                                                            @RequestParam(value = "cueItemCapDivCd", required = false) String cueItemCapDivCd) {

        List<CueSheetItemCapDTO> cueSheetItemCapDTOList = cueSheetItemCapService.findAll(cueId, cueItemId, cueItemCapDivCd);

        return new AnsApiResponse<>(cueSheetItemCapDTOList);
    }

    @Operation(summary = "큐시트 아이템 자막 상세정보 조회", description = "큐시트 아이템 자막 상세정보 조회")
    @GetMapping(path = "/{cueId}/item/{cueItemId}/caption/{cueItemCapId}")
    public AnsApiResponse<CueSheetItemCapDTO> find(@Parameter(name = "cueId", description = "큐시트 아이디")
                                                   @PathVariable("cueId") Long cueId,
                                                   @Parameter(name = "cueItemId", description = "큐시트 아이템 아이디")
                                                   @PathVariable("cueItemId") Long cueItemId,
                                                   @Parameter(name = "cueItemCapId", description = "자막아이디")
                                                   @PathVariable("cueItemCapId") Long cueItemCapId) {

        //수정. cueId가 왜 들어가는지...

        CueSheetItemCapDTO cueSheetItemCapDTO = cueSheetItemCapService.find(cueItemId, cueItemCapId);

        return new AnsApiResponse<>(cueSheetItemCapDTO);

    }

    @Operation(summary = "큐시트 아이템 자막 등록", description = "큐시트 아이템 자막 등록 List<CueSheetItemCapCreateDTO>")
    @PostMapping(path = "/{cueId}/item/{cueItemId}/caption/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<List<CueSheetItemCapDTO>> createList(@Parameter(description = "필수값<br> ", required = true)
                                                               @RequestBody List<CueSheetItemCapCreateDTO> cueSheetItemCapCreateDTOList,
                                                               @Parameter(name = "cueId", description = "큐시트 아이디")
                                                               @PathVariable("cueId") Long cueId,
                                                               @Parameter(name = "cueItemId", description = "큐시트 아이템 아이디")
                                                               @PathVariable("cueItemId") Long cueItemId,
                                                               @RequestHeader(value = "Authorization", required = false)String Authorization) throws Exception {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId =jwtGetUserService.getUser(Authorization);
        log.info("CueSheet Item Cap Create List : userId - " + userId + " cueId - " + cueId + " cueItemId - " + cueItemId);

        cueSheetItemCapService.createList(cueSheetItemCapCreateDTOList, cueId, cueItemId ,userId);

        String cueItemCapDivCd = cueSheetItemCapCreateDTOList.get(0).getCueItemCapDivCd();

        List<CueSheetItemCapDTO> cueSheetItemCapDTOList = cueSheetItemCapService.findAll(cueId, cueItemId, cueItemCapDivCd);

        return new AnsApiResponse<>(cueSheetItemCapDTOList);
    }

    @Operation(summary = "큐시트 아이템 자막 등록", description = "큐시트 아이템 자막 등록 CueSheetItemCapCreateDTO")
    @PostMapping(path = "/{cueId}/item/{cueItemId}/caption")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<CueSheetItemCapDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                                     @RequestBody CueSheetItemCapCreateDTO cueSheetItemCapCreateDTO,
                                                     @Parameter(name = "cueId", description = "큐시트 아이디")
                                                     @PathVariable("cueId") Long cueId,
                                                     @Parameter(name = "cueItemId", description = "큐시트 아이템 아이디")
                                                     @PathVariable("cueItemId") Long cueItemId,
                                                     @RequestHeader(value = "Authorization", required = false)String Authorization) throws Exception {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId =jwtGetUserService.getUser(Authorization);

        log.info("CueSheet Item Cap Create : userId - " + userId + " cueId - " + cueId + " cueItemId - " + cueItemId);

        Long cueItemCapId = cueSheetItemCapService.create(cueSheetItemCapCreateDTO, cueId, cueItemId, userId);

        CueSheetItemCapDTO cueSheetItemCapDTO = cueSheetItemCapService.find(cueItemId, cueItemCapId);

        return new AnsApiResponse<>(cueSheetItemCapDTO);

    }

    @Operation(summary = "큐시트 아이템 자막 수정", description = "큐시트 아이템 자막 수정 ")
    @PutMapping(path = "/{cueId}/item/{cueItemId}/caption/{cueItemCapId")
    public AnsApiResponse<CueSheetItemCapDTO> update(@Parameter(description = "필수값<br> ", required = true)
                                                     @RequestBody CueSheetItemCapUpdateDTO cueSheetItemCapUpdateDTO,
                                                     @Parameter(name = "cueId", description = "큐시트 아이디")
                                                     @PathVariable("cueId") Long cueId,
                                                     @Parameter(name = "cueItemId", description = "큐시트 아이템 아이디")
                                                     @PathVariable("cueItemId") Long cueItemId,
                                                     @Parameter(name = "cueItemCapId", description = "자막아이디")
                                                     @PathVariable("cueItemCapId") Long cueItemCapId,
                                                     @RequestHeader(value = "Authorization", required = false)String Authorization) throws Exception {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId =jwtGetUserService.getUser(Authorization);
        log.info("CueSheet Item Cap Update : userId - " + userId +" cueItemCapId - "+cueItemCapId
                +" cueId - " + cueId + " cueItemId - " + cueItemId);

        cueSheetItemCapService.update(cueSheetItemCapUpdateDTO, cueId, cueItemId, cueItemCapId, userId);

        CueSheetItemCapDTO cueSheetItemCapDTO = cueSheetItemCapService.find(cueItemId, cueItemCapId);

        return new AnsApiResponse<>(cueSheetItemCapDTO);
    }

    @Operation(summary = "큐시트 아이템 자막 삭제", description = "큐시트 아이템 자막 삭제")
    @DeleteMapping(path = "/{cueId}/item/{cueItemId}/caption/{cueItemCapId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "cueId", description = "큐시트 아이디")
                                    @PathVariable("cueId") Long cueId,
                                    @Parameter(name = "cueItemId", description = "큐시트 아이템 아이디")
                                    @PathVariable("cueItemId") Long cueItemId,
                                    @Parameter(name = "cueItemCapId", description = "자막아이디")
                                    @PathVariable("cueItemCapId") Long cueItemCapId,
                                    @RequestHeader(value = "Authorization", required = false)String Authorization) throws Exception {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId =jwtGetUserService.getUser(Authorization);
        log.info("CueSheet Item Cap Delete : userId - " + userId +" cueItemCapId - "+cueItemCapId
                +" cueId - " + cueId + " cueItemId - " + cueItemId);

        cueSheetItemCapService.delete(cueId, cueItemId, cueItemCapId);

        return AnsApiResponse.noContent();
    }

}
