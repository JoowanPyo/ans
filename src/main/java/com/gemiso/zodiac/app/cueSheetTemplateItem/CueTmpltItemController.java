package com.gemiso.zodiac.app.cueSheetTemplateItem;

import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmpltItemCreateDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmpltItemDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmpltItemSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmpltItemUpdateDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.gemiso.zodiac.core.service.UserAuthService;
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
@RequestMapping("/cuetmpltitem")
@Slf4j
@RequiredArgsConstructor
public class CueTmpltItemController {

    private final CueTmpltItemService cueTmpltItemService;

    private final UserAuthService userAuthService;


    @Operation(summary = "큐시트 템플릿 아이템 목록조회", description = "큐시트 템플릿 아이템 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<CueTmpltItemDTO>> findAll(@Parameter(name = "cueTmpltId", description = "큐시트 템플릿 아이디")
                                                         @RequestParam(value = "cueTmpltId", required = false) Long cueTmpltId,
                                                         @Parameter(name = "searchWord", description = "검색키워드[큐시트 템플릿 아이템 제목]")
                                                         @RequestParam(value = "searchWord", required = false) String searchWord) {

        List<CueTmpltItemDTO> cueTmpltItemDTOList = cueTmpltItemService.findAll(cueTmpltId, searchWord);

        return new AnsApiResponse<>(cueTmpltItemDTOList);
    }

    @Operation(summary = "큐시트 템플릿 아이템 상세조회", description = "큐시트 템플릿 아이템 상세조회")
    @GetMapping(path = "/{cueTmpltItemId}")
    public AnsApiResponse<CueTmpltItemDTO> find(@Parameter(name = "cueTmpltItemId", description = "큐시트 템플릿 아이템 아이디")
                                                @PathVariable("cueTmpltItemId") Long cueTmpltItemId) {

        CueTmpltItemDTO cueTmpltItemDTO = cueTmpltItemService.find(cueTmpltItemId);

        return new AnsApiResponse<>(cueTmpltItemDTO);
    }

    @Operation(summary = "큐시트 템플릿 아이템 등록", description = "큐시트 템플릿 아이템 등록")
    @PostMapping(path = "/{cueTmpltId}/item")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<CueTmpltItemSimpleDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                                        @RequestBody @Valid CueTmpltItemCreateDTO cueTmpltItemCreateDTO,
                                                        @Parameter(name = "cueTmpltId", description = "큐시트 템플릿 아이디")
                                                        @PathVariable("cueTmpltId") Long cueTmpltId) {

        String userId = userAuthService.authUser.getUserId(); //토큰에서 사장자 아이디 get
        log.info("CueSheet Template Item Create : userId - "+userId+" cueTmpltId - "+cueTmpltId+"<br>"+
                "CueSheet Template Model - "+cueTmpltItemCreateDTO.toString());

        CueTmpltItemSimpleDTO cueTmpltItemSimpleDTO = cueTmpltItemService.create(cueTmpltItemCreateDTO, cueTmpltId, userId);

        return new AnsApiResponse<>(cueTmpltItemSimpleDTO);
    }

    @Operation(summary = "큐시트 템플릿 아이템 수정", description = "큐시트 템플릿 아이템 수정")
    @PutMapping(path = "/{cueTmpltItemId}")
    public AnsApiResponse<CueTmpltItemSimpleDTO> update(@Parameter(description = "필수값<br> ", required = true)
                                                        @RequestBody @Valid CueTmpltItemUpdateDTO cueTmpltItemUpdateDTO,
                                                        @Parameter(name = "cueTmpltItemId", description = "큐시트 템플릿 아이템 아이디")
                                                        @PathVariable("cueTmpltItemId") Long cueTmpltItemId) {

        String userId = userAuthService.authUser.getUserId(); //토큰에서 사장자 아이디 get
        log.info("CueSheet Template Item Update : userId - "+userId+" cueTmpltItemId - "+cueTmpltItemId+"<br>"+
                "CueSheet Template Model - "+cueTmpltItemUpdateDTO.toString());

        CueTmpltItemSimpleDTO cueTmpltItemSimpleDTO = cueTmpltItemService.update(cueTmpltItemUpdateDTO, cueTmpltItemId, userId);

        return new AnsApiResponse<>(cueTmpltItemSimpleDTO);
    }

    @Operation(summary = "큐시트 템플릿 아이템 삭제", description = "큐시트 템플릿 아이템 삭제")
    @DeleteMapping(path = "/{cueTmpltItemId}")
    public AnsApiResponse<?> delete(@Parameter(name = "cueTmpltItemId", description = "큐시트 템플릿 아이템 아이디")
                                    @PathVariable("cueTmpltItemId") Long cueTmpltItemId) {

        String userId = userAuthService.authUser.getUserId(); //토큰에서 사장자 아이디 get
        log.info("CueSheet Template Item Delete : userId - "+userId+" cueTmpltItemId - "+cueTmpltItemId);

        cueTmpltItemService.delete(cueTmpltItemId, userId);

        return AnsApiResponse.noContent();
    }

    @Operation(summary = "큐시트 템플릿 아이템 순서변경", description = "큐시트 템플릿 아이템 순서변경")
    @PutMapping(path = "/{cueTmpltId}/item/{cueTmpltItemId}/ord")
    public AnsApiResponse<List<CueTmpltItemDTO>> ordUpdate(@Parameter(name = "cueTmpltItemId", description = "큐시트 템플릿 아이템 아이디")
                                                           @PathVariable("cueTmpltItemId") Long cueTmpltItemId,
                                                           @Parameter(name = "cueTmpltId", description = "큐시트 템플릿 아이디")
                                                           @PathVariable("cueTmpltId") Long cueTmpltId,
                                                           @Parameter(name = "cueItemOrd", description = "큐시트 아이템 순번")
                                                           @RequestParam(value = "cueItemOrd", required = false) int cueItemOrd) {

        String userId = userAuthService.authUser.getUserId(); //토큰에서 사장자 아이디 get
        log.info("CueSheet Template Item Order Update : userId - "+userId+" cueTmpltItemId - "+cueTmpltItemId+ " cueTmpltId - "
                +cueTmpltId+" Order - "+cueItemOrd);

        cueTmpltItemService.ordUpdate(cueTmpltItemId, cueTmpltId, cueItemOrd);

        List<CueTmpltItemDTO> cueTmpltItemDTOList = cueTmpltItemService.findAll(cueTmpltId, null);

        return new AnsApiResponse<>(cueTmpltItemDTOList);
    }
}
