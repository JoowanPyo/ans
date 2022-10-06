package com.gemiso.zodiac.app.cueSheet;

import com.gemiso.zodiac.app.cueSheet.dto.*;
import com.gemiso.zodiac.core.enumeration.AuthEnum;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.gemiso.zodiac.core.service.JwtGetUserService;
import com.gemiso.zodiac.core.service.UserAuthChkService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
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

    private final UserAuthChkService userAuthChkService;
    private final JwtGetUserService jwtGetUserService;

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
                                                      @Parameter(name = "deptCd", description = "부서 코드")
                                                      @RequestParam(value = "deptCd", required = false) Long deptCd,
                                                      @Parameter(name = "searchWord", description = "검색키워드")
                                                      @RequestParam(value = "searchWord", required = false) String searchWord) throws Exception {

        CueSheetFindAllDTO cueSheetFindAllDTO = new CueSheetFindAllDTO();


        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            //검색날짜 시간설정 (검색시작 Date = yyyy-MM-dd 00:00:00 / 검색종료 Date yyyy-MM-dd 24:00:00)
            SearchDate searchDate = new SearchDate(sdate, edate);
            cueSheetFindAllDTO = cueSheetService.findAll(searchDate.getStartDate(), searchDate.getEndDate(),
                    brdcPgmId, brdcPgmNm, deptCd, searchWord);

        } else {
            cueSheetFindAllDTO = cueSheetService.findAll(null, null, brdcPgmId, brdcPgmNm, deptCd, searchWord);
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
    public AnsApiResponse<CueSheetSimpleDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                                    @RequestBody @Valid CueSheetCreateDTO cueSheetCreateDTO,
                                                    @Parameter(name = "cueTmpltId", description = "큐시트템플릿아이디", in = ParameterIn.QUERY)
                                                    @RequestParam(value = "cueTmpltId", required = false) Long cueTmpltId,
                                                    @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = jwtGetUserService.getUser(Authorization);
        log.info("CueSheet Create : UserId - " + userId + "<br>" +
                "CueSheet model - " + cueSheetCreateDTO.toString());

        //큐시트 생성 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        CueSheetSimpleDTO cueSheetSimpleDTO = new CueSheetSimpleDTO();

        Integer cueCnt = cueSheetService.getCueSheetCount(cueSheetCreateDTO);

        //이미 같은날짜에 같은프로그램으로 큐시트 생성되어 있을시 error ( 409 )
        if (cueCnt != 0) {
            //return new AnsApiResponse<>(cueSheetSimpleDTO, HttpStatus.CONFLICT);
            throw new Exception("이미 같은날짜에 생성된 큐시트가 존재합니다.");
        }

        Long cueId = cueSheetService.create(cueSheetCreateDTO, userId);

        //수정! 큐시트아이템복사.???

        //큐시트 등록 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        cueSheetSimpleDTO.setCueId(cueId);

        return new AnsApiResponse<>(cueSheetSimpleDTO);
    }

    @Operation(summary = "큐시트 수정", description = "큐시트 수정")
    @PutMapping(path = "/{cueId}")
    public AnsApiResponse<CueSheetSimpleDTO> update(@Parameter(name = "cueSheetUpdateDTO", required = true, description = "필수값<br>")
                                                    @Valid @RequestBody CueSheetUpdateDTO cueSheetUpdateDTO,
                                                    @Parameter(name = "cueId", required = true, description = "큐시트 아이디")
                                                    @PathVariable("cueId") Long cueId,
                                                    @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {
        //버전체크
        //토픽메세지
        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = jwtGetUserService.getUser(Authorization);
        log.info("CueSheet Update : UserId - " + userId + "<br>" +
                "CueSheet model - " + cueSheetUpdateDTO.toString());


        cueSheetService.update(cueSheetUpdateDTO, cueId, userId);

        //큐시트 수정 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        CueSheetSimpleDTO cueSheetSimpleDTO = new CueSheetSimpleDTO();
        cueSheetSimpleDTO.setCueId(cueId);

        return new AnsApiResponse<>(cueSheetSimpleDTO);

    }

    @Operation(summary = "큐시트 삭제", description = "큐시트 삭제")
    @DeleteMapping(path = "/{cueId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "cueId", required = true, description = "큐시트 아이디")
                                    @PathVariable("cueId") Long cueId,
                                    @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        //사용자 비밀번호 체크
        //토픽메세지
        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = jwtGetUserService.getUser(Authorization);
        log.info("CueSheet Update : UserId - " + userId + "<br>" +
                "CueSheet Id - " + cueId);

        cueSheetService.delete(cueId, userId);

        return AnsApiResponse.noContent();
    }

    @Operation(summary = "큐시트 오더락", description = "큐시트 오더락")
    @PutMapping(path = "/{cueId}/orderLock")
    public AnsApiResponse<CueSheetOrderLockResponsDTO> cueSheetOrderLock(@Parameter(name = "cueSheetOrderLockDTO", required = true, description = "필수값<br>")
                                                                         @Valid @RequestBody CueSheetOrderLockDTO cueSheetOrderLockDTO,
                                                                         @Parameter(name = "cueId", required = true, description = "큐시트 아이디")
                                                                         @PathVariable("cueId") Long cueId,
                                                                         @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {


        String getUserId = jwtGetUserService.getUser(Authorization);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        //String userId = userAuthService.authUser.getUserId();
        log.info("CueSheet OrderLock : UserId - " + getUserId + "CueSheet Id - " + cueId +
                "<br>" + "CueSheet Model - " + cueSheetOrderLockDTO.toString());

        //권한이 없는 사용자 error.forbidden
        if (userAuthChkService.authChkPutUser(AuthEnum.CueSheetEdit.getAuth(), getUserId)) {
            log.info("CueSheet Auth Fail : userId - " + getUserId + " token : " + Authorization);
            throw new ResourceNotFoundException("큐시트 수정 권한이 없습니다. 사용자 아이디 : " + getUserId);
        }


        CueSheetOrderLockResponsDTO cueSheetDTO = cueSheetService.cueSheetOrderLock(cueSheetOrderLockDTO, cueId, getUserId);

        //큐시트 오더락 수정 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        /*CueSheetDTO cueSheetDTO = new CueSheetDTO();
        cueSheetDTO.setCueId(cueId);*/

        return new AnsApiResponse<>(cueSheetDTO);

    }

    @Operation(summary = "큐시트 잠금해제", description = "큐시트 잠금해제")
    @PutMapping(path = "/{cueId}/unLock")
    public AnsApiResponse<CueSheetDTO> cueSheetUnLock(@Parameter(name = "cueId", required = true, description = "큐시트 아이디")
                                                      @PathVariable("cueId") Long cueId,
                                                      @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = jwtGetUserService.getUser(Authorization);
        log.info("CueSheet Unlock : UserId - " + userId + "CueSheet Id - " + cueId);


        cueSheetService.cueSheetUnLock(cueId, userId);

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
                                                          @RequestParam(value = "brdcDt", required = false) String brdcDt,
                                                          @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = jwtGetUserService.getUser(Authorization);
        log.info("CueSheet Copy : UserId - " + userId + "CueSheet Id - " + cueId + " Program Id - " + brdcPgmId + " Brdc Date - " + brdcDt);

        //오더버전?
        //토픽메세지

        CueSheetSimpleDTO cueSheetSimpleDTO = new CueSheetSimpleDTO();

        int cueCnt = cueSheetService.getCueSheetCount2(brdcDt, brdcPgmId);

        //이미 같은날짜에 같은프로그램으로 큐시트 생성되어 있을시 error ( 409 )
        if (cueCnt != 0) {
            return new AnsApiResponse<>(cueSheetSimpleDTO, HttpStatus.CONFLICT);
        }

        Long cueSheetId = cueSheetService.copy(cueId, brdcPgmId, brdcDt, userId);

        cueSheetSimpleDTO.setCueId(cueSheetId);

        return new AnsApiResponse<>(cueSheetSimpleDTO);
    }

    @Operation(summary = "큐시트 복사[ 아이템 ]", description = "큐시트 복사[ 아이템 ]")
    @PostMapping(path = "/{cueId}/cuesheetitemcopy")
    public AnsApiResponse<CueSheetSimpleDTO> cueSheetCopy(@Parameter(name = "cueId", required = true, description = "큐시트 아이디")
                                                          @PathVariable("cueId") Long cueId,
                                                          @Parameter(name = "newCueId", description = "새로운 큐시트 아이디")
                                                          @RequestParam(value = "newCueId", required = false) Long newCueId,
                                                          @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = jwtGetUserService.getUser(Authorization);
        log.info("CueSheet Item List Copy : UserId - " + userId + "CueSheet Id - " + cueId);

        //오더버전?
        //토픽메세지

        CueSheetSimpleDTO cueSheetSimpleDTO = new CueSheetSimpleDTO();

        Long cueSheetId = cueSheetService.cueItemcopy(cueId, newCueId, userId);

        cueSheetSimpleDTO.setCueId(cueSheetId);

        return new AnsApiResponse<>(cueSheetSimpleDTO);
    }

    /***********자막 템플릿 설계변경*************/
   /* @Operation(summary = "큐시트 자막 다운로드", description = "큐시트 자막 다운로드")
    @GetMapping(path = "/{cueId}/download")
    public String capDownload(@Parameter(name = "cueId", description = "큐시트 아이디")
                              @PathVariable("cueId") Long cueId,
                              @Parameter(name = "brdcPgmId", description = "프로그램 아이디")
                              @RequestParam(value = "brdcPgmId", required = false) String brdcPgmId) {

        String capDownload = cueSheetService.capDownload(cueId, brdcPgmId);


        return capDownload;
    }*/
    @Operation(summary = "큐시트 자막 다운로드", description = "큐시트 자막 다운로드")
    @GetMapping(path = "/{cueId}/downloads")
    public String capDownloads(@Parameter(name = "cueId", description = "큐시트 아이디")
                               @PathVariable("cueId") Long cueId) {

        String capDownload = cueSheetService.capDownloads(cueId);

        return capDownload;
    }

    @Operation(summary = "큐시트 자막 다운로드 단건", description = "큐시트 자막 다운로드 단건")
    @GetMapping(path = "/{cueId}/download")
    public String capDownload(@Parameter(name = "cueId", description = "큐시트 아이디")
                              @PathVariable("cueId") Long cueId,
                              @Parameter(name = "cueItemId", description = "큐시트 아이템 아이디")
                              @RequestParam("cueItemId") Long cueItemId) {

        String capDownload = cueSheetService.capDownload(cueItemId, cueId);

        return capDownload;
    }


}
