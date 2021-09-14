package com.gemiso.zodiac.app.template;

import com.gemiso.zodiac.app.template.dto.TemplateCreateDTO;
import com.gemiso.zodiac.app.template.dto.TemplateDTO;
import com.gemiso.zodiac.app.template.dto.TemplateUpdateDTO;
import com.gemiso.zodiac.app.user.dto.UserCreateDTO;
import com.gemiso.zodiac.core.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(description = "템플릿 API")
@RestController
@RequestMapping("/templategroups")
@Slf4j
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @Operation(summary = "템플릿 그룹 목록조회", description = "템플릿 그룹 목록조회")
    @GetMapping(path = "")
    public ApiResponse<List<TemplateDTO>> findAll(@Parameter(name = "searchWord", description = "템플릿 그룹 이름 검색")
                                                  @RequestParam(value = "searchWord", required = false) String searchWord) {

        List<TemplateDTO> templateDTOList = templateService.findAll(searchWord);

        return new ApiResponse<>(templateDTOList);
    }

    @Operation(summary = "템플릿 그룹 상세조회", description = "템플릿 그룹 상세조회")
    @GetMapping(path = "/{tmpltGrpId}")
    public ApiResponse<TemplateDTO> find(@Parameter(name = "tmpltGrpId", description = "템플릿 그룹 아이디")
                                         @PathVariable("tmpltGrpId") Long tmpltGrpId) {

        TemplateDTO templateDTO = templateService.find(tmpltGrpId);

        return new ApiResponse<>(templateDTO);

    }

    @Operation(summary = "템플릿 그룹 등록", description = "템플릿 그룹 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TemplateDTO> create(@Parameter(name = "templateCreateDTO", required = true, description = "필수값<br>")
                                           @Valid @RequestBody TemplateCreateDTO templateCreateDTO) {

        Long tmpltGrpId = templateService.create(templateCreateDTO);

        TemplateDTO templateDTO = templateService.find(tmpltGrpId);

        return new ApiResponse<>(templateDTO);

    }

    @Operation(summary = "템플릿 그룹 수정", description = "템플릿 그룹 수정")
    @PutMapping(path = "/{tmpltGrpId}")
    public ApiResponse<TemplateDTO> update(@Parameter(name = "tmpltGrpId", description = "템플릿 그룹 아이디")
                                           @PathVariable("tmpltGrpId") Long tmpltGrpId,
                                           @Parameter(name = "templateUpdateDTO", required = true, description = "필수값<br>")
                                           @Valid @RequestBody TemplateUpdateDTO templateUpdateDTO) {

        templateService.update(templateUpdateDTO, tmpltGrpId);

        TemplateDTO templateDTO = templateService.find(tmpltGrpId);

        return new ApiResponse<>(templateDTO);

    }

    @Operation(summary = "템플릿 그룹 삭제", description = "템플릿 그룹 삭제")
    @DeleteMapping(path = "/{tmpltGrpId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> delete(@Parameter(name = "tmpltGrpId", description = "템플릿 그룹 아이디")
                                 @PathVariable("tmpltGrpId") Long tmpltGrpId) {

        templateService.delete(tmpltGrpId);

        return ApiResponse.noContent();
    }
}
