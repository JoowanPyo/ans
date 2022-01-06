package com.gemiso.zodiac.app.cueSheetItem;

import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemCreateDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemCreateListDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemUpdateDTO;
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
                                                         @RequestParam(value = "delYn", required = false) String delYn) {

        List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemService.findAll(artlcId, cueId, delYn);

        return new AnsApiResponse<>(cueSheetItemDTOList);
    }

    @Operation(summary = "큐시트 아이템 상세정보 조회", description = "큐시트 아이템 상세정보 조회")
    @GetMapping(path = "/{cueItemId}")
    public AnsApiResponse<CueSheetItemDTO> find(@Parameter(name = "cueItemId", description = "큐시트아이템아이디")
                                                @PathVariable("cueItemId") Long cueItemId) {

        CueSheetItemDTO cueSheetItemDTO = cueSheetItemService.find(cueItemId);

        return new AnsApiResponse<>(cueSheetItemDTO);
    }

    @Operation(summary = "큐시트 아이템 등록", description = "큐시트 아이템 등록")
    @PostMapping(path = "/{cueId}/item")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<CueSheetItemDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                                  @RequestBody @Valid CueSheetItemCreateDTO cueSheetItemCreateDTO,
                                                  @Parameter(name = "cueId", description = "큐시트아이디")
                                                  @PathVariable("cueId") Long cueId) {

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
        } else if (cueItemDivCd.equals("cue_item") || cueItemDivCd.equals("cue_template")) {
            //큐시트 템플릿 Update
            cueSheetItemService.update(cueSheetItemUpdateDTO, cueId, cueItemId);
        } else if (cueItemDivCd.equals("cue_article")) {
            //기사복사본 수정.
            cueSheetItemService.updateCueItemArticle(cueSheetItemUpdateDTO, cueId, cueItemId);
            /*articleService.update(cueSheetItemUpdateDTO.getArticle(), cueSheetItemUpdateDTO.getArticle().getArtclId());*/
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
                                    @PathVariable("cueItemId") Long cueItemId) {

        cueSheetItemService.delete(cueId, cueItemId);

        return AnsApiResponse.noContent();
    }

    @Operation(summary = "큐시트 아이템 순서변경", description = "큐시트 아이템 순서변경")
    @PutMapping(path = "/{cueId}/item/{cueItemId}/ord")
    public AnsApiResponse<List<CueSheetItemDTO>> ordUpdate(@Parameter(name = "cueId", description = "큐시트아이디")
                                                           @PathVariable("cueId") Long cueId,
                                                           @Parameter(name = "cueItemId", description = "큐시트아이템 아이디")
                                                           @PathVariable("cueItemId") Long cueItemId,
                                                           @Parameter(name = "cueItemOrd", description = "큐시트 아이템 순번")
                                                           @RequestParam(value = "cueItemOrd", required = false) int cueItemOrd) {

        cueSheetItemService.ordUpdate(cueId, cueItemId, cueItemOrd);

        List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemService.findAll(null, cueId, null);

        return new AnsApiResponse<>(cueSheetItemDTOList);

    }

    @Operation(summary = "큐시트 아이템 생성[Drag and Drop]", description = "큐시트 아이템 생성[Drag and Drop]")
    @PostMapping(path = "/{cueId}/item/{artclId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<List<CueSheetItemDTO>> createCueItem(@Parameter(name = "cueId", description = "큐시트아이디")
                                                               @PathVariable("cueId") Long cueId,
                                                               @Parameter(name = "artclId", description = "기사 아이디")
                                                               @PathVariable("artclId") Long artclId,
                                                               @Parameter(name = "cueItemOrd", description = "기사아이디")
                                                               @RequestParam(value = "cueItemOrd", required = false) int cueItemOrd,
                                                               @Parameter(name = "cueItemDivCd", description = "큐시트 아이템 구분 코드")
                                                               @RequestParam(value = "cueItemDivCd", required = false) String cueItemDivCd) {

        cueSheetItemService.createCueItem(cueId, artclId, cueItemOrd, cueItemDivCd);

        List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemService.findAll(null, cueId, null);

        return new AnsApiResponse<>(cueSheetItemDTOList);

    }

    @Operation(summary = "큐시트 아이템 생성[Drag and Drop] List", description = "큐시트 아이템 생성[Drag and Drop] List")
    @PostMapping(path = "/{cueId}/item/createList")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<List<CueSheetItemDTO>> createCueItemList(@Parameter(description = "필수값<br> ", required = true)
                                                                   @RequestBody @Valid List<CueSheetItemCreateListDTO> cueSheetItemCreateListDTO,
                                                                   @Parameter(name = "cueId", description = "큐시트아이디")
                                                                   @PathVariable("cueId") Long cueId) {

        cueSheetItemService.createCueItemList(cueSheetItemCreateListDTO, cueId);

        List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemService.findAll(null, cueId, null);

        return new AnsApiResponse<>(cueSheetItemDTOList);
    }

}
