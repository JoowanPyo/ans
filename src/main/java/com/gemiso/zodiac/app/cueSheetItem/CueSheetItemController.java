package com.gemiso.zodiac.app.cueSheetItem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetSimpleDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.*;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.gemiso.zodiac.core.service.UserAuthService;
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

    private final UserAuthService userAuthService;

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
    public AnsApiResponse<CueSheetItemResponseDTO> createTemplate(@Parameter(description = "필수값<br> ", required = true)
                                                                @RequestBody @Valid List<CueSheetItemCreateDTO> cueSheetItemCreateDTOList,
                                                                @Parameter(name = "cueId", description = "큐시트아이디")
                                                                @PathVariable("cueId") Long cueId) throws JsonProcessingException {

        //토큰 사용자 Id(현재 로그인된 사용자 ID)
        String userId = userAuthService.authUser.getUserId();
        log.info("CueSheet Item Template Create : userId - "+userId+ " CueId - "+cueId +"<br>"+
                " CueSheet Item Model List - " +cueSheetItemCreateDTOList.toString());


        cueSheetItemService.createTemplate(cueSheetItemCreateDTOList, cueId, userId);

        CueSheetItemResponseDTO cueSheetItemDTOList = new CueSheetItemResponseDTO();
        cueSheetItemDTOList.setCueId(cueId);
        //List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemService.findAll(null, cueId, null, null);

        return new AnsApiResponse<>(cueSheetItemDTOList);

    }

    @Operation(summary = "큐시트 아이템 등록", description = "큐시트 아이템 등록")
    @PostMapping(path = "/{cueId}/item")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<CueSheetItemResponseDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                                  @RequestBody @Valid CueSheetItemCreateDTO cueSheetItemCreateDTO,
                                                  @Parameter(name = "cueId", description = "큐시트아이디")
                                                  @PathVariable("cueId") Long cueId) throws JsonProcessingException {

        //토큰 사용자 Id(현재 로그인된 사용자 ID)
        String userId = userAuthService.authUser.getUserId();
        log.info("CueSheet Item Create : userId - "+userId+ " CueId - "+cueId +"<br>"+
                " CueSheet Item Model - " +cueSheetItemCreateDTO.toString());

        Long cueItemId = cueSheetItemService.create(cueSheetItemCreateDTO, cueId, userId);

        CueSheetItemResponseDTO cueSheetItemDTOList = new CueSheetItemResponseDTO();
        cueSheetItemDTOList.setCueId(cueId);

        //CueSheetItemDTO cueSheetItemDTO = cueSheetItemService.find(cueItemId);

        return new AnsApiResponse<>(cueSheetItemDTOList);
    }

    @Operation(summary = "큐시트 아이템 수정", description = "큐시트 아이템 수정")
    @PutMapping(path = "/{cueId}/item/{cueItemId}")
    public AnsApiResponse<CueSheetItemResponseDTO> update(@Parameter(description = "필수값<br> ", required = true)
                                                  @RequestBody @Valid CueSheetItemUpdateDTO cueSheetItemUpdateDTO,
                                                  @Parameter(name = "cueItemDivCd", description = "큐시트 아이템 구분 코드", required = true)
                                                  @RequestParam(value = "cueItemDivCd") String cueItemDivCd,
                                                  @Parameter(name = "cueId", description = "큐시트아이디")
                                                  @PathVariable("cueId") Long cueId,
                                                  @Parameter(name = "cueItemId", description = "큐시트아이템 아이디")
                                                  @PathVariable("cueItemId") Long cueItemId) throws Exception {

        //토큰 사용자 Id(현재 로그인된 사용자 ID)
        String userId = userAuthService.authUser.getUserId();
        log.info("CueSheet Item Create : userId - "+userId+ " CueId - "+cueId +" CueItemDivCd - "+cueItemDivCd
                        +"CueItemId - "+cueItemId+"<br>"+
                " CueSheet Item Model - " +cueSheetItemUpdateDTO.toString());


        if (cueItemDivCd == null || "".equals(cueItemDivCd)) {

            throw new ResourceNotFoundException("큐시트 아이템 구분 코드가 잘못 되었습니다. 구분코드 : " + cueItemDivCd);

            //큐시트 아이템 코드가 cue_item, cue_template일 경우 [큐시트 아이템 수정]
        } else if ("cue_item".equals(cueItemDivCd) || "cue_template".equals(cueItemDivCd)) {

            //큐시트 템플릿 Update
            cueSheetItemService.update(cueSheetItemUpdateDTO, cueId, cueItemId, userId);

            //큐시트 아이템 코드가 cue_article일 경우 [큐시트 아이템 수정 & 기사 수정]
        } else if ("cue_article".equals(cueItemDivCd)) {

            //기사복사본 수정.
            cueSheetItemService.updateCueItemArticle(cueSheetItemUpdateDTO, cueId, cueItemId, userId);

        } else {
            throw new ResourceNotFoundException("큐시트 아이템 구분코드를 확인해 주세요. 큐시트 아이템 구분 코드 : " + cueItemDivCd);
        }

        CueSheetItemResponseDTO cueSheetItemDTOList = new CueSheetItemResponseDTO();
        cueSheetItemDTOList.setCueId(cueId);


        return new AnsApiResponse<>(cueSheetItemDTOList);

    }

    @Operation(summary = "큐시트 아이템 삭제", description = "큐시트 아이템 삭제")
    @DeleteMapping(path = "/{cueId}/item/{cueItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "cueId", description = "큐시트아이디")
                                    @PathVariable("cueId") Long cueId,
                                    @Parameter(name = "cueItemId", description = "큐시트아이템 아이디")
                                    @PathVariable("cueItemId") Long cueItemId) throws Exception {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        log.info("CueSheet Item Delete : userId - "+userId+ " CueId - "+cueId
                +"CueItemId - "+cueItemId);

        cueSheetItemService.delete(cueId, cueItemId, userId);

        return AnsApiResponse.noContent();
    }

    @Operation(summary = "큐시트 아이템 순서변경", description = "큐시트 아이템 순서변경")
    @PutMapping(path = "/{cueId}/item/{cueItemId}/ord")
    public AnsApiResponse<CueSheetItemResponseDTO> ordUpdate(@Parameter(name = "cueId", description = "큐시트아이디")
                                                           @PathVariable("cueId") Long cueId,
                                                           @Parameter(name = "cueItemId", description = "큐시트아이템 아이디")
                                                           @PathVariable("cueItemId") Long cueItemId,
                                                           @Parameter(name = "cueItemOrd", description = "큐시트 아이템 순번")
                                                           @RequestParam(value = "cueItemOrd", required = false) Integer cueItemOrd,
                                                           @Parameter(name = "spareYn", description = "예비큐시트 여부(N, Y)")
                                                           @RequestParam(value = "spareYn", required = true) String spareYn
    ) {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        log.info("CueSheet Item OrderUpdate : userId - "+userId+ " CueId - "+cueId
                +"CueItemId - "+cueItemId+ "Order - "+cueItemOrd+ " SpareYn - "+spareYn);

        cueSheetItemService.ordUpdate(cueId, cueItemId, cueItemOrd, spareYn);

        CueSheetItemResponseDTO cueSheetItemDTOList = new CueSheetItemResponseDTO();
        cueSheetItemDTOList.setCueId(cueId);

        //List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemService.findAll(null, cueId, null, null);

        return new AnsApiResponse<>(cueSheetItemDTOList);

    }

    /*@Operation(summary = "큐시트 아이템 순서변경", description = "큐시트 아이템 순서변경")
    @PutMapping(path = "/{cueId}/item/change_order")
    public AnsApiResponse<List<CueSheetItemDTO>> ordCdUpdate(@Parameter(name = "cueId", description = "큐시트아이디")
                                                             @PathVariable("cueId") Long cueId,
                                                             @Parameter(name = "spareYn", description = "예비큐시트 여부(N, Y)")
                                                             @RequestParam(value = "spareYn", required = true) String spareYn,
                                                             @Parameter(description = "필수값<br> ", required = true)
                                                             @RequestBody @Valid List<CueSheetItemOrdUpdateDTO> cueSheetItemOrdUpdateDTOList) throws JsonProcessingException {
        cueSheetItemService.ordCdUpdate(cueId, spareYn, cueSheetItemOrdUpdateDTOList);

        List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemService.findAll(null, cueId, null, null);

        return new AnsApiResponse<>(cueSheetItemDTOList);

    }*/

    @Operation(summary = "큐시트 아이템 생성[Drag and Drop] By Article", description = "큐시트 아이템 생성[Drag and Drop] By Article")
    @PostMapping(path = "/{cueId}/article/{artclId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<CueSheetItemResponseDTO> createCueItemArticle(@Parameter(name = "cueId", description = "큐시트아이디")
                                                               @PathVariable("cueId") Long cueId,
                                                               @Parameter(name = "artclId", description = "기사 아이디")
                                                               @PathVariable("artclId") Long artclId,
                                                               @Parameter(name = "cueItemOrd", description = "큐시트 아이템 순번")
                                                               @RequestParam(value = "cueItemOrd", required = false) Integer cueItemOrd,
                                                               @Parameter(name = "cueItemDivCd", description = "큐시트 아이템 구분 코드")
                                                               @RequestParam(value = "cueItemDivCd", required = false) String cueItemDivCd,
                                                               @Parameter(name = "spareYn", description = "예비큐시트 여부(N, Y)")
                                                               @RequestParam(value = "spareYn", required = false) String spareYn) throws Exception {

        //토큰 사용자 Id(현재 로그인된 사용자 ID)
        String userId = userAuthService.authUser.getUserId();
        log.info("CueSheet Item Create [Drag and Drop] Article : userId - "+userId+ " CueId - "+cueId +"<br>"+
                " ArticleId - " +artclId + " Order - "+cueItemOrd+" SpareYn - "+spareYn);


        cueSheetItemService.createCueItemArticle(cueId, artclId, cueItemOrd, cueItemDivCd, spareYn, userId);

        //List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemService.findAll(null, cueId, null, null);

        CueSheetItemResponseDTO cueSheetItemDTOList = new CueSheetItemResponseDTO();
        cueSheetItemDTOList.setCueId(cueId);

        return new AnsApiResponse<>(cueSheetItemDTOList);

    }

    @Operation(summary = "큐시트 아이템 생성[Drag and Drop] By CueSheetItem", description = "큐시트 아이템 생성[Drag and Drop] By CueSheetItem")
    @PostMapping(path = "/{cueId}/item/{cueItemId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<CueSheetItemResponseDTO> createCueItem(@Parameter(name = "cueId", description = "큐시트아이디")
                                                                 @PathVariable("cueId") Long cueId,
                                                                 @Parameter(name = "cueItemId", description = "큐시트 아이템 아이디")
                                                                 @PathVariable("cueItemId") Long cueItemId,
                                                                 @Parameter(name = "cueItemOrd", description = "큐시트 아이템 순번")
                                                                 @RequestParam(value = "cueItemOrd", required = false) int cueItemOrd,
                                                                 @Parameter(name = "cueItemDivCd", description = "큐시트 아이템 구분 코드")
                                                                 @RequestParam(value = "cueItemDivCd", required = false) String cueItemDivCd,
                                                                 @Parameter(name = "spareYn", description = "예비큐시트 여부(N, Y)")
                                                                 @RequestParam(value = "spareYn", required = false) String spareYn) throws Exception {

        //토큰 사용자 Id(현재 로그인된 사용자 ID)
        String userId = userAuthService.authUser.getUserId();
        log.info("CueSheet Item Create [Drag and Drop] : userId - "+userId+ " CueId - "+cueId +"<br>"+
                " CueSheetItemId - " +cueItemId + " Order - "+cueItemOrd+" SpareYn - "+spareYn);


        cueSheetItemService.createCueItem(cueId, cueItemId, cueItemOrd, cueItemDivCd, spareYn, userId);

        //List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemService.findAll(null, cueId, null, null);

        CueSheetItemResponseDTO cueSheetItemDTOList = new CueSheetItemResponseDTO();
        cueSheetItemDTOList.setCueId(cueId);

        return new AnsApiResponse<>(cueSheetItemDTOList);

    }

    @Operation(summary = "큐시트 아이템 생성[Drag and Drop] By CueTemplateItem", description = "큐시트 아이템 생성[Drag and Drop] By CueTemplateItem")
    @PostMapping(path = "/{cueId}/template/{cueTmpltItemId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<CueSheetItemResponseDTO> createCueTemplate(@Parameter(name = "cueId", description = "큐시트아이디")
                                                                 @PathVariable("cueId") Long cueId,
                                                                 @Parameter(name = "cueTmpltItemId", description = "큐시트 템플릿 아이템 아이디")
                                                                 @PathVariable("cueTmpltItemId") Long cueTmpltItemId,
                                                                 @Parameter(name = "cueItemOrd", description = "큐시트 아이템 순번")
                                                                 @RequestParam(value = "cueItemOrd", required = false) int cueItemOrd,
                                                                 @Parameter(name = "cueItemDivCd", description = "큐시트 아이템 구분 코드")
                                                                 @RequestParam(value = "cueItemDivCd", required = false) String cueItemDivCd,
                                                                 @Parameter(name = "spareYn", description = "예비큐시트 여부(N, Y)")
                                                                 @RequestParam(value = "spareYn", required = false) String spareYn) throws Exception {

        //토큰 사용자 Id(현재 로그인된 사용자 ID)
        String userId = userAuthService.authUser.getUserId();
        log.info("CueSheet Item Create [Drag and Drop] CueSheetTemplate : userId - "+userId+ " CueId - "+cueId +"<br>"+
                " CueTmpltItemId - " +cueTmpltItemId + " Order - "+cueItemOrd+" SpareYn - "+spareYn);

        cueSheetItemService.createCueTemplate(cueId, cueTmpltItemId, cueItemOrd, cueItemDivCd, spareYn, userId);

        CueSheetItemResponseDTO cueSheetItemDTOList = new CueSheetItemResponseDTO();
        cueSheetItemDTOList.setCueId(cueId);

        return new AnsApiResponse<>(cueSheetItemDTOList);
    }

    @Operation(summary = "큐시트 아이템 생성[Drag and Drop] List", description = "큐시트 아이템 생성[Drag and Drop] List")
    @PostMapping(path = "/{cueId}/item/createList")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<CueSheetItemResponseDTO> createCueItemList(@Parameter(description = "필수값<br> ", required = true)
                                                                   @RequestBody @Valid List<CueSheetItemCreateListDTO> cueSheetItemCreateListDTO,
                                                                   @Parameter(name = "cueId", description = "큐시트아이디")
                                                                   @PathVariable("cueId") Long cueId,
                                                                   @Parameter(name = "spareYn", description = "예비큐시트 여부(N, Y)")
                                                                   @RequestParam(value = "spareYn", required = false) String spareYn) throws Exception {

        //토큰 사용자 Id(현재 로그인된 사용자 ID)
        String userId = userAuthService.authUser.getUserId();
        log.info("CueSheet Item Create [Drag and Drop List] : userId - "+userId+ " CueId - "+cueId );

        cueSheetItemService.createCueItemList(cueSheetItemCreateListDTO, cueId, spareYn, userId);

        //List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemService.findAll(null, cueId, null, null);

        CueSheetItemResponseDTO cueSheetItemDTOList = new CueSheetItemResponseDTO();
        cueSheetItemDTOList.setCueId(cueId);

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
                                                                    @RequestParam(value = "spareYn", required = false) String spareYn) throws JsonProcessingException {

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

        //토큰 사용자 Id(현재 로그인된 사용자 ID)
        String userId = userAuthService.authUser.getUserId();
        log.info("CueSheet Item Restore : userId - "+userId+ " CueId - "+cueId +" Order - "+cueItemOrd);

        CueSheetItemSimpleDTO cueSheetItemSimpleDTO = cueSheetItemService.cueSheetItemRestore(cueId, cueItemId, cueItemOrd);

        return new AnsApiResponse<>(cueSheetItemSimpleDTO);
    }


}
