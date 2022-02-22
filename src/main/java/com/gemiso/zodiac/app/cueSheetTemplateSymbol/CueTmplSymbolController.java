package com.gemiso.zodiac.app.cueSheetTemplateSymbol;

import com.gemiso.zodiac.app.cueSheetTemplateSymbol.dto.CueTmplSymbolCreateDTO;
import com.gemiso.zodiac.app.cueSheetTemplateSymbol.dto.CueTmplSymbolDTO;
import com.gemiso.zodiac.app.cueSheetTemplateSymbol.dto.CueTmplSymbolSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplateSymbol.dto.CueTmplSymbolUpdateDTO;
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

@Api(description = "큐시트 템플릿 방송아이콘 API")
@RestController
@RequestMapping("/cuetmplsymbol")
@Slf4j
@RequiredArgsConstructor
public class CueTmplSymbolController {

    private final CueTmplSymbolService cueTmplSymbolService;

    @Operation(summary = "큐시트 템플릿 방송아이콘 목록조회", description = "큐시트 템플릿 방송아이콘 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<CueTmplSymbolDTO>> findAll(@Parameter(name = "cueTmpltItemId", description = "큐시트 템플릿 아이템 아이디")
                                                          @RequestParam(value = "cueTmpltItemId", required = false) Long cueTmpltItemId) {

        List<CueTmplSymbolDTO> cueTmplSymbolDTOList = cueTmplSymbolService.findAll(cueTmpltItemId);

        return new AnsApiResponse<>(cueTmplSymbolDTOList);
    }

    @Operation(summary = "큐시트 템플릿 방송아이콘 단건조회", description = "큐시트 템플릿 방송아이콘 단건조회")
    @GetMapping(path = "/{id}")
    public AnsApiResponse<CueTmplSymbolDTO> find(@Parameter(name = "id", required = true, description = "큐시트 템플릿 아이템 방송아이콘 아이디")
                                                 @PathVariable("id") Long id) {

        CueTmplSymbolDTO cueTmplSymbolDTO = cueTmplSymbolService.find(id);

        return new AnsApiResponse<>(cueTmplSymbolDTO);

    }

    @Operation(summary = "큐시트 템플릿 방송아이콘 등록", description = "큐시트 템플릿 방송아이콘 등록")
    @PostMapping(path = "/{cueTmpltItemId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<CueTmplSymbolSimpleDTO> create(@Parameter(name = "cueTmpltItemId", required = true, description = "큐시트 템플릿 아이템 아이디")
                                                   @PathVariable("cueTmpltItemId") Long cueTmpltItemId,
                                                   @Parameter(description = "필수값<br> ", required = true)
                                                   @RequestBody @Valid CueTmplSymbolCreateDTO cueTmplSymbolCreateDTO) {

        CueTmplSymbolSimpleDTO cueTmplSymbolSimpleDTO = cueTmplSymbolService.create(cueTmpltItemId, cueTmplSymbolCreateDTO);

        return new AnsApiResponse<>(cueTmplSymbolSimpleDTO);
    }

    @Operation(summary = "큐시트 템플릿 방송아이콘 수정", description = "큐시트 템플릿 방송아이콘 수정")
    @PutMapping(path = "/{cueTmpltItemId}")
    public AnsApiResponse<CueTmplSymbolSimpleDTO> update(@Parameter(name = "cueTmpltItemId", description = "큐시트 템플릿 아이템 아이디")
                                    @PathVariable("cueTmpltItemId") Long cueTmpltItemId,
                                    @Parameter(description = "필수값<br> ", required = true)
                                    @RequestBody @Valid CueTmplSymbolUpdateDTO cueTmplSymbolUpdateDTO) {

        CueTmplSymbolSimpleDTO cueTmplSymbolSimpleDTO = cueTmplSymbolService.update(cueTmpltItemId, cueTmplSymbolUpdateDTO);

        return new AnsApiResponse<>(cueTmplSymbolSimpleDTO);
    }

    @Operation(summary = "큐시트 템플릿 방송아이콘 삭제", description = "큐시트 템플릿 방송아이콘 삭제")
    @DeleteMapping(path = "/{id}")
    public AnsApiResponse<?> delete(@Parameter(name = "id", required = true, description = "큐시트 템플릿 아이템 방송아이콘 아이디")
                                    @PathVariable("id") Long id) {

        cueTmplSymbolService.delete(id);

        return AnsApiResponse.noContent();
    }

    /*@Operation(summary = "큐시트 템플릿 방송아이콘 등록List", description = "큐시트 템플릿 방송아이콘 등록List")
    @PostMapping(path = "/{cueTmpltItemId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<CueTmplSymbolDTO> create(@Parameter(name = "cueTmpltItemId", required = true, description = "큐시트 템플릿 아이템 아이디")
                                                   @PathVariable("cueTmpltItemId") Long cueTmpltItemId,
                                                   @Parameter(description = "필수값<br> ", required = true)
                                                   @RequestBody @Valid List<CueTmplSymbolCreateDTO> cueTmplSymbolCreateDTO) {

        cueTmplSymbolService.create(cueTmpltItemId, cueTmplSymbolCreateDTO);

        return AnsApiResponse.ok();
    }*/
}
