package com.gemiso.zodiac.app.cueSheetTemplate;

import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateCreateDTO;
import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateDTO;
import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateUpdateDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
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
    public AnsApiResponse<List<CueSheetTemplateDTO>> findAll(@Parameter(name = "searchWord", description = "검색키워드")
                                                             @RequestParam(value = "searchWord", required = false) String searchWord,
                                                             @Parameter(name = "brdcPgmId", description = "방송프로그램 아이디")
                                                             @RequestParam(value = "brdcPgmId", required = false) String brdcPgmId,
                                                             @Parameter(name = "basPgmschId", description = "기본편성 아이디")
                                                             @RequestParam(value = "basPgmschId", required = false) Long basPgmschId
    ) {

        List<CueSheetTemplateDTO> cueSheetTemplateDTOList = cueSheetTemplateService.findAll(searchWord, brdcPgmId, basPgmschId);

        return new AnsApiResponse<>(cueSheetTemplateDTOList);

    }

    @Operation(summary = "큐시트 템플릿 상세조회", description = "큐시트 템플릿 상세조회")
    @GetMapping(path = "/{cueTmpltId}")
    public AnsApiResponse<CueSheetTemplateDTO> find(@Parameter(name = "cueTmpltId", required = true, description = "큐시트 템플릿 아이디")
                                                    @PathVariable("cueTmpltId") Long cueTmpltId) {

        CueSheetTemplateDTO cueSheetTemplateDTO = cueSheetTemplateService.find(cueTmpltId);

        return new AnsApiResponse<>(cueSheetTemplateDTO);

    }

    @Operation(summary = "큐시트 템플릿 등록", description = "큐시트 템플릿 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<CueSheetTemplateDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                                      @RequestBody @Valid CueSheetTemplateCreateDTO cueSheetTemplateCreateDTO) {

        Long cueTmpltId = cueSheetTemplateService.create(cueSheetTemplateCreateDTO);

        CueSheetTemplateDTO cueSheetTemplateDTO = cueSheetTemplateService.find(cueTmpltId);

        return new AnsApiResponse<>(cueSheetTemplateDTO);
    }

    @Operation(summary = "큐시트 템플릿 수정", description = "큐시트 템플릿 수정")
    @PutMapping(path = "/{cueTmpltId}")
    public AnsApiResponse<CueSheetTemplateDTO> update(@Parameter(name = "cueTmpltId", required = true, description = "큐시트 템플릿 아이디")
                                                      @PathVariable("cueTmpltId") Long cueTmpltId,
                                                      @Parameter(description = "필수값<br> ", required = true)
                                                      @RequestBody @Valid CueSheetTemplateUpdateDTO cueSheetTemplateUpdateDTO) {

        cueSheetTemplateService.update(cueTmpltId, cueSheetTemplateUpdateDTO);

        CueSheetTemplateDTO cueSheetTemplateDTO = cueSheetTemplateService.find(cueTmpltId);

        return new AnsApiResponse<>(cueSheetTemplateDTO);

    }

    @Operation(summary = "큐시트 템플릿 삭제", description = "큐시트 템플릿 삭제")
    @DeleteMapping(path = "/{cueTmpltId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "cueTmpltId", required = true, description = "큐시트 템플릿 아이디")
                                    @PathVariable("cueTmpltId") Long cueTmpltId) {

        cueSheetTemplateService.delete(cueTmpltId);

        return AnsApiResponse.noContent();
    }
}
