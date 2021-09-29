package com.gemiso.zodiac.app.cueSheetItem;

import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemCreateDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemUpdateDTO;
import com.gemiso.zodiac.core.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(description = "큐시트 아이템 API")
@RestController
@RequestMapping("/cuesheetitem")
@RequiredArgsConstructor
@Slf4j
public class CueSheetItemController {

    private final CueSheetItemService cueSheetItemService;

    @Operation(summary = "큐시트 아이템 상세정보 조회", description = "큐시트 아이템 상세정보 조회")
    @GetMapping(path = "")
    public ApiResponse<CueSheetItemDTO> find(@Parameter(name = "cueItemId", description = "큐시트아이템아이디")
                                             @PathVariable("cueItemId") Long cueItemId) {

        CueSheetItemDTO cueSheetItemDTO = cueSheetItemService.find(cueItemId);

        return new ApiResponse<>(cueSheetItemDTO);
    }

    @Operation(summary = "큐시트 아이템 저장", description = "큐시트 아이템 저장")
    @PostMapping(path = "/{cueId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CueSheetItemDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                               @RequestBody @Valid CueSheetItemCreateDTO cueSheetItemCreateDTO,
                                               @Parameter(name = "cueId", description = "큐시트아이디")
                                               @PathVariable("cueId") Long cueId) {

        Long cueItemId = cueSheetItemService.create(cueSheetItemCreateDTO, cueId);

        CueSheetItemDTO cueSheetItemDTO = cueSheetItemService.find(cueItemId);

        return new ApiResponse<>(cueSheetItemDTO);
    }

    @Operation(summary = "큐시트 아이템 수정", description = "큐시트 아이템 수정")
    @PutMapping(path = "/{cueId}/{cueItemId}")
    public ApiResponse<CueSheetItemDTO> update(@Parameter(description = "필수값<br> ", required = true)
                                               @RequestBody @Valid CueSheetItemUpdateDTO cueSheetItemUpdateDTO,
                                               @Parameter(name = "cueId", description = "큐시트아이디")
                                               @PathVariable("cueId") Long cueId,
                                               @Parameter(name = "cueItemId", description = "큐시트아이템 아이디")
                                               @PathVariable("cueItemId") Long cueItemId) {

        cueSheetItemService.update(cueSheetItemUpdateDTO, cueId, cueItemId);

        CueSheetItemDTO cueSheetItemDTO = cueSheetItemService.find(cueItemId);

        return new ApiResponse<>(cueSheetItemDTO);

    }

    @Operation(summary = "큐시트 아이템 삭제", description = "큐시트 아이템 삭제")
    @DeleteMapping(path = "/{cueId}/{cueItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> delete(@Parameter(name = "cueId", description = "큐시트아이디")
                                 @PathVariable("cueId") Long cueId,
                                 @Parameter(name = "cueItemId", description = "큐시트아이템 아이디")
                                 @PathVariable("cueItemId") Long cueItemId) {

        cueSheetItemService.delete(cueId, cueItemId);

        return ApiResponse.noContent();
    }

    @Operation(summary = "큐시트 아이템 순서변경", description = "큐시트 아이템 순서변경")
    @PutMapping(path = "")
    public ApiResponse<CueSheetItemDTO> ordUpdate(@Parameter(description = "필수값<br> ", required = true)
                                                  @RequestBody @Valid CueSheetItemUpdateDTO cueSheetItemUpdateDTO,
                                                  @Parameter(name = "cueId", description = "큐시트아이디")
                                                  @PathVariable("cueId") Long cueId,
                                                  @Parameter(name = "cueItemId", description = "큐시트아이템 아이디")
                                                  @PathVariable("cueItemId") Long cueItemId) {

        cueSheetItemService.ordUpdate(cueSheetItemUpdateDTO, cueId, cueItemId);

        CueSheetItemDTO cueSheetItemDTO = cueSheetItemService.find(cueItemId);

        return new ApiResponse<>(cueSheetItemDTO);

    }
}
