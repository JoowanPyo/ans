package com.gemiso.zodiac.app.cueSheetTemplateItem;

import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmplItemCreateDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmplItemDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmplItemSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmplItemUpdateDTO;
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

@Api(description = "큐시트 템플릿 아이템 API")
@RestController
@RequestMapping("/cuetmplitem")
@Slf4j
@RequiredArgsConstructor
public class CueTmplItemController {

    private final CueTmplItemService cueTmplItemService;


    @Operation(summary = "큐시트 템플릿 아이템 목록조회", description = "큐시트 템플릿 아이템 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<CueTmplItemDTO>> findAll(@Parameter(name = "cueTmpltId", description = "큐시트 템플릿 아이디")
                                                        @RequestParam(value = "cueTmpltId", required = false) Long cueTmpltId,
                                                        @Parameter(name = "searchWord", description = "검색키워드[큐시트 템플릿 아이템 제목]")
                                                        @RequestParam(value = "searchWord", required = false) String searchWord) {

        List<CueTmplItemDTO> cueTmplItemDTOList = cueTmplItemService.findAll(cueTmpltId, searchWord);

        return new AnsApiResponse<>(cueTmplItemDTOList);
    }

    @Operation(summary = "큐시트 템플릿 아이템 상세조회", description = "큐시트 템플릿 아이템 상세조회")
    @GetMapping(path = "/{cueTmpltItemId}")
    public AnsApiResponse<CueTmplItemDTO> find(@Parameter(name = "cueTmpltItemId", description = "큐시트 템플릿 아이템 아이디")
                                               @PathVariable("cueTmpltItemId") Long cueTmpltItemId) {

        CueTmplItemDTO cueTmplItemDTO = cueTmplItemService.find(cueTmpltItemId);

        return new AnsApiResponse<>(cueTmplItemDTO);
    }

    @Operation(summary = "큐시트 템플릿 아이템 등록", description = "큐시트 템플릿 아이템 등록")
    @PostMapping(path = "/{cueTmpltId}/item")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<CueTmplItemSimpleDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                                       @RequestBody @Valid CueTmplItemCreateDTO cueTmplItemCreateDTO,
                                                       @Parameter(name = "cueTmpltId", description = "큐시트 템플릿 아이디")
                                                       @PathVariable("cueTmpltId") Long cueTmpltId) {

        CueTmplItemSimpleDTO cueTmplItemSimpleDTO = cueTmplItemService.create(cueTmplItemCreateDTO, cueTmpltId);

        return new AnsApiResponse<>(cueTmplItemSimpleDTO);
    }

    @Operation(summary = "큐시트 템플릿 아이템 수정", description = "큐시트 템플릿 아이템 수정")
    @PutMapping(path = "/{cueTmpltItemId}")
    public AnsApiResponse<CueTmplItemSimpleDTO> update(@Parameter(description = "필수값<br> ", required = true)
                                                       @RequestBody @Valid CueTmplItemUpdateDTO cueTmplItemUpdateDTO,
                                                       @Parameter(name = "cueTmpltItemId", description = "큐시트 템플릿 아이템 아이디")
                                                       @PathVariable("cueTmpltItemId") Long cueTmpltItemId) {

        CueTmplItemSimpleDTO cueTmplItemSimpleDTO = cueTmplItemService.update(cueTmplItemUpdateDTO, cueTmpltItemId);

        return new AnsApiResponse<>(cueTmplItemSimpleDTO);
    }

    @Operation(summary = "큐시트 템플릿 아이템 삭제", description = "큐시트 템플릿 아이템 삭제")
    @DeleteMapping(path = "/{cueTmpltItemId}")
    public AnsApiResponse<?> delete(@Parameter(name = "cueTmpltItemId", description = "큐시트 템플릿 아이템 아이디")
                                    @PathVariable("cueTmpltItemId") Long cueTmpltItemId) {

        cueTmplItemService.delete(cueTmpltItemId);

        return AnsApiResponse.noContent();
    }

    @Operation(summary = "큐시트 템플릿 아이템 순서변경", description = "큐시트 템플릿 아이템 순서변경")
    @PutMapping(path = "/{cueTmpltId}/item/{cueTmpltItemId}/ord")
    public AnsApiResponse<List<CueTmplItemDTO>> ordUpdate(@Parameter(name = "cueTmpltItemId", description = "큐시트 템플릿 아이템 아이디")
                                      @PathVariable("cueTmpltItemId") Long cueTmpltItemId,
                                      @Parameter(name = "cueTmpltId", description = "큐시트 템플릿 아이디")
                                      @PathVariable("cueTmpltId") Long cueTmpltId,
                                      @Parameter(name = "cueItemOrd", description = "큐시트 아이템 순번")
                                      @RequestParam(value = "cueItemOrd", required = false) int cueItemOrd) {

        cueTmplItemService.ordUpdate(cueTmpltItemId, cueTmpltId, cueItemOrd);

        List<CueTmplItemDTO> cueTmplItemDTOList = cueTmplItemService.findAll(cueTmpltId, null);

        return new AnsApiResponse<>(cueTmplItemDTOList);
    }
}
