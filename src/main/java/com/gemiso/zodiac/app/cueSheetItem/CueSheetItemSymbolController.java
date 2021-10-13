package com.gemiso.zodiac.app.cueSheetItem;

import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemCreateDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSimpleDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSymbolCreateDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSymbolDTO;
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
@RequestMapping("/cuesheetitemsymbol")
@RequiredArgsConstructor
@Slf4j
public class CueSheetItemSymbolController {

    private final CueSheetItemSymbolService cueSheetItemSymbolService;


    @Operation(summary = "큐시트 아이템 심볼 상세조회", description = "큐시트 아이템 심볼 상세조회")
    @GetMapping(path = "/{cueItemId}")
    public ApiResponse<CueSheetItemSymbolDTO> find(@Parameter(name = "cueItemId", description = "큐시트아이템 아이디")
                                                       @PathVariable("cueItemId") Long cueItemId){

        CueSheetItemSymbolDTO cueSheetItemSymbolDTO = cueSheetItemSymbolService.find(cueItemId);

        return new ApiResponse<CueSheetItemSymbolDTO>(cueSheetItemSymbolDTO);
    }

    @Operation(summary = "큐시트 아이템 심볼 등록", description = "큐시트 아이템 심볼 등록")
    @PostMapping(path = "/{cueItemId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CueSheetItemSymbolDTO> create(@Parameter(name = "cueItemId", description = "큐시트아이템 아이디")
                                                     @PathVariable("cueItemId") Long cueItemId,
                                                     @Parameter(description = "필수값<br> ", required = true)
                                                     @RequestBody @Valid CueSheetItemSymbolCreateDTO cueSheetItemSymbolCreateDTO) {

        cueSheetItemSymbolService.create(cueSheetItemSymbolCreateDTO, cueItemId);

        CueSheetItemSymbolDTO cueSheetItemSymbolDTO = cueSheetItemSymbolService.find(cueItemId);

        return new ApiResponse<CueSheetItemSymbolDTO>(cueSheetItemSymbolDTO);
    }
}
