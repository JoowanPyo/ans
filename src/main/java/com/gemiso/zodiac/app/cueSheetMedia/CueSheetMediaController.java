package com.gemiso.zodiac.app.cueSheetMedia;

import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaCreateDTO;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaDTO;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaRequestDTO;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupCreateDTO;
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

    @Operation(summary = "큐시트 미디어 등록", description = "큐시트 미디어 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CueSheetMediaDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                                @RequestBody @Valid CueSheetMediaRequestDTO cueSheetMediaRequestDTO) {

        Long cueMediaId = cueSheetMediaService.create(cueSheetMediaRequestDTO);

        return null;

    }

}
