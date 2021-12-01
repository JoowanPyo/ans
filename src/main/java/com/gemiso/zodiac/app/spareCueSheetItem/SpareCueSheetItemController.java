package com.gemiso.zodiac.app.spareCueSheetItem;

import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemUpdateDTO;
import com.gemiso.zodiac.app.program.dto.ProgramCreateDTO;
import com.gemiso.zodiac.app.spareCueSheetItem.dto.SpareCueSheetItemCreateDTO;
import com.gemiso.zodiac.app.spareCueSheetItem.dto.SpareCueSheetItemDTO;
import com.gemiso.zodiac.app.spareCueSheetItem.dto.SpareCueSheetItemSimpleDTO;
import com.gemiso.zodiac.app.spareCueSheetItem.dto.SpareCueSheetItemUpdateDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(description = "예비 큐시트 아이템 API")
@RestController
@RequestMapping("/sparecuesheetitem")
@Slf4j
@RequiredArgsConstructor
public class SpareCueSheetItemController {

    private final SpareCueSheetItemService spareCueSheetItemService;


    @Operation(summary = "예비 큐시트 아이템 목록조회", description = "예비 큐시트 아이템 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<SpareCueSheetItemDTO>> findAll(@Parameter(name = "cueId", description = "기사 아이디")
                                                              @RequestParam(value = "cueId", required = false) Long cueId) {

        List<SpareCueSheetItemDTO> spareCueSheetItemDTOList = spareCueSheetItemService.findAll(cueId);

        return new AnsApiResponse<>(spareCueSheetItemDTOList);
    }

    @Operation(summary = "예비 큐시트 아이템 상세조회", description = "예비 큐시트 아이템 상세조회")
    @GetMapping(path = "/{spareCueItemId}")
    public AnsApiResponse<SpareCueSheetItemDTO> find(@Parameter(name = "spareCueItemId", description = "예비 큐시트 아이템 아이디", required = true)
                                                     @PathVariable("spareCueItemId") Long spareCueItemId) {

        SpareCueSheetItemDTO spareCueSheetItemDTO = spareCueSheetItemService.find(spareCueItemId);

        return new AnsApiResponse<>(spareCueSheetItemDTO);
    }

    @Operation(summary = "예비 큐시트 아이템 등록", description = "예비 큐시트 아이템 등록")
    @PostMapping(path = "/{cueId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<SpareCueSheetItemSimpleDTO> create(@Parameter(name = "spareCueSheetItemCreateDTO", required = true, description = "필수값<br>  , ")
                                                             @Valid @RequestBody SpareCueSheetItemCreateDTO spareCueSheetItemCreateDTO,
                                                             @Parameter(name = "cueId", description = "큐시트 아이디", required = true)
                                                             @PathVariable("cueId") Long cueId) {

        SpareCueSheetItemSimpleDTO spareCueSheetItemSimpleDTO = spareCueSheetItemService.create(spareCueSheetItemCreateDTO, cueId);

        return new AnsApiResponse<>(spareCueSheetItemSimpleDTO);
    }

    /*@Operation(summary = "예비 큐시트 아이템 수정", description = "예비 큐시트 아이템 수정")
    @PutMapping(path = "/{cueId}/item/{cueItemId}")
    public AnsApiResponse<SpareCueSheetItemSimpleDTO> update(@Parameter(description = "필수값<br> ", required = true)
                                                             @RequestBody @Valid SpareCueSheetItemUpdateDTO spareCueSheetItemUpdateDTO,
                                                             @Parameter(name = "cueItemDivCd", description = "큐시트 아이템 구분 코드[cuearticle]", required = true)
                                                             @RequestParam(value = "cueItemDivCd") String cueItemDivCd,
                                                             @Parameter(name = "cueId", description = "큐시트아이디")
                                                             @PathVariable("cueId") Long cueId,
                                                             @Parameter(name = "cueItemId", description = "큐시트아이템 아이디")
                                                             @PathVariable("cueItemId") Long cueItemId) {

        if (cueItemDivCd == null || "".equals(cueItemDivCd)) {
            throw new ResourceNotFoundException("큐시트 아이템 구분 코드가 잘못 되었습니다. 구분코드 : " + cueItemDivCd);
        } else if (cueItemDivCd.equals("cueitem") || cueItemDivCd.equals("cuetemplate")) {
            //큐시트 템플릿 Update는 들어오면 에러, 예비 큐시트 아이템은 기사만,
            throw new ResourceNotFoundException("큐시트 아이템 구분 코드가 잘못 되었습니다. 구분코드 : " + cueItemDivCd);
        } else if (cueItemDivCd.equals("cuearticle")) {
            //기사복사본 수정.
            spareCueSheetItemService.update(cueSheetItemUpdateDTO.getArticle(), cueSheetItemUpdateDTO.getArticle().getArtclId());
        }
        CueSheetItemDTO cueSheetItemDTO = cueSheetItemService.find(cueItemId);

        return new AnsApiResponse<>(cueSheetItemDTO);

    }*/
}
