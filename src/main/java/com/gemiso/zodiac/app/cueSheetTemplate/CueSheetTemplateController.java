package com.gemiso.zodiac.app.cueSheetTemplate;

import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateCreateDTO;
import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateDTO;
import com.gemiso.zodiac.core.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(description = "큐시트 템플릿 API")
@RestController
@RequestMapping("/cuesheetemplate")
@Slf4j
@RequiredArgsConstructor
public class CueSheetTemplateController {

    private final CueSheetTemplateService cueSheetTemplateService;


    @Operation(summary = "큐시트 템플릿 목록조회", description = "큐시트 템플릿 목록조회")
    @GetMapping(path = "")
    public ApiResponse<List<CueSheetTemplateDTO>> findAll(@Parameter(name = "searchWord", description = "검색키워드")
                                                          @RequestParam(value = "searchWord", required = false) String searchWord,
                                                          @Parameter(name = "pgmschTime", description = "검색키워드")
                                                          @RequestParam(value = "pgmschTime", required = false) String pgmschTime
                                                          ) {

        List<CueSheetTemplateDTO> cueSheetTemplateDTOList = cueSheetTemplateService.findAll(searchWord, pgmschTime);

        return new ApiResponse<>(cueSheetTemplateDTOList);

    }

    @Operation(summary = "큐시트 템플릿 상세조회", description = "큐시트 템플릿 상세조회")
    @GetMapping(path = "/{cueTmpltId}")
    public ApiResponse<CueSheetTemplateDTO> find(@Parameter(name = "cueTmpltId", required = true, description = "큐시트 템플릿 아이디")
                                                 @PathVariable("cueTmpltId") long cueTmpltId) {

        CueSheetTemplateDTO cueSheetTemplateDTO = cueSheetTemplateService.find(cueTmpltId);

        return new ApiResponse<>(cueSheetTemplateDTO);

    }

    @Operation(summary = "큐시트 템플릿 등록", description = "큐시트 템플릿 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CueSheetTemplateDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                                   @RequestBody @Valid CueSheetTemplateCreateDTO cueSheetTemplateCreateDTO) {

        Long cueTmpltId = cueSheetTemplateService.create(cueSheetTemplateCreateDTO);

        CueSheetTemplateDTO cueSheetTemplateDTO = cueSheetTemplateService.find(cueTmpltId);

        return new ApiResponse<>(cueSheetTemplateDTO);
    }
}
