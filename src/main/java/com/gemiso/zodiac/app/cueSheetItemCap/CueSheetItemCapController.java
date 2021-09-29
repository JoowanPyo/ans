package com.gemiso.zodiac.app.cueSheetItemCap;

import com.gemiso.zodiac.app.cueSheetItemCap.dto.CueSheetItemCapCreateDTO;
import com.gemiso.zodiac.app.cueSheetItemCap.dto.CueSheetItemCapDTO;
import com.gemiso.zodiac.app.cueSheetItemCap.dto.CueSheetItemCapUpdateDTO;
import com.gemiso.zodiac.core.response.ApiResponse;
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
@RequestMapping("/cueitemcap")
@Slf4j
@RequiredArgsConstructor
public class CueSheetItemCapController {

    private final CueSheetItemCapService cueSheetItemCapService;

    @Operation(summary = "큐시트 아이템 자막 조회", description = "큐시트 아이템 자막 조회")
    @GetMapping(path = "/{cueId}/item/{cueItemId}/caption")
    public ApiResponse<List<CueSheetItemCapDTO>> findAll(@Parameter(name = "cueId", description = "큐시트 아이디")
                                                    @PathVariable("cueId") Long cueId,
                                                         @Parameter(name = "cueItemId", description = "큐시트 아이템 아이디")
                                                    @PathVariable("cueItemId") Long cueItemId,
                                                         @Parameter(name = "cueItemCapDivCd", description = "큐시트 아이템 자막 구분 코드")
                                                    @RequestParam(value = "cueItemCapDivCd", required = false) String cueItemCapDivCd) {

        List<CueSheetItemCapDTO> cueSheetItemCapDTOList = cueSheetItemCapService.findAll(cueId, cueItemId, cueItemCapDivCd);

        return new ApiResponse<>(cueSheetItemCapDTOList);
    }

    @Operation(summary = "큐시트 아이템 자막 상세정보 조회", description = "큐시트 아이템 자막 상세정보 조회")
    @GetMapping(path = "/{cueId}/item/{cueItemId}/caption/{cueItemCapId}")
    public ApiResponse<CueSheetItemCapDTO> find(@Parameter(name = "cueId", description = "큐시트 아이디")
                                           @PathVariable("cueId") Long cueId,
                                                @Parameter(name = "cueItemId", description = "큐시트 아이템 아이디")
                                           @PathVariable("cueItemId") Long cueItemId,
                                                @Parameter(name = "cueItemCapId", description = "자막아이디")
                                           @PathVariable("cueItemCapId") Long cueItemCapId) {

        //수정. cueId가 왜 들어가는지...

        CueSheetItemCapDTO cueSheetItemCapDTO = cueSheetItemCapService.find(cueItemId, cueItemCapId);

        return new ApiResponse<>(cueSheetItemCapDTO);

    }

    @Operation(summary = "큐시트 아이템 자막 등록", description = "큐시트 아이템 자막 등록 List<CueSheetItemCapCreateDTO>")
    @PostMapping(path = "/{cueId}/item/{cueItemId}/caption/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<List<CueSheetItemCapDTO>> createList(@Parameter(description = "필수값<br> ", required = true)
                                                       @RequestBody List<CueSheetItemCapCreateDTO> cueSheetItemCapCreateDTOList,
                                                            @Parameter(name = "cueId", description = "큐시트 아이디")
                                                       @PathVariable("cueId") Long cueId,
                                                            @Parameter(name = "cueItemId", description = "큐시트 아이템 아이디")
                                                       @PathVariable("cueItemId") Long cueItemId) {

        cueSheetItemCapService.createList(cueSheetItemCapCreateDTOList, cueId, cueItemId);

        List<CueSheetItemCapDTO> cueSheetItemCapDTOList = cueSheetItemCapService.findAll(cueId, cueItemId, cueSheetItemCapCreateDTOList.get(0).getCueItemCapDivCd());

        return new ApiResponse<>(cueSheetItemCapDTOList);
    }

    @Operation(summary = "큐시트 아이템 자막 등록", description = "큐시트 아이템 자막 등록 CueSheetItemCapCreateDTO")
    @PostMapping(path = "/{cueId}/item/{cueItemId}/caption")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CueSheetItemCapDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                             @RequestBody CueSheetItemCapCreateDTO cueSheetItemCapCreateDTO,
                                                  @Parameter(name = "cueId", description = "큐시트 아이디")
                                             @PathVariable("cueId") Long cueId,
                                                  @Parameter(name = "cueItemId", description = "큐시트 아이템 아이디")
                                             @PathVariable("cueItemId") Long cueItemId) {

        Long cueItemCapId = cueSheetItemCapService.create(cueSheetItemCapCreateDTO, cueId, cueItemId);

        CueSheetItemCapDTO cueSheetItemCapDTO = cueSheetItemCapService.find(cueItemId, cueItemCapId);

        return new ApiResponse<>(cueSheetItemCapDTO);

    }

    @Operation(summary = "큐시트 아이템 자막 수정", description = "큐시트 아이템 자막 수정 ")
    @PutMapping(path = "/{cueId}/item/{cueItemId}/caption/{cueItemCapId")
    public ApiResponse<CueSheetItemCapDTO> update(@Parameter(description = "필수값<br> ", required = true)
                                             @RequestBody CueSheetItemCapUpdateDTO cueSheetItemCapUpdateDTO,
                                                  @Parameter(name = "cueId", description = "큐시트 아이디")
                                             @PathVariable("cueId") Long cueId,
                                                  @Parameter(name = "cueItemId", description = "큐시트 아이템 아이디")
                                             @PathVariable("cueItemId") Long cueItemId,
                                                  @Parameter(name = "cueItemCapId", description = "자막아이디")
                                             @PathVariable("cueItemCapId") Long cueItemCapId) {

        cueSheetItemCapService.update(cueSheetItemCapUpdateDTO, cueId, cueItemId, cueItemCapId);

        CueSheetItemCapDTO cueSheetItemCapDTO = cueSheetItemCapService.find(cueItemId, cueItemCapId);

        return new ApiResponse<>(cueSheetItemCapDTO);
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

        cueSheetItemCapService.delete(cueId, cueItemId, cueItemCapId);

        return ApiResponse.noContent();
    }

}
