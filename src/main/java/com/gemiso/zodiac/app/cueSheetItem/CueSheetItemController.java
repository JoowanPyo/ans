package com.gemiso.zodiac.app.cueSheetItem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetSimpleDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.*;
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

@Api(description = "큐시트 아이템 API")
@RestController
@RequestMapping("/cuesheetitems")
@RequiredArgsConstructor
@Slf4j
public class CueSheetItemController {

    private final CueSheetItemService cueSheetItemService;
    /*private final ArticleService articleService;*/

    @Operation(summary = "큐시트 아이템 목록조회", description = "큐시트 아이템 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<CueSheetItemDTO>> findAll(@Parameter(name = "artlcId", description = "기사 아이디")
                                                         @RequestParam(value = "artlcId", required = false) Long artlcId,
                                                         @Parameter(name = "cueId", description = "큐시트 아이디")
                                                         @RequestParam(value = "cueId", required = false) Long cueId,
                                                         @Parameter(name = "delYn", description = "삭제여부 (디폴트 : N)")
                                                         @RequestParam(value = "delYn", required = false) String delYn,
                                                         @Parameter(name = "spareYn", description = "스페어여부 (디폴트 : N)")
                                                         @RequestParam(value = "spareYn", required = false) String spareYn) {

        List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemService.findAll(artlcId, cueId, delYn, spareYn);

        return new AnsApiResponse<>(cueSheetItemDTOList);
    }

    @Operation(summary = "큐시트 아이템 상세정보 조회", description = "큐시트 아이템 상세정보 조회")
    @GetMapping(path = "/{cueItemId}")
    public AnsApiResponse<CueSheetItemDTO> find(@Parameter(name = "cueItemId", description = "큐시트아이템아이디")
                                                @PathVariable("cueItemId") Long cueItemId) {

        CueSheetItemDTO cueSheetItemDTO = cueSheetItemService.find(cueItemId);

        return new AnsApiResponse<>(cueSheetItemDTO);
    }

    @Operation(summary = "큐시트 아이템 템플릿(운영참조) 등록", description = "큐시트 아이템 템플릿(운영참조) 등록")
    @PostMapping(path = "/{cueId}/item/template")
    public AnsApiResponse<List<CueSheetItemDTO>> createTemplate(@Parameter(description = "필수값<br> ", required = true)
                                                                @RequestBody @Valid List<CueSheetItemCreateDTO> cueSheetItemCreateDTOList,
                                                                @Parameter(name = "cueId", description = "큐시트아이디")
                                                                @PathVariable("cueId") Long cueId) throws JsonProcessingException {

        cueSheetItemService.createTemplate(cueSheetItemCreateDTOList, cueId);

        List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemService.findAll(null, cueId, null, null);

        return new AnsApiResponse<>(cueSheetItemDTOList);

    }

    @Operation(summary = "큐시트 아이템 등록", description = "큐시트 아이템 등록")
    @PostMapping(path = "/{cueId}/item")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<CueSheetItemDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                                  @RequestBody @Valid CueSheetItemCreateDTO cueSheetItemCreateDTO,
                                                  @Parameter(name = "cueId", description = "큐시트아이디")
                                                  @PathVariable("cueId") Long cueId) throws JsonProcessingException {

        Long cueItemId = cueSheetItemService.create(cueSheetItemCreateDTO, cueId);

        CueSheetItemDTO cueSheetItemDTO = cueSheetItemService.find(cueItemId);

        return new AnsApiResponse<>(cueSheetItemDTO);
    }

    @Operation(summary = "큐시트 아이템 수정", description = "큐시트 아이템 수정")
    @PutMapping(path = "/{cueId}/item/{cueItemId}")
    public AnsApiResponse<CueSheetItemDTO> update(@Parameter(description = "필수값<br> ", required = true)
                                                  @RequestBody @Valid CueSheetItemUpdateDTO cueSheetItemUpdateDTO,
                                                  @Parameter(name = "cueItemDivCd", description = "큐시트 아이템 구분 코드", required = true)
                                                  @RequestParam(value = "cueItemDivCd") String cueItemDivCd,
                                                  @Parameter(name = "cueId", description = "큐시트아이디")
                                                  @PathVariable("cueId") Long cueId,
                                                  @Parameter(name = "cueItemId", description = "큐시트아이템 아이디")
                                                  @PathVariable("cueItemId") Long cueItemId) throws Exception {

        if (cueItemDivCd == null || "".equals(cueItemDivCd)) {
            
            throw new ResourceNotFoundException("큐시트 아이템 구분 코드가 잘못 되었습니다. 구분코드 : " + cueItemDivCd);
            
            //큐시트 아이템 코드가 cue_item, cue_template일 경우 [큐시트 아이템 수정]
        } else if ("cue_item".equals(cueItemDivCd) || "cue_template".equals(cueItemDivCd)) {
            
            //큐시트 템플릿 Update
            cueSheetItemService.update(cueSheetItemUpdateDTO, cueId, cueItemId);

            //큐시트 아이템 코드가 cue_article일 경우 [큐시트 아이템 수정 & 기사 수정]
        } else if ("cue_article".equals(cueItemDivCd)) {

            //기사복사본 수정.
            cueSheetItemService.updateCueItemArticle(cueSheetItemUpdateDTO, cueId, cueItemId);

        } else {
            throw new ResourceNotFoundException("큐시트 아이템 구분코드를 확인해 주세요. 큐시트 아이템 구분 코드 : " + cueItemDivCd);
        }
        CueSheetItemDTO cueSheetItemDTO = cueSheetItemService.find(cueItemId);

        return new AnsApiResponse<>(cueSheetItemDTO);

    }

    @Operation(summary = "큐시트 아이템 삭제", description = "큐시트 아이템 삭제")
    @DeleteMapping(path = "/{cueId}/item/{cueItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "cueId", description = "큐시트아이디")
                                    @PathVariable("cueId") Long cueId,
                                    @Parameter(name = "cueItemId", description = "큐시트아이템 아이디")
                                    @PathVariable("cueItemId") Long cueItemId) throws Exception {

        cueSheetItemService.delete(cueId, cueItemId);

        return AnsApiResponse.noContent();
    }

    /*@Operation(summary = "큐시트 아이템 순서변경", description = "큐시트 아이템 순서변경")
    @PutMapping(path = "/{cueId}/item/{cueItemId}/ord")
    public AnsApiResponse<List<CueSheetItemDTO>> ordUpdate(@Parameter(name = "cueId", description = "큐시트아이디")
                                                           @PathVariable("cueId") Long cueId,
                                                           @Parameter(name = "cueItemId", description = "큐시트아이템 아이디")
                                                           @PathVariable("cueItemId") Long cueItemId,
                                                           @Parameter(name = "cueItemOrd", description = "큐시트 아이템 순번")
                                                           @RequestParam(value = "cueItemOrd", required = false) int cueItemOrd) {

        cueSheetItemService.ordUpdate(cueId, cueItemId, cueItemOrd);

        List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemService.findAll(null, cueId, null, null);

        return new AnsApiResponse<>(cueSheetItemDTOList);

    }*/

    @Operation(summary = "큐시트 아이템 순서변경", description = "큐시트 아이템 순서변경")
    @PutMapping(path = "/{cueId}/item/change_order")
    public AnsApiResponse<List<CueSheetItemDTO>> ordCdUpdate(@Parameter(name = "cueId", description = "큐시트아이디")
                                                             @PathVariable("cueId") Long cueId,
                                                             @Parameter(name = "spareYn", description = "예비큐시트 여부(N, Y)")
                                                             @RequestParam(value = "spareYn", required = true) String spareYn,
                                                             @Parameter(description = "필수값<br> ", required = true)
                                                             @RequestBody @Valid List<CueSheetItemOrdUpdateDTO> cueSheetItemOrdUpdateDTOList) {
        cueSheetItemService.ordCdUpdate(cueId, spareYn, cueSheetItemOrdUpdateDTOList);

        List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemService.findAll(null, cueId, null, null);

        return new AnsApiResponse<>(cueSheetItemDTOList);

    }

    @Operation(summary = "큐시트 아이템 생성[Drag and Drop]", description = "큐시트 아이템 생성[Drag and Drop]")
    @PostMapping(path = "/{cueId}/item/{artclId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<List<CueSheetItemDTO>> createCueItem(@Parameter(name = "cueId", description = "큐시트아이디")
                                                               @PathVariable("cueId") Long cueId,
                                                               @Parameter(name = "artclId", description = "기사 아이디")
                                                               @PathVariable("artclId") Long artclId,
                                                               @Parameter(name = "cueItemOrd", description = "큐시트 아이템 순번")
                                                               @RequestParam(value = "cueItemOrd", required = false) int cueItemOrd,
                                                               @Parameter(name = "cueItemDivCd", description = "큐시트 아이템 구분 코드")
                                                               @RequestParam(value = "cueItemDivCd", required = false) String cueItemDivCd,
                                                               @Parameter(name = "spareYn", description = "예비큐시트 여부(N, Y)")
                                                               @RequestParam(value = "spareYn", required = false) String spareYn) throws Exception {

        cueSheetItemService.createCueItem(cueId, artclId, cueItemOrd, cueItemDivCd, spareYn);

        List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemService.findAll(null, cueId, null, null);

        return new AnsApiResponse<>(cueSheetItemDTOList);

    }

    @Operation(summary = "큐시트 아이템 생성[Drag and Drop] List", description = "큐시트 아이템 생성[Drag and Drop] List")
    @PostMapping(path = "/{cueId}/item/createList")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<List<CueSheetItemDTO>> createCueItemList(@Parameter(description = "필수값<br> ", required = true)
                                                                   @RequestBody @Valid List<CueSheetItemCreateListDTO> cueSheetItemCreateListDTO,
                                                                   @Parameter(name = "cueId", description = "큐시트아이디")
                                                                   @PathVariable("cueId") Long cueId,
                                                                   @Parameter(name = "spareYn", description = "예비큐시트 여부(N, Y)")
                                                                   @RequestParam(value = "spareYn", required = false) String spareYn) throws Exception {

        cueSheetItemService.createCueItemList(cueSheetItemCreateListDTO, cueId, spareYn);

        List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemService.findAll(null, cueId, null, null);

        return new AnsApiResponse<>(cueSheetItemDTOList);
    }

    @Operation(summary = "예비 큐시트 아이템 수정", description = "예비 큐시트 아이템 수정")
    @PutMapping(path = "/{cueId}/item/{cueItemId}/updatespare")
    public AnsApiResponse<CueSheetItemSimpleDTO> updateSpareCueItem(@Parameter(name = "cueId", description = "큐시트아이디")
                                                                @PathVariable("cueId") Long cueId,
                                                                @Parameter(name = "cueItemId", description = "큐시트아이템 아이디")
                                                                @PathVariable("cueItemId") Long cueItemId,
                                                                @Parameter(name = "cueItemOrd", description = "큐시트 아이템 순번")
                                                                @RequestParam(value = "cueItemOrd", required = false) int cueItemOrd,
                                                                @Parameter(name = "spareYn", description = "예비여부(Y,N)")
                                                                @RequestParam(value = "spareYn", required = false) String spareYn) {

        CueSheetItemSimpleDTO cueSheetItemSimpleDTO = cueSheetItemService.updateSpareCueItem(cueId, cueItemId, cueItemOrd, spareYn);

        return new AnsApiResponse(cueSheetItemSimpleDTO);
    }

    @Operation(summary = "큐시트 아이템 복구", description = "큐시트 아이템 복구")
    @PutMapping(path = "/{cueId}/item/{cueItemId}/restore")
    public AnsApiResponse<CueSheetItemSimpleDTO> cueSheetItemRestore(@Parameter(name = "cueId", description = "큐시트아이디")
                                                                 @PathVariable("cueId") Long cueId,
                                                                 @Parameter(name = "cueItemId", description = "큐시트아이템 아이디")
                                                                 @PathVariable("cueItemId") Long cueItemId,
                                                                 @Parameter(name = "cueItemOrd", description = "큐시트 아이템 순번")
                                                                 @RequestParam(value = "cueItemOrd", required = false) Integer cueItemOrd) throws JsonProcessingException {

        CueSheetItemSimpleDTO cueSheetItemSimpleDTO = cueSheetItemService.cueSheetItemRestore(cueId, cueItemId, cueItemOrd);

        return new AnsApiResponse<>(cueSheetItemSimpleDTO);
    }


}
