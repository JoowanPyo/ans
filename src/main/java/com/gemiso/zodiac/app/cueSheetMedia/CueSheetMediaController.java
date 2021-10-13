package com.gemiso.zodiac.app.cueSheetMedia;

import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaCreateDTO;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaDTO;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaUpdateDTO;
import com.gemiso.zodiac.core.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(description = "큐시트 미디어 API")
@RestController
@RequestMapping("/cuesheetmedia")
@Slf4j
@RequiredArgsConstructor
public class CueSheetMediaController {

    private final CueSheetMediaService cueSheetMediaService;

    @Operation(summary = "큐시트 미디어 상세조회", description = "큐시트 미디어 상세조회")
    @GetMapping("/{cueMediaId}")
    public ApiResponse<CueSheetMediaDTO> find(@Parameter(name = "cueMediaId", required = true, description = "큐시트 미디어 아이디")
                                              @PathVariable("cueMediaId") long cueMediaId) {

        CueSheetMediaDTO cueSheetMediaDTO = cueSheetMediaService.find(cueMediaId);

        return new ApiResponse<>(cueSheetMediaDTO);
    }

    @Operation(summary = "큐시트 미디어 등록", description = "큐시트 미디어 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CueSheetMediaDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                                @RequestBody @Valid CueSheetMediaCreateDTO cueSheetMediaCreateDTO) {

        Long cueMediaId = cueSheetMediaService.create(cueSheetMediaCreateDTO);

        CueSheetMediaDTO cueSheetMediaDTO = cueSheetMediaService.find(cueMediaId);

        return new ApiResponse<>(cueSheetMediaDTO);

    }

    @Operation(summary = "큐시트 미디어 수정", description = "큐시트 미디어 수정")
    @PutMapping(path = "/{cueMediaId}")
    public ApiResponse<CueSheetMediaDTO> update(@Parameter(description = "필수값<br> ", required = true)
                                                @RequestBody @Valid CueSheetMediaUpdateDTO cueSheetMediaUpdateDTO,
                                                @Parameter(name = "cueMediaId", required = true, description = "큐시트 미디어 아이디")
                                                @PathVariable("cueMediaId") long cueMediaId) {

        cueSheetMediaService.update(cueSheetMediaUpdateDTO, cueMediaId);

        CueSheetMediaDTO cueSheetMediaDTO = cueSheetMediaService.find(cueMediaId);

        return new ApiResponse<>(cueSheetMediaDTO);

    }

    @Operation(summary = "큐시트 미디어 삭제", description = "큐시트 미디어 삭제")
    @DeleteMapping(path = "/{cueMediaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> delete(@Parameter(name = "cueMediaId", required = true, description = "큐시트 미디어 아이디")
                                 @PathVariable("cueMediaId") long cueMediaId) {

        cueSheetMediaService.delete(cueMediaId);

        return ApiResponse.noContent();
    }

}
