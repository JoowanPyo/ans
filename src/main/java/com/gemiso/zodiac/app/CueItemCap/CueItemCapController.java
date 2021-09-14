package com.gemiso.zodiac.app.CueItemCap;

import com.gemiso.zodiac.app.CueItemCap.dto.CueItemCapCreateDTO;
import com.gemiso.zodiac.app.CueItemCap.dto.CueItemCapDTO;
import com.gemiso.zodiac.app.CueItemCap.dto.CueItemCapUpdateDTO;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupCreateDTO;
import com.gemiso.zodiac.core.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "큐시트 아이템 자막 API")
@RestController
@RequestMapping("/cueitemcap")
@Slf4j
@RequiredArgsConstructor
public class CueItemCapController {

    private final CueItemCapService cueItemCapService;

    @Operation(summary = "큐시트 아이템 자막 조회", description = "큐시트 아이템 자막 조회")
    @GetMapping(path = "/{cueId}/item/{cueItemId}/caption")
    public ApiResponse<List<CueItemCapDTO>> findAll(@Parameter(name = "cueId", description = "큐시트 아이디")
                                                    @PathVariable("cueId") Long cueId,
                                                    @Parameter(name = "cueItemId", description = "큐시트 아이템 아이디")
                                                    @PathVariable("cueItemId") Long cueItemId,
                                                    @Parameter(name = "cueItemCapDivCd", description = "큐시트 아이템 자막 구분 코드")
                                                    @RequestParam(value = "cueItemCapDivCd", required = false) String cueItemCapDivCd) {

        List<CueItemCapDTO> cueItemCapDTOList = cueItemCapService.findAll(cueId, cueItemId, cueItemCapDivCd);

        return new ApiResponse<>(cueItemCapDTOList);
    }

    @Operation(summary = "큐시트 아이템 자막 상세정보 조회", description = "큐시트 아이템 자막 상세정보 조회")
    @GetMapping(path = "/{cueId}/item/{cueItemId}/caption/{cueItemCapId}")
    public ApiResponse<CueItemCapDTO> find(@Parameter(name = "cueId", description = "큐시트 아이디")
                                           @PathVariable("cueId") Long cueId,
                                           @Parameter(name = "cueItemId", description = "큐시트 아이템 아이디")
                                           @PathVariable("cueItemId") Long cueItemId,
                                           @Parameter(name = "cueItemCapId", description = "자막아이디")
                                           @PathVariable("cueItemCapId") Long cueItemCapId) {

        //수정. cueId가 왜 들어가는지...

        CueItemCapDTO cueItemCapDTO = cueItemCapService.find(cueItemId, cueItemCapId);

        return new ApiResponse<>(cueItemCapDTO);

    }

    @Operation(summary = "큐시트 아이템 자막 등록", description = "큐시트 아이템 자막 등록 List<CueItemCapCreateDTO>")
    @PostMapping(path = "/{cueId}/item/{cueItemId}/caption/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<List<CueItemCapDTO>> createList(@Parameter(description = "필수값<br> ", required = true)
                                                       @RequestBody List<CueItemCapCreateDTO> cueItemCapCreateDTOList,
                                                       @Parameter(name = "cueId", description = "큐시트 아이디")
                                                       @PathVariable("cueId") Long cueId,
                                                       @Parameter(name = "cueItemId", description = "큐시트 아이템 아이디")
                                                       @PathVariable("cueItemId") Long cueItemId) {

        cueItemCapService.createList(cueItemCapCreateDTOList, cueId, cueItemId);

        List<CueItemCapDTO> cueItemCapDTOList = cueItemCapService.findAll(cueId, cueItemId, cueItemCapCreateDTOList.get(0).getCueItemCapDivCd());

        return new ApiResponse<>(cueItemCapDTOList);
    }

    @Operation(summary = "큐시트 아이템 자막 등록", description = "큐시트 아이템 자막 등록 CueItemCapCreateDTO")
    @PostMapping(path = "/{cueId}/item/{cueItemId}/caption")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CueItemCapDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                             @RequestBody CueItemCapCreateDTO cueItemCapCreateDTO,
                                             @Parameter(name = "cueId", description = "큐시트 아이디")
                                             @PathVariable("cueId") Long cueId,
                                             @Parameter(name = "cueItemId", description = "큐시트 아이템 아이디")
                                             @PathVariable("cueItemId") Long cueItemId) {

        Long cueItemCapId = cueItemCapService.create(cueItemCapCreateDTO, cueId, cueItemId);

        CueItemCapDTO cueItemCapDTO = cueItemCapService.find(cueItemId, cueItemCapId);

        return new ApiResponse<>(cueItemCapDTO);

    }

    @Operation(summary = "큐시트 아이템 자막 수정", description = "큐시트 아이템 자막 수정 ")
    @PutMapping(path = "/{cueId}/item/{cueItemId}/caption/{cueItemCapId")
    public ApiResponse<CueItemCapDTO> update(@Parameter(description = "필수값<br> ", required = true)
                                             @RequestBody CueItemCapUpdateDTO cueItemCapUpdateDTO,
                                             @Parameter(name = "cueId", description = "큐시트 아이디")
                                             @PathVariable("cueId") Long cueId,
                                             @Parameter(name = "cueItemId", description = "큐시트 아이템 아이디")
                                             @PathVariable("cueItemId") Long cueItemId,
                                             @Parameter(name = "cueItemCapId", description = "자막아이디")
                                             @PathVariable("cueItemCapId") Long cueItemCapId) {

        cueItemCapService.update(cueItemCapUpdateDTO, cueId, cueItemId, cueItemCapId);

        CueItemCapDTO cueItemCapDTO = cueItemCapService.find(cueItemId, cueItemCapId);

        return new ApiResponse<>(cueItemCapDTO);
    }

    @Operation(summary = "큐시트 아이템 자막 삭제", description = "큐시트 아이템 자막 삭제")
    @DeleteMapping(path = "/{cueId}/item/{cueItemId}/caption/{cueItemCapId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> delete(@Parameter(name = "cueId", description = "큐시트 아이디")
                                 @PathVariable("cueId") Long cueId,
                                 @Parameter(name = "cueItemId", description = "큐시트 아이템 아이디")
                                 @PathVariable("cueItemId") Long cueItemId,
                                 @Parameter(name = "cueItemCapId", description = "자막아이디")
                                 @PathVariable("cueItemCapId") Long cueItemCapId) {

        cueItemCapService.delete(cueId, cueItemId, cueItemCapId);

        return ApiResponse.noContent();
    }

}
