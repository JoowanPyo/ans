package com.gemiso.zodiac.app.cueSheetItem;

import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSymbolCreateDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSymbolDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSymbolUpdateDTO;
import com.gemiso.zodiac.core.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(description = "큐시트 아이템 심볼 API")
@RestController
@RequestMapping("/cuesheetitemsymbols")
@RequiredArgsConstructor
@Slf4j
public class CueSheetItemSymbolController {

    private final CueSheetItemSymbolService cueSheetItemSymbolService;


    @Operation(summary = "큐시트 아이템 심볼 상세조회", description = "큐시트 아이템 심볼 상세조회")
    @GetMapping(path = "/{cueItemSymbolId}")
    public ApiResponse<CueSheetItemSymbolDTO> find(@Parameter(name = "cueItemSymbolId", description = "큐시트아이템 아이디")
                                                   @PathVariable("cueItemSymbolId") Long cueItemSymbolId) {

        CueSheetItemSymbolDTO cueSheetItemSymbolDTO = cueSheetItemSymbolService.find(cueItemSymbolId);

        return new ApiResponse<CueSheetItemSymbolDTO>(cueSheetItemSymbolDTO);
    }

    @Operation(summary = "큐시트 아이템 심볼 등록", description = "큐시트 아이템 심볼 등록")
    @PostMapping(path = "/{cueItemId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CueSheetItemSymbolDTO> create(@Parameter(name = "cueItemId", description = "큐시트아이템 아이디")
                                                     @PathVariable("cueItemId") Long cueItemId,
                                                     @Parameter(description = "필수값<br> ", required = true)
                                                     @RequestBody @Valid CueSheetItemSymbolCreateDTO cueSheetItemSymbolCreateDTO) {

        Long id = cueSheetItemSymbolService.create(cueSheetItemSymbolCreateDTO, cueItemId);

        CueSheetItemSymbolDTO cueSheetItemSymbolDTO = cueSheetItemSymbolService.find(id);

        return new ApiResponse<CueSheetItemSymbolDTO>(cueSheetItemSymbolDTO);
    }

    @Operation(summary = "큐시트 아이템 심볼 수정", description = "큐시트 아이템 심볼 수정")
    @PutMapping(path = "/{cueItemId}")
    public ApiResponse<CueSheetItemSymbolDTO> update(@Parameter(name = "cueItemId", description = "큐시트아이템 아이디")
                                                     @PathVariable("cueItemId") Long cueItemId,
                                                     @Parameter(description = "필수값<br> ", required = true)
                                                     @RequestBody @Valid CueSheetItemSymbolUpdateDTO cueSheetItemSymbolUpdateDTO) {

        cueSheetItemSymbolService.update(cueSheetItemSymbolUpdateDTO, cueItemId);

        CueSheetItemSymbolDTO cueSheetItemSymbolDTO = cueSheetItemSymbolService.find(cueItemId);

        return new ApiResponse<CueSheetItemSymbolDTO>(cueSheetItemSymbolDTO);
    }

    @Operation(summary = "큐시트 아이템 심볼 삭제", description = "큐시트 아이템 심볼 삭제")
    @DeleteMapping(path = "/{cueItemSymbolId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> delete(@Parameter(name = "cueItemSymbolId", description = "큐시트아이템 방송아이콘 아이디")
                                     @PathVariable("cueItemSymbolId") Long cueItemSymbolId){

        cueSheetItemSymbolService.delete(cueItemSymbolId);

        return ApiResponse.noContent();
    }
}
