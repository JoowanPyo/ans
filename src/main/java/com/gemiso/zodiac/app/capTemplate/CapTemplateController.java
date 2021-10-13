package com.gemiso.zodiac.app.capTemplate;

import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateCreateDTO;
import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateDTO;
import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateUpdateDTO;
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

@Api(description = "자막템플릿 API")
@RestController
@RequestMapping("/caps")
@Slf4j
@RequiredArgsConstructor
public class CapTemplateController {

    private final CapTemplateService capTemplateService;

    @Operation(summary = "자막 템플릿 조회", description = "자막 템플릿 조회")
    @GetMapping(path = "")
    public ApiResponse<List<CapTemplateDTO>> findAll(@Parameter(name = "brdc_pgm_id", description = "방송프로그램 아이디")
                                                     @RequestParam(value = "brdc_pgm_id", required = false) Long brdc_pgm_id,
                                                     @Parameter(name = "cap_class_cd", description = "자막 분류 코드")
                                                     @RequestParam(value = "cap_class_cd", required = false) String cap_class_cd,
                                                     @Parameter(name = "use_yn", description = "사용여부")
                                                     @RequestParam(value = "use_yn", required = false) String use_yn,
                                                     @Parameter(name = "search_word", description = "검색어")
                                                     @RequestParam(value = "search_word", required = false) String search_word
    ) {

        List<CapTemplateDTO> capTemplateDTOList = capTemplateService.findAll(brdc_pgm_id, cap_class_cd, use_yn, search_word);

        return new ApiResponse<>(capTemplateDTOList);

    }

    @Operation(summary = "자막 템플릿 상세정보 조회", description = "자막 템플릿 상세정보 조회")
    @GetMapping(path = "/{capTmpltId}")
    public ApiResponse<CapTemplateDTO> find(@Parameter(name = "capTmpltId", description = "자막 템플릿 아이디")
                                            @PathVariable("capTmpltId") Long capTmpltId) {

        CapTemplateDTO capTemplateDTO = capTemplateService.find(capTmpltId);

        return new ApiResponse<>(capTemplateDTO);

    }

    @Operation(summary = "자막 템플릿 등록", description = "자막 템플릿 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CapTemplateDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                              @RequestBody @Valid CapTemplateCreateDTO capTemplateCreateDTO) {

        Long capTmpltId = capTemplateService.create(capTemplateCreateDTO);

        CapTemplateDTO capTemplateDTO = capTemplateService.find(capTmpltId);

        return new ApiResponse<>(capTemplateDTO);

    }

    @Operation(summary = "자막 템플릿 수정", description = "자막 템플릿 수정")
    @PutMapping(path = "/{capTmpltId}")
    public ApiResponse<CapTemplateDTO> update(@Parameter(description = "필수값<br> ", required = true)
                                              @RequestBody @Valid CapTemplateUpdateDTO capTemplateUpdateDTO,
                                              @Parameter(name = "capTmpltId", description = "자막 템플릿 아이디")
                                              @PathVariable("capTmpltId") Long capTmpltId) {

        capTemplateService.update(capTemplateUpdateDTO, capTmpltId);

        CapTemplateDTO capTemplateDTO = capTemplateService.find(capTmpltId);

        return new ApiResponse<>(capTemplateDTO);

    }

    @Operation(summary = "자막 템플릿 삭제", description = "자막 템플릿 삭제")
    @DeleteMapping(path = "/{capTmpltId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> delete(@Parameter(name = "capTmpltId", description = "자막 템플릿 아이디")
                                 @PathVariable("capTmpltId") Long[] capTmpltId) {

        capTemplateService.delete(capTmpltId);

        return ApiResponse.noContent();
    }


}
