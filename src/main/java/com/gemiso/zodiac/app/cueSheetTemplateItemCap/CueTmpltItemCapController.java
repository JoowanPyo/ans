package com.gemiso.zodiac.app.cueSheetTemplateItemCap;

import com.gemiso.zodiac.app.cueSheetTemplateItemCap.dto.CueTmpltItemCapCreateDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.dto.CueTmpltItemCapDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.dto.CueTmpltItemCapSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.dto.CueTmpltItemCapUpdateDTO;
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

@Api(description = "큐시트 템플릿 아이템 자막 API")
@RestController
@RequestMapping("/cuetmpltitemcap")
@Slf4j
@RequiredArgsConstructor
public class CueTmpltItemCapController {

    private final CueTmpltItemCapService cueTmpltItemCapService;


    @Operation(summary = "큐시트 템플릿 아이템 자막 목록조회", description = "큐시트 템플릿 아이템 자막 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<CueTmpltItemCapDTO>> findAll(@Parameter(name = "cueTmpltItemId", description = "큐시트 템플릿 아이템 아이디")
                                                            @RequestParam(value = "cueTmpltItemId", required = false) Long cueTmpltItemId,
                                                            @Parameter(name = "cueItemCapDivCd", description = "큐시트 아이템 자막 구분 코드")
                                                            @RequestParam(value = "cueItemCapDivCd", required = false) String cueItemCapDivCd) {

        List<CueTmpltItemCapDTO> cueTmpltItemCapDTOList = cueTmpltItemCapService.findAll(cueTmpltItemId, cueItemCapDivCd);

        return new AnsApiResponse<>(cueTmpltItemCapDTOList);
    }

    @Operation(summary = "큐시트 템플릿 아이템 자막 상세조회", description = "큐시트 템플릿 아이템 자막 상세조회")
    @GetMapping(path = "/{cueItemCapId}")
    public AnsApiResponse<CueTmpltItemCapDTO> find(@Parameter(name = "cueItemCapId", description = "큐시트 템플릿 아이템 자막 아이디")
                                                   @PathVariable("cueItemCapId") Long cueItemCapId) {

        CueTmpltItemCapDTO cueTmpltItemCapDTO = cueTmpltItemCapService.find(cueItemCapId);

        return new AnsApiResponse<>(cueTmpltItemCapDTO);
    }

    @Operation(summary = "큐시트 템플릿 아이템 자막 등록", description = "큐시트 템플릿 아이템 자막 등록")
    @PostMapping(path = "/{cueTmpltItemId}/itemcap")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<CueTmpltItemCapSimpleDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                                           @RequestBody @Valid CueTmpltItemCapCreateDTO cueTmpltItemCapCreateDTO,
                                                           @Parameter(name = "cueTmpltItemId", description = "큐시트 템플릿 아이템 아이디")
                                                           @PathVariable("cueTmpltItemId") Long cueTmpltItemId) {

        //아이디를 담아 리털할 articleDTO
        CueTmpltItemCapSimpleDTO cueTmpltItemCapSimpleDTO = cueTmpltItemCapService.create(cueTmpltItemCapCreateDTO, cueTmpltItemId);

        return new AnsApiResponse<>(cueTmpltItemCapSimpleDTO);

    }

    @Operation(summary = "큐시트 템플릿 아이템 자막 수정", description = "큐시트 템플릿 아이템 자막 수정")
    @PutMapping(path = "/{cueItemCapId}")
    public AnsApiResponse<CueTmpltItemCapSimpleDTO> update(@Parameter(description = "필수값<br> ", required = true)
                                                           @RequestBody @Valid CueTmpltItemCapUpdateDTO cueTmpltItemCapUpdateDTO,
                                                           @Parameter(name = "cueItemCapId", description = "큐시트 템플릿 아이템 자막 아이디")
                                                           @PathVariable("cueItemCapId") Long cueItemCapId) {

        cueTmpltItemCapService.update(cueTmpltItemCapUpdateDTO, cueItemCapId);

        //아이디 리턴.
        CueTmpltItemCapSimpleDTO cueTmpltItemCapSimpleDTO = new CueTmpltItemCapSimpleDTO();
        cueTmpltItemCapSimpleDTO.setCueItemCapId(cueItemCapId);

        return new AnsApiResponse<>(cueTmpltItemCapSimpleDTO);
    }

    @Operation(summary = "큐시트 템플릿 아이템 자막 삭제", description = "큐시트 템플릿 아이템 자막 삭제")
    @DeleteMapping(path = "/{cueItemCapId}")
    public AnsApiResponse<?> delete(@Parameter(name = "cueItemCapId", description = "큐시트 템플릿 아이템 자막 아이디")
                                    @PathVariable("cueItemCapId") Long cueItemCapId) {

        cueTmpltItemCapService.delete(cueItemCapId);

        return AnsApiResponse.noContent();
    }

    @Operation(summary = "큐시트 템플릿 아이템 자막 순서변경", description = "큐시트 템플릿 아이템 자막 순서변경")
    @PutMapping(path = "/{cueTmpltItemId}/cap/{cueItemCapId}/ord")
    public AnsApiResponse<List<CueTmpltItemCapDTO>> updateCapOrd(@Parameter(name = "cueTmpltItemId", description = "큐시트 템플릿 아이템 아이디")
                                                                 @PathVariable("cueTmpltItemId") Long cueTmpltItemId,
                                                                 @Parameter(name = "cueItemCapId", description = "큐시트 템플릿 아이템 자막 아이디")
                                                                 @PathVariable("cueItemCapId") Long cueItemCapId,
                                                                 @Parameter(name = "capOrd", description = "큐시트 템플릿 아이템 자막 순번")
                                                                 @RequestParam(value = "capOrd", required = false) int capOrd) {

        cueTmpltItemCapService.updateCapOrd(cueTmpltItemId, cueItemCapId, capOrd);

        List<CueTmpltItemCapDTO> cueTmpltItemCapDTOList = cueTmpltItemCapService.findAll(cueTmpltItemId, null);

        return new AnsApiResponse<>(cueTmpltItemCapDTOList);
    }

}
