package com.gemiso.zodiac.app.cueSheet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gemiso.zodiac.app.cueSheet.dto.*;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@Api(description = "큐시트 API")
@RestController
@RequestMapping("/cuesheet")
@Slf4j
@RequiredArgsConstructor
public class CueSheetController {

    private final CueSheetService cueSheetService;


    @Operation(summary = "큐시트 목록조회", description = "큐시트 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<CueSheetFindAllDTO> findAll(@Parameter(description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                      @Parameter(description = "검색 종료 날짜(yyyy-MM-dd)", required = false)
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                      @Parameter(name = "brdcPgmId", description = "프로그램 아이디")
                                                      @RequestParam(value = "brdcPgmId", required = false) String brdcPgmId,
                                                      @Parameter(name = "brdcPgmNm", description = "프로그램 명")
                                                      @RequestParam(value = "brdcPgmNm", required = false) String brdcPgmNm,
                                                      @Parameter(name = "searchWord", description = "검색키워드")
                                                      @RequestParam(value = "searchWord", required = false) String searchWord) throws Exception {

        CueSheetFindAllDTO cueSheetFindAllDTO = new CueSheetFindAllDTO();


        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            //검색날짜 시간설정 (검색시작 Date = yyyy-MM-dd 00:00:00 / 검색종료 Date yyyy-MM-dd 24:00:00)
            SearchDate searchDate = new SearchDate(sdate, edate);
            cueSheetFindAllDTO = cueSheetService.findAll(searchDate.getStartDate(), searchDate.getEndDate(), brdcPgmId, brdcPgmNm, searchWord);

        } else {
            cueSheetFindAllDTO = cueSheetService.findAll(null, null, brdcPgmId, brdcPgmNm, searchWord);
        }

        return new AnsApiResponse<>(cueSheetFindAllDTO);

    }

    @Operation(summary = "큐시트 상세조회", description = "큐시트 상세조회")
    @GetMapping(path = "/{cueId}")
    public AnsApiResponse<CueSheetDTO> find(@Parameter(name = "cueId", description = "큐시트 아이디")
                                            @PathVariable("cueId") Long cueId) {


        CueSheetDTO cueSheetDTO = cueSheetService.find(cueId);

        return new AnsApiResponse<>(cueSheetDTO);
    }


    @Operation(summary = "큐시트 등록", description = "큐시트 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<CueSheetDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                              @RequestBody @Valid CueSheetCreateDTO cueSheetCreateDTO,
                                              @Parameter(name = "cueTmpltId", description = "큐시트템플릿아이디", in = ParameterIn.QUERY)
                                              @RequestParam(value = "cueTmpltId", required = false) Long cueTmpltId) throws JsonProcessingException {

        CueSheetDTO cueSheetDTO = new CueSheetDTO(); //리턴시켜줄 큐시트 모델 생성

        int cueCnt = cueSheetService.getCueSheetCount(cueSheetCreateDTO);

        //이미 같은날짜에 같은프로그램으로 큐시트 생성되어 있을시 error ( 409 )
        if (cueCnt != 0){
            return new AnsApiResponse<>(cueSheetDTO, HttpStatus.CONFLICT);
        }

        Long cueId = cueSheetService.create(cueSheetCreateDTO);

        //수정! 큐시트아이템복사.???

        //큐시트 등록 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        cueSheetDTO.setCueId(cueId);

        return new AnsApiResponse<>(cueSheetDTO);
    }

    @Operation(summary = "큐시트 수정", description = "큐시트 수정")
    @PutMapping(path = "/{cueId}")
    public AnsApiResponse<CueSheetDTO> update(@Parameter(name = "cueSheetUpdateDTO", required = true, description = "필수값<br>")
                                              @Valid @RequestBody CueSheetUpdateDTO cueSheetUpdateDTO,
                                              @Parameter(name = "cueId", required = true, description = "큐시트 아이디")
                                              @PathVariable("cueId") Long cueId) throws JsonProcessingException {
        //버전체크
        //토픽메세지


        cueSheetService.update(cueSheetUpdateDTO, cueId);

        //큐시트 수정 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        CueSheetDTO cueSheetDTO = new CueSheetDTO();
        cueSheetDTO.setCueId(cueId);

        return new AnsApiResponse<>(cueSheetDTO);

    }

    @Operation(summary = "큐시트 삭제", description = "큐시트 삭제")
    @DeleteMapping(path = "/{cueId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "cueId", required = true, description = "큐시트 아이디")
                                    @PathVariable("cueId") Long cueId) {

        //사용자 비밀번호 체크
        //토픽메세지

        cueSheetService.delete(cueId);

        return AnsApiResponse.noContent();
    }

    @Operation(summary = "큐시트 오더락", description = "큐시트 오더락")
    @PutMapping(path = "/{cueId}/orderLock")
    public AnsApiResponse<CueSheetDTO> cueSheetOrderLock(@Parameter(name = "cueSheetUpdateDTO", required = true, description = "필수값<br>")
                                                         @Valid @RequestBody CueSheetOrderLockDTO cueSheetOrderLockDTO,
                                                         @Parameter(name = "cueId", required = true, description = "큐시트 아이디")
                                                         @PathVariable("cueId") Long cueId) {

        cueSheetService.cueSheetOrderLock(cueSheetOrderLockDTO, cueId);

        //큐시트 오더락 수정 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        CueSheetDTO cueSheetDTO = new CueSheetDTO();
        cueSheetDTO.setCueId(cueId);

        return new AnsApiResponse<>(cueSheetDTO);

    }

    @Operation(summary = "큐시트 잠금해제", description = "큐시트 잠금해제")
    @PutMapping(path = "/{cueId}/unLock")
    public AnsApiResponse<CueSheetDTO> cueSheetUnLock(@Parameter(name = "cueId", required = true, description = "큐시트 아이디")
                                                      @PathVariable("cueId") Long cueId) {

        cueSheetService.cueSheetUnLock(cueId);

        //큐시트 잠금해제 수정 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        CueSheetDTO cueSheetDTO = new CueSheetDTO();
        cueSheetDTO.setCueId(cueId);

        return new AnsApiResponse<>(cueSheetDTO);
    }

    @Operation(summary = "큐시트 복사", description = "큐시트 복사")
    @PostMapping(path = "/{cueId}/cuesheetcopy")
    public AnsApiResponse<CueSheetSimpleDTO> cueSheetCopy(@Parameter(name = "cueId", required = true, description = "큐시트 아이디")
                                                          @PathVariable("cueId") Long cueId,
                                                          @Parameter(name = "brdcPgmId", description = "프로그램 아이디")
                                                          @RequestParam(value = "brdcPgmId", required = false) String brdcPgmId,
                                                          @Parameter(name = "brdcDt", description = "방송일자")
                                                          @RequestParam(value = "brdcDt", required = false) String brdcDt) {

        //오더버전?
        //토픽메세지

        CueSheetSimpleDTO cueSheetSimpleDTO = new CueSheetSimpleDTO();

        int cueCnt = cueSheetService.getCueSheetCount2(brdcDt, brdcPgmId);

        //이미 같은날짜에 같은프로그램으로 큐시트 생성되어 있을시 error ( 409 )
        if (cueCnt != 0){
            return new AnsApiResponse<>(cueSheetSimpleDTO, HttpStatus.CONFLICT);
        }

        Long cueSheetId = cueSheetService.copy(cueId, brdcPgmId, brdcDt);

        cueSheetSimpleDTO.setCueId(cueSheetId);

        return new AnsApiResponse<>(cueSheetSimpleDTO);
    }


}
