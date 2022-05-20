package com.gemiso.zodiac.app.article;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gemiso.zodiac.app.article.dto.*;
import com.gemiso.zodiac.core.enumeration.AuthEnum;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.page.PageResultDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.gemiso.zodiac.core.service.UserAuthChkService;
import com.gemiso.zodiac.core.service.UserAuthService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@Api(description = "기사 API")
@RestController
@RequestMapping("/article")
@Slf4j
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    private final UserAuthService userAuthService;
    private final UserAuthChkService userAuthChkService;

    @Operation(summary = "기사 목록조회", description = "기사 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<?> findAll(
            @Parameter(name = "sdate", description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
            @Parameter(name = "edate", description = "검색 종료 날짜(yyyy-MM-dd)", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
            @Parameter(name = "rcvDt", description = "수신일자(yyyyMMdd)", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date rcvDt,
            @Parameter(name = "rptrId", description = "기자 아이디") @RequestParam(value = "rptrId", required = false) String rptrId,
            @Parameter(name = "inputrId", description = "등록자 아이디") @RequestParam(value = "inputrId", required = false) String inputrId,
            @Parameter(name = "brdcPgmId", description = "방송프로그램 아이디") @RequestParam(value = "brdcPgmId", required = false) String brdcPgmId,
            @Parameter(name = "artclDivCd", description = "기사구분코드(01:일반, 02:전체, 03:이슈)") @RequestParam(value = "artclDivCd", required = false) String artclDivCd,
            @Parameter(name = "artclTypCd", description = "기사유형코드(01:스트레이트, 02:리포트, 03:C/T, 04:하단롤, 05:긴급자막)") @RequestParam(value = "artclTypCd", required = false) String artclTypCd,
            @Parameter(name = "searchDivCd", description = "검색구분코드<br>01 - 기사제목<br>02 - 기자명") @RequestParam(value = "searchDivCd", required = false) String searchDivCd,
            @Parameter(name = "searchWord", description = "검색키워드") @RequestParam(value = "searchWord", required = false) String searchWord,
            @Parameter(name = "page", description = "시작페이지") @RequestParam(value = "page", required = false) Integer page,
            @Parameter(name = "limit", description = "한 페이지에 데이터 수") @RequestParam(value = "limit", required = false) Integer limit,
            @Parameter(name = "apprvDivCdList", description = "픽스구분코드(fix_none,article_fix,editor_fix,anchor_fix,desk_fix)") @RequestParam(value = "apprvDivCdList", required = false) List<String> apprvDivCdList,
            @Parameter(name = "deptCd", description = "부서코드") @RequestParam(value = "deptCd", required = false) String deptCd,
            @Parameter(name = "artclCateCd", description = "기사 카테고리 코드") @RequestParam(value = "artclCateCd", required = false) String artclCateCd,
            @Parameter(name = "artclTypDtlCd", description = "기사 유형 상세 코드") @RequestParam(value = "artclTypDtlCd", required = false) String artclTypDtlCd,
            @Parameter(name = "delYn", description = "삭제 여부") @RequestParam(value = "delYn", required = false) String delYn,
            @Parameter(name = "artclId", description = "기사아이디") @RequestParam(value = "artclId", required = false) Long artclId,
            @Parameter(name = "copyYn", description = "기사 복사여부[오리지날 기사 : N, 복사기사 : Y]") @RequestParam(value = "copyYn", required = false) String copyYn) throws Exception {

        PageResultDTO<ArticleDTO, Article> pageList = null;
        //List<ArticleDTO> articleDTOList = new ArrayList<>();

        //기사읽기 권한이 없는 사용자 error.forbidden
        //List<String> userAuth = userAuthService.authChk(AuthEnum.ArticleRead.getAuth(), AuthEnum.AdminRead.getAuth());
        // if (userAuthService.authChks(AuthEnum.ArticleRead.getAuth(), AuthEnum.AdminMode.getAuth())) { //기사읽기 권한이거나, 관리자 읽기 권한일 경우 가능.
        //     throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        // }

        //검색조건 날짜형식이 들어왔을경우
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {

            SearchDate searchDate = new SearchDate(sdate, edate);

            pageList = articleService.findAll(searchDate.getStartDate(), searchDate.getEndDate(), rcvDt, rptrId, inputrId,
                    brdcPgmId, artclDivCd, artclTypCd, searchDivCd, searchWord, page, limit, apprvDivCdList, deptCd,
                    artclCateCd, artclTypDtlCd, delYn, artclId, copyYn);
            //검색조건 날짜형식이 안들어왔을경우
        } else {

            pageList = articleService.findAll(null, null, rcvDt, rptrId, inputrId, brdcPgmId, artclDivCd,
                    artclTypCd, searchDivCd, searchWord, page, limit, apprvDivCdList, deptCd, artclCateCd,
                    artclTypDtlCd, delYn, artclId, copyYn);

        }

        return new AnsApiResponse<>(pageList);
    }

    @Operation(summary = "기사 목록조회[큐시트]", description = "기사 목록조회[큐시트]")
    @GetMapping(path = "/cuesheet")
    public AnsApiResponse<PageResultDTO<ArticleDTO, Article>> findCue(@Parameter(name = "sdate", description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = true)
                                                                      @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                                      @Parameter(name = "edate", description = "검색 종료 날짜(yyyy-MM-dd)", required = true)
                                                                      @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                                      @Parameter(name = "searchWord", description = "검색키워드")
                                                                      @RequestParam(value = "searchWord", required = false) String searchWord,
                                                                      @Parameter(name = "cueId", description = "검색키워드", required = false)
                                                                      @RequestParam(value = "cueId") Long cueId,
                                                                      @Parameter(name = "brdcPgmId", description = "방송프로그램 아이디")
                                                                      @RequestParam(value = "brdcPgmId", required = false) String brdcPgmId,
                                                                      @Parameter(name = "artclTypDtlCd", description = "기사 유형 상세 코드")
                                                                      @RequestParam(value = "artclTypDtlCd", required = false) String artclTypDtlCd,
                                                                      @Parameter(name = "copyYn", description = "기사 복사여부[오리지날 기사 : N, 복사기사 : Y]")
                                                                      @RequestParam(value = "copyYn", required = false) String copyYn,
                                                                      @Parameter(name = "page", description = "시작페이지")
                                                                      @RequestParam(value = "page", required = false) Integer page,
                                                                      @Parameter(name = "limit", description = "한 페이지에 데이터 수")
                                                                      @RequestParam(value = "limit", required = false) Integer limit) throws Exception {


        SearchDate searchDate = new SearchDate(sdate, edate);

        PageResultDTO<ArticleDTO, Article> pageList = articleService.findCue(searchDate.getStartDate(), searchDate.getEndDate(),
                searchWord, cueId, brdcPgmId, artclTypDtlCd, copyYn, page, limit);

        pageList = articleService.confirmArticleList(pageList, cueId);

        return new AnsApiResponse<>(pageList);

    }

    @Operation(summary = "기사 목록조회[이슈]", description = "기사 목록조회")
    @GetMapping(path = "/issue")
    public AnsApiResponse<?> findAllIssue(
            @Parameter(name = "date", description = "이슈 검색 시작 데이터 날짜(yyyy-MM-dd)", required = true)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @Parameter(name = "issuKwd", description = "이슈 키워드") @RequestParam(value = "issuKwd", required = false) String issuKwd,
            @Parameter(name = "artclDivCd", description = "기사 구분 코드") @RequestParam(value = "artclDivCd", required = false) String artclDivCd,
            @Parameter(name = "artclTypCd", description = "기사 유형 코드") @RequestParam(value = "artclTypCd", required = false) String artclTypCd,
            @Parameter(name = "artclTypDtlCd", description = "기상 유형 상세 코드") @RequestParam(value = "artclTypDtlCd", required = false) String artclTypDtlCd,
            @Parameter(name = "artclCateCd", description = "기사 카테고리 코드") @RequestParam(value = "artclCateCd", required = false) String artclCateCd,
            @Parameter(name = "deptCd", description = "부서 코드") @RequestParam(value = "deptCd", required = false) String deptCd,
            @Parameter(name = "inputrId", description = "입력자 아이디") @RequestParam(value = "inputrId", required = false) String inputrId,
            @Parameter(name = "brdcPgmId", description = "방송 프로그램 아이디") @RequestParam(value = "brdcPgmId", required = false) String brdcPgmId,
            @Parameter(name = "orgArtclId", description = "원본 기사 아이디") @RequestParam(value = "orgArtclId", required = false) Long orgArtclId,
            @Parameter(name = "delYn", description = "삭제 여부") @RequestParam(value = "delYn", required = false) String delYn,
            @Parameter(name = "searchDivCd", description = "검색구분코드<br>01 - 기사제목<br>02 - 기자명") @RequestParam(value = "searchDivCd", required = false) String searchDivCd,
            @Parameter(name = "searchWord", description = "검색키워드") @RequestParam(value = "searchWord", required = false) String searchWord,
            @Parameter(name = "apprvDivCdList", description = "픽스구분코드(fix_none,article_fix,editor_fix,anchor_fix,desk_fix)") @RequestParam(value = "apprvDivCdList", required = false) List<String> apprvDivCdList,
            @Parameter(name = "page", description = "시작페이지") @RequestParam(value = "page", required = false) Integer page,
            @Parameter(name = "limit", description = "한 페이지에 데이터 수") @RequestParam(value = "limit", required = false) Integer limit) throws Exception {


        //기사읽기 권한이 없는 사용자 error.forbidden
        //List<String> userAuth = userAuthService.authChk(AuthEnum.ArticleRead.getAuth(), AuthEnum.AdminRead.getAuth());
        //if (userAuthService.authChks(AuthEnum.ArticleRead.getAuth(), AuthEnum.AdminMode.getAuth())) { //기사읽기 권한이거나, 관리자 읽기 권한일 경우 가능.
        //    throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        //}

        SearchDate searchDate = new SearchDate(date, date);

        PageResultDTO<ArticleDTO, Article> pageList = articleService.findAllIsuue(searchDate.getStartDate(),
                searchDate.getEndDate(), issuKwd, artclDivCd, artclTypCd, artclTypDtlCd, artclCateCd, deptCd, inputrId,
                brdcPgmId, orgArtclId, delYn, searchDivCd, searchWord, page, limit, apprvDivCdList);


        return new AnsApiResponse<>(pageList);
    }

    @Operation(summary = "기사 상세조회", description = "기사 상세조회")
    @GetMapping(path = "/{artclId}")
    public AnsApiResponse<ArticleDTO> find(@Parameter(name = "artclId", required = true, description = "기사 아이디")
                                           @PathVariable("artclId") Long artclId) {

        ArticleDTO articleDTO = articleService.find(artclId);

        return new AnsApiResponse<>(articleDTO);
    }

    @Operation(summary = "기사 등록", description = "기사 등록")
    @PostMapping(name = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<ArticleSimpleDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                                   @RequestBody @Valid ArticleCreateDTO articleCreateDTO) throws Exception {


        String userId = userAuthService.authUser.getUserId();

        log.debug("Article Create : User Id - " + userId + "<br>" + "Create Model - " + articleCreateDTO);

        Long artclId = articleService.create(articleCreateDTO, userId);

        //기사 등록 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        ArticleSimpleDTO articleDTO = new ArticleSimpleDTO();
        articleDTO.setArtclId(artclId);

        log.info("Article Create Success ID : " + articleDTO);

        return new AnsApiResponse<>(articleDTO);
    }

    @Operation(summary = "기사 수정", description = "기사 수정")
    @PutMapping(path = "/{artclId}")
    public AnsApiResponse<ArticleSimpleDTO> update(@Parameter(description = "필수값<br> ", required = true)
                                                   @RequestBody @Valid ArticleUpdateDTO articleUpdateDTO,
                                                   @Parameter(name = "artclId", required = true, description = "기사 아이디")
                                                   @PathVariable("artclId") Long artclId) throws Exception {

        String userId = userAuthService.authUser.getUserId();
        log.debug("Article Update : User Id - " + userId + "<br>" +
                " Article Model -" + articleUpdateDTO);

        //수정. 잠금사용자확인
        if (articleService.chkOrderLock(artclId, userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //해당기사 잠금여부가 Y일 경우 NOT_FOUND EXPCEPTION.
        }

        articleService.update(articleUpdateDTO, artclId, userId);

        /* ArticleDTO articleDTO = articleService.find(artclId);*/
        //기사 수정 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        ArticleSimpleDTO articleDTO = new ArticleSimpleDTO();
        articleDTO.setArtclId(artclId);

        log.info("Article Update Success Id : " + artclId + "Update Model" + articleDTO);

        return new AnsApiResponse<>(articleDTO);

    }

    @Operation(summary = "기사 삭제", description = "기사 삭제")
    @DeleteMapping(path = "/{artclId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "artclId", required = true, description = "기사 아이디")
                                    @PathVariable("artclId") Long artclId) throws Exception {

        String userId = userAuthService.authUser.getUserId();
        log.info("Article Delete  : User Id - " + userId + "<br>" +
                " Article Id" + artclId);

        articleService.delete(artclId, userId);

        return AnsApiResponse.noContent();

    }

    @Operation(summary = "기사 수정권한 확인", description = "기사 수정권한 확인")
    @PutMapping(path = "/{artclId}/confirm")
    public AnsApiResponse<ArticleAuthConfirmDTO> articleConfirm(@Parameter(name = "artclId", required = true, description = "기사 아이디")
                                                                @PathVariable("artclId") Long artclId) {

        // 사용자 정보
        String userId = userAuthService.authUser.getUserId();//토큰에서 유저 아이디를 가져온다.

        //권한 확인 로그
        log.info("Article Confirm : Article Id" + artclId + " User Id: " + userId);

        ArticleAuthConfirmDTO articleAuthConfirmDTO = articleService.articleConfirm(artclId, userId);

        /*if (ObjectUtils.isEmpty(article) == false){
            ArticleAuthConfirmDTO articleAuthConfirmDTO = articleService.errorArticleAuthConfirm(article);
            return new AnsApiResponse<>(articleAuthConfirmDTO);
        }*/
        return new AnsApiResponse<>(articleAuthConfirmDTO);
    }

    @Operation(summary = "기사 잠금", description = "기사 잠금")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "FORBIDDEN(사용자 권한이 없다.)"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN(잠금사용자가 다르다)")})
    @PutMapping(path = "/{artclId}/lock")
    public AnsApiResponse<ArticleDTO> articleLock(@Parameter(name = "artclId", required = true, description = "기사 아이디")
                                                  @PathVariable("artclId") Long artclId,
                                                  @Parameter(description = "필수값<br> lckYn ", required = true)
                                                  @RequestBody @Valid ArticleLockDTO articleLockDTO) throws Exception {

        String userId = userAuthService.authUser.getUserId();
        log.info("Article Lock : ArticleId - " + artclId + " User Id - " + userId
                + "<br>" + " Lock Info : " + articleLockDTO);

        //권한체크(기사 쓰기)
        if (userAuthChkService.authChk(AuthEnum.ArticleWrite.getAuth())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        articleService.articleLock(artclId, articleLockDTO, userId);

        ArticleDTO articleDTO = articleService.find(artclId);

        return new AnsApiResponse<>(articleDTO);
    }

    @Operation(summary = "기사 잠금해제", description = "기사 잠금해제")
    @PutMapping(path = "/{artclId}/unlock")
    public AnsApiResponse<ArticleDTO> articleUnlock(@Parameter(name = "artclId", required = true, description = "기사 아이디")
                                                    @PathVariable("artclId") Long artclId,
                                                    @Parameter(description = "필수값<br> lckYn ", required = true)
                                                    @RequestBody @Valid ArticleLockDTO articleLockDTO) throws Exception {

        String userId = userAuthService.authUser.getUserId();
        log.info("Article Unlock : ArticleId = " + artclId + " User Id - " + userId + "" +
                "<br>" + " Unlock Info : " + articleLockDTO);

        //권한, 작성자 확인
        articleService.articleUnlock(artclId, articleLockDTO, userId);

        ArticleDTO articleDTO = articleService.find(artclId);

        return new AnsApiResponse<>(articleDTO);
    }


    @Operation(summary = "기사 픽스", description = "기사 픽스")
    @PutMapping(path = "/{artclId}/fix")
    public AnsApiResponse<ArticleDTO> fix(@Parameter(name = "artclId", description = "기사 아이디")
                                          @PathVariable("artclId") Long artclId,
                                          @Parameter(name = "apprvDivCd", description = "픽스 상태 코드(fix_none[픽스가 없는상태]" +
                                                  ", article_fix[기자 픽스], editor_fix[에디터 픽스], anchor_fix[앵커 픽스], desk_fix[데스크 픽스])")
                                          @RequestParam(value = "apprvDivCd", required = true) String apprvDivCd) throws Exception {

        // 사용자 정보
        String userId = userAuthService.authUser.getUserId();

        //픽스정보
        log.info("Article Fix Info : ArticleId = " + artclId /*+" OrgApprvDivcd : " + orgApprvDivcd*/ + "NewApprvDivcd : " + apprvDivCd
                + " User Id : " + userId);

        articleService.vaildFixStaus(artclId, apprvDivCd, userId);

        ArticleDTO articleDTO = articleService.find(artclId);

        return new AnsApiResponse<>(articleDTO);
    }

    @Operation(summary = "기사 삭제자 확인", description = "기사 삭제자 확인")
    @PutMapping(path = "/{artclId}/confirmuser")
    public AnsApiResponse<?> confirmUser(@Parameter(description = "필수값<br> lckYn ", required = true)
                                         @RequestBody @Valid ArticleDeleteConfirmDTO articleDeleteConfirmDTO,
                                         @Parameter(name = "artclId", description = "기사 아이디")
                                         @PathVariable("artclId") Long artclId) throws NoSuchAlgorithmException {

        String userId = userAuthService.authUser.getUserId(); //토큰에서 유저 아이디를 가져온다.
        log.info(" Article Delete User Confirm : Article Id - " + artclId + "User Id :" + userId);

        articleService.confirmUser(articleDeleteConfirmDTO, artclId, userId);

        return AnsApiResponse.ok();
    }
}
