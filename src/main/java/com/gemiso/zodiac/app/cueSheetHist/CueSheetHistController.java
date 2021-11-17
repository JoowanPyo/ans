package com.gemiso.zodiac.app.cueSheetHist;

import com.gemiso.zodiac.app.cueSheetHist.dto.CueSheetHistDTO;
import com.gemiso.zodiac.core.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "큐시트 이력 API")
@RestController
@RequestMapping("/cuesheethist")
@RequiredArgsConstructor
@Slf4j
public class CueSheetHistController {

    private final CueSheetHistService cueSheetHistService;


    @Operation(summary = "큐시트 이력 목록조회", description = "큐시트 이력 목록조회")
    @GetMapping(path = "")
    public ApiResponse<List<CueSheetHistDTO>> findAll(@Parameter(name = "cueId", description = "큐시트 아이디")
                                                      @RequestParam(value = "cueId", required = false) Long cueId,
                                                      @Parameter(name = "cueAction", description = "큐시트 액션")
                                                      @RequestParam(value = "cueAction", required = false) String cueAction) {

        List<CueSheetHistDTO> cueSheetHistDTOList = cueSheetHistService.findAll(cueId, cueAction);

        return new ApiResponse<>(cueSheetHistDTOList);
    }

    @Operation(summary = "큐시트 이력 상세조회", description = "큐시트 이력 상세조회")
    @GetMapping(path = "/{cueHistId}")
    public ApiResponse<CueSheetHistDTO> find(@Parameter(name = "cueHistId", description = "큐시트 이력 아이디")
                                             @PathVariable("cueHistId") Long cueHistId) {

        CueSheetHistDTO cueSheetHistDTO = cueSheetHistService.find(cueHistId);

        return new ApiResponse<>(cueSheetHistDTO);
    }
}
