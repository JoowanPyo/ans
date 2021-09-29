package com.gemiso.zodiac.app.capTemplateGrp;

import com.gemiso.zodiac.app.capTemplateGrp.dto.CapTemplateGrpCreateDTO;
import com.gemiso.zodiac.app.capTemplateGrp.dto.CapTemplateGrpDTO;
import com.gemiso.zodiac.app.capTemplateGrp.dto.CapTemplateGrpUpdateDTO;
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

@Api(description = "템플릿 API")
@RestController
@RequestMapping("/templategroups")
@Slf4j
@RequiredArgsConstructor
public class CapTemplateGrpController {

    private final CapTemplateGrpService capTemplateGrpService;

    @Operation(summary = "템플릿 그룹 목록조회", description = "템플릿 그룹 목록조회")
    @GetMapping(path = "")
    public ApiResponse<List<CapTemplateGrpDTO>> findAll(@Parameter(name = "searchWord", description = "템플릿 그룹 이름 검색")
                                                  @RequestParam(value = "searchWord", required = false) String searchWord) {

        List<CapTemplateGrpDTO> capTemplateGrpDTOList = capTemplateGrpService.findAll(searchWord);

        return new ApiResponse<>(capTemplateGrpDTOList);
    }

    @Operation(summary = "템플릿 그룹 상세조회", description = "템플릿 그룹 상세조회")
    @GetMapping(path = "/{tmpltGrpId}")
    public ApiResponse<CapTemplateGrpDTO> find(@Parameter(name = "tmpltGrpId", description = "템플릿 그룹 아이디")
                                         @PathVariable("tmpltGrpId") Long tmpltGrpId) {

        CapTemplateGrpDTO capTemplateGrpDTO = capTemplateGrpService.find(tmpltGrpId);

        return new ApiResponse<>(capTemplateGrpDTO);

    }

    @Operation(summary = "템플릿 그룹 등록", description = "템플릿 그룹 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CapTemplateGrpDTO> create(@Parameter(name = "capTemplateGrpCreateDTO", required = true, description = "필수값<br>")
                                           @Valid @RequestBody CapTemplateGrpCreateDTO capTemplateGrpCreateDTO) {

        Long tmpltGrpId = capTemplateGrpService.create(capTemplateGrpCreateDTO);

        CapTemplateGrpDTO capTemplateGrpDTO = capTemplateGrpService.find(tmpltGrpId);

        return new ApiResponse<>(capTemplateGrpDTO);

    }

    @Operation(summary = "템플릿 그룹 수정", description = "템플릿 그룹 수정")
    @PutMapping(path = "/{tmpltGrpId}")
    public ApiResponse<CapTemplateGrpDTO> update(@Parameter(name = "tmpltGrpId", description = "템플릿 그룹 아이디")
                                           @PathVariable("tmpltGrpId") Long tmpltGrpId,
                                                 @Parameter(name = "capTemplateGrpUpdateDTO", required = true, description = "필수값<br>")
                                           @Valid @RequestBody CapTemplateGrpUpdateDTO capTemplateGrpUpdateDTO) {

        capTemplateGrpService.update(capTemplateGrpUpdateDTO, tmpltGrpId);

        CapTemplateGrpDTO capTemplateGrpDTO = capTemplateGrpService.find(tmpltGrpId);

        return new ApiResponse<>(capTemplateGrpDTO);

    }

    @Operation(summary = "템플릿 그룹 삭제", description = "템플릿 그룹 삭제")
    @DeleteMapping(path = "/{tmpltGrpId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> delete(@Parameter(name = "tmpltGrpId", description = "템플릿 그룹 아이디")
                                 @PathVariable("tmpltGrpId") Long tmpltGrpId) {

        capTemplateGrpService.delete(tmpltGrpId);

        return ApiResponse.noContent();
    }
}
