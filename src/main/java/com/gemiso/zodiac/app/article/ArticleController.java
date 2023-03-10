package com.gemiso.zodiac.app.article;

import com.gemiso.zodiac.app.article.dto.*;
import com.gemiso.zodiac.app.elasticsearch.ElasticSearchArticleService;
import com.gemiso.zodiac.app.elasticsearch.articleDTO.ElasticSearchArticleDTO;
import com.gemiso.zodiac.app.elasticsearch.articleEntity.ElasticSearchArticle;
import com.gemiso.zodiac.core.enumeration.AuthEnum;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.page.PageResultDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.gemiso.zodiac.core.service.JwtGetUserService;
import com.gemiso.zodiac.core.service.UserAuthChkService;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(description = "기사 API")
@RestController
@RequestMapping("/article")
@Slf4j
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ElasticSearchArticleService elasticSearchArticleService;

    //private final UserAuthService userAuthService;
    private final UserAuthChkService userAuthChkService;
    private final JwtGetUserService jwtGetUserService;

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
            @Parameter(name = "deptCd", description = "부서코드") @RequestParam(value = "deptCd", required = false) Long deptCd,
            @Parameter(name = "artclCateCd", description = "기사 카테고리 코드") @RequestParam(value = "artclCateCd", required = false) String artclCateCd,
            @Parameter(name = "artclTypDtlCd", description = "기사 유형 상세 코드") @RequestParam(value = "artclTypDtlCd", required = false) String artclTypDtlCd,
            @Parameter(name = "delYn", description = "삭제 여부") @RequestParam(value = "delYn", required = false) String delYn,
            @Parameter(name = "artclId", description = "기사아이디") @RequestParam(value = "artclId", required = false) Long artclId,
            @Parameter(name = "orgArtclId", description = "원본기사 아이디") @RequestParam(value = "orgArtclId", required = false) Long orgArtclId,
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
                    artclCateCd, artclTypDtlCd, delYn, artclId, copyYn, orgArtclId);
            //검색조건 날짜형식이 안들어왔을경우
        } else {

            pageList = articleService.findAll(null, null, rcvDt, rptrId, inputrId, brdcPgmId, artclDivCd,
                    artclTypCd, searchDivCd, searchWord, page, limit, apprvDivCdList, deptCd, artclCateCd,
                    artclTypDtlCd, delYn, artclId, copyYn, orgArtclId);

        }

        return new AnsApiResponse<>(pageList);
    }

    @Operation(summary = "기사 목록조회 [엘라스틱]", description = "기사 목록조회 [엘라스틱]")
    @GetMapping(path = "/search")
    public AnsApiResponse<?> findAllElasticsearch(
            @Parameter(name = "sdate", description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
            @Parameter(name = "edate", description = "검색 종료 날짜(yyyy-MM-dd)", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
            @Parameter(name = "rptrId", description = "기자 아이디") @RequestParam(value = "rptrId", required = false) String rptrId,
            @Parameter(name = "inputrId", description = "등록자 아이디") @RequestParam(value = "inputrId", required = false) String inputrId,
            @Parameter(name = "brdcPgmId", description = "방송프로그램 아이디") @RequestParam(value = "brdcPgmId", required = false) String brdcPgmId,
            @Parameter(name = "artclDivCd", description = "기사구분코드(01:일반, 02:전체, 03:이슈)") @RequestParam(value = "artclDivCd", required = false) String artclDivCd,
            @Parameter(name = "artclTypCd", description = "기사유형코드(01:스트레이트, 02:리포트, 03:C/T, 04:하단롤, 05:긴급자막)") @RequestParam(value = "artclTypCd", required = false) String artclTypCd,
            @Parameter(name = "searchDivCd", description = "검색구분코드<br>01 - 기사제목<br>02 - 원본 기사 아이디") @RequestParam(value = "searchDivCd", required = false) String searchDivCd,
            @Parameter(name = "searchWord", description = "검색키워드") @RequestParam(value = "searchWord", required = false) String searchWord,
            @Parameter(name = "page", description = "시작페이지") @RequestParam(value = "page", required = false) Integer page,
            @Parameter(name = "limit", description = "한 페이지에 데이터 수") @RequestParam(value = "limit", required = false) Integer limit,
            @Parameter(name = "apprvDivCdList", description = "픽스구분코드(fix_none,article_fix,editor_fix,anchor_fix,desk_fix)") @RequestParam(value = "apprvDivCdList", required = false) List<String> apprvDivCdList,
            @Parameter(name = "deptCd", description = "부서코드") @RequestParam(value = "deptCd", required = false) Long deptCd,
            @Parameter(name = "artclCateCd", description = "기사 카테고리 코드") @RequestParam(value = "artclCateCd", required = false) String artclCateCd,
            @Parameter(name = "artclTypDtlCd", description = "기사 유형 상세 코드") @RequestParam(value = "artclTypDtlCd", required = false) String artclTypDtlCd,
            @Parameter(name = "delYn", description = "삭제 여부") @RequestParam(value = "delYn", required = false) String delYn,
            @Parameter(name = "artclId", description = "기사아이디") @RequestParam(value = "artclId", required = false) Long artclId,
            @Parameter(name = "orgArtclId", description = "원본기사 아이디") @RequestParam(value = "orgArtclId", required = false) Long orgArtclId,
            @Parameter(name = "copyYn", description = "기사 복사여부[오리지날 기사 : N, 복사기사 : Y]") @RequestParam(value = "copyYn", required = false) String copyYn,
            @Parameter(name = "cueId", description = "큐시트 아이디") @RequestParam(value = "cueId", required = false) Long cueId) throws Exception {

        PageResultDTO<ElasticSearchArticleDTO, ElasticSearchArticle> pageList = null;

        Long count = 0L;
        //List<ArticleDTO> articleDTOList = new ArrayList<>();

        //기사읽기 권한이 없는 사용자 error.forbidden
        //List<String> userAuth = userAuthService.authChk(AuthEnum.ArticleRead.getAuth(), AuthEnum.AdminRead.getAuth());
        // if (userAuthService.authChks(AuthEnum.ArticleRead.getAuth(), AuthEnum.AdminMode.getAuth())) { //기사읽기 권한이거나, 관리자 읽기 권한일 경우 가능.
        //     throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        // }

        //검색조건 날짜형식이 들어왔을경우
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {

            SearchDate searchDate = new SearchDate(sdate, edate);

            pageList = articleService.findAllElasticsearch(searchDate.getStartDate(), searchDate.getEndDate(), rptrId, inputrId,
                    brdcPgmId, artclDivCd, artclTypCd, searchDivCd, searchWord, page, limit, apprvDivCdList, deptCd,
                    artclCateCd, artclTypDtlCd, delYn, artclId, copyYn, orgArtclId, cueId);

            count = articleService.findAllStatisticsCount(searchDate.getStartDate(), searchDate.getEndDate(), rptrId, inputrId,
                    brdcPgmId, artclDivCd, artclTypCd, searchDivCd, searchWord, apprvDivCdList, deptCd,
                    artclCateCd, artclTypDtlCd, delYn, artclId, copyYn, orgArtclId, cueId);

            //엘라스틱서치 lock데이터 추가
            pageList = articleService.lockInfoAdd(pageList);

            pageList.setTotalCount(count);
            //검색조건 날짜형식이 안들어왔을경우
        } else {

            pageList = articleService.findAllElasticsearch(null, null, rptrId, inputrId, brdcPgmId, artclDivCd,
                    artclTypCd, searchDivCd, searchWord, page, limit, apprvDivCdList, deptCd, artclCateCd,
                    artclTypDtlCd, delYn, artclId, copyYn, orgArtclId, cueId);

            count = articleService.findAllStatisticsCount(null, null, rptrId, inputrId,
                    brdcPgmId, artclDivCd, artclTypCd, searchDivCd, searchWord, apprvDivCdList, deptCd,
                    artclCateCd, artclTypDtlCd, delYn, artclId, copyYn, orgArtclId, cueId);

            //엘라스틱서치 lock데이터 추가
            pageList = articleService.lockInfoAdd(pageList);

            pageList.setTotalCount(count);

        }

        return new AnsApiResponse<>(pageList);
    }

    @Operation(summary = "기사 통계 엑셀 다운로드", description = "기사 통계 엑셀 다운로드")
    @GetMapping(path = "/statistics")
    public AnsApiResponse<?> findAllStatistics(
            @Parameter(name = "sdate", description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
            @Parameter(name = "edate", description = "검색 종료 날짜(yyyy-MM-dd)", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
            @Parameter(name = "rptrId", description = "기자 아이디") @RequestParam(value = "rptrId", required = false) String rptrId,
            @Parameter(name = "inputrId", description = "등록자 아이디") @RequestParam(value = "inputrId", required = false) String inputrId,
            @Parameter(name = "brdcPgmId", description = "방송프로그램 아이디") @RequestParam(value = "brdcPgmId", required = false) String brdcPgmId,
            @Parameter(name = "artclDivCd", description = "기사구분코드(01:일반, 02:전체, 03:이슈)") @RequestParam(value = "artclDivCd", required = false) String artclDivCd,
            @Parameter(name = "artclTypCd", description = "기사유형코드(01:스트레이트, 02:리포트, 03:C/T, 04:하단롤, 05:긴급자막)") @RequestParam(value = "artclTypCd", required = false) String artclTypCd,
            @Parameter(name = "searchDivCd", description = "검색구분코드<br>01 - 기사제목<br>02 - 원본 기사 아이디") @RequestParam(value = "searchDivCd", required = false) String searchDivCd,
            @Parameter(name = "searchWord", description = "검색키워드") @RequestParam(value = "searchWord", required = false) String searchWord,
            @Parameter(name = "apprvDivCdList", description = "픽스구분코드(fix_none,article_fix,editor_fix,anchor_fix,desk_fix)") @RequestParam(value = "apprvDivCdList", required = false) List<String> apprvDivCdList,
            @Parameter(name = "deptCd", description = "부서코드") @RequestParam(value = "deptCd", required = false) Long deptCd,
            @Parameter(name = "artclCateCd", description = "기사 카테고리 코드") @RequestParam(value = "artclCateCd", required = false) String artclCateCd,
            @Parameter(name = "artclTypDtlCd", description = "기사 유형 상세 코드") @RequestParam(value = "artclTypDtlCd", required = false) String artclTypDtlCd,
            @Parameter(name = "delYn", description = "삭제 여부") @RequestParam(value = "delYn", required = false) String delYn,
            @Parameter(name = "artclId", description = "기사아이디") @RequestParam(value = "artclId", required = false) Long artclId,
            @Parameter(name = "orgArtclId", description = "원본기사 아이디") @RequestParam(value = "orgArtclId", required = false) Long orgArtclId,
            @Parameter(name = "copyYn", description = "기사 복사여부[오리지날 기사 : N, 복사기사 : Y]") @RequestParam(value = "copyYn", required = false) String copyYn,
            @Parameter(name = "cueId", description = "큐시트 아이디") @RequestParam(value = "cueId", required = false) Long cueId,
            @Parameter(name = "page", description = "시작페이지") @RequestParam(value = "page", required = false) Integer page,
            HttpServletResponse response) throws Exception {

        List<ElasticSearchArticle> articleList = new ArrayList<>();

        //검색조건 날짜형식이 들어왔을경우
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {

            SearchDate searchDate = new SearchDate(sdate, edate);

            articleList = articleService.findAllStatistics(searchDate.getStartDate(), searchDate.getEndDate(), rptrId, inputrId,
                    brdcPgmId, artclDivCd, artclTypCd, searchDivCd, searchWord, apprvDivCdList, deptCd,
                    artclCateCd, artclTypDtlCd, delYn, artclId, copyYn, orgArtclId, cueId, page);

            articleService.excelDownload(response, articleList, sdate);

            //엘라스틱서치 lock데이터 추가
            //pageList = articleService.lockInfoAdd(pageList);
            //검색조건 날짜형식이 안들어왔을경우
        } else {

            articleList = articleService.findAllStatistics(null, null, rptrId, inputrId, brdcPgmId, artclDivCd,
                    artclTypCd, searchDivCd, searchWord, apprvDivCdList, deptCd, artclCateCd,
                    artclTypDtlCd, delYn, artclId, copyYn, orgArtclId, cueId, page);

            articleService.excelDownload(response, articleList, sdate);

            //엘라스틱서치 lock데이터 추가
            //pageList = articleService.lockInfoAdd(pageList);

        }

        return null;
    }

    @Operation(summary = "기사 통계 엑셀 다운로드[ count ]", description = "기사 통계 엑셀 다운로드[ count ]")
    @GetMapping(path = "/count")
    public AnsApiResponse<?> findAllStatisticsCount(
            @Parameter(name = "sdate", description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
            @Parameter(name = "edate", description = "검색 종료 날짜(yyyy-MM-dd)", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
            @Parameter(name = "rptrId", description = "기자 아이디") @RequestParam(value = "rptrId", required = false) String rptrId,
            @Parameter(name = "inputrId", description = "등록자 아이디") @RequestParam(value = "inputrId", required = false) String inputrId,
            @Parameter(name = "brdcPgmId", description = "방송프로그램 아이디") @RequestParam(value = "brdcPgmId", required = false) String brdcPgmId,
            @Parameter(name = "artclDivCd", description = "기사구분코드(01:일반, 02:전체, 03:이슈)") @RequestParam(value = "artclDivCd", required = false) String artclDivCd,
            @Parameter(name = "artclTypCd", description = "기사유형코드(01:스트레이트, 02:리포트, 03:C/T, 04:하단롤, 05:긴급자막)") @RequestParam(value = "artclTypCd", required = false) String artclTypCd,
            @Parameter(name = "searchDivCd", description = "검색구분코드<br>01 - 기사제목<br>02 - 원본 기사 아이디") @RequestParam(value = "searchDivCd", required = false) String searchDivCd,
            @Parameter(name = "searchWord", description = "검색키워드") @RequestParam(value = "searchWord", required = false) String searchWord,
            @Parameter(name = "apprvDivCdList", description = "픽스구분코드(fix_none,article_fix,editor_fix,anchor_fix,desk_fix)") @RequestParam(value = "apprvDivCdList", required = false) List<String> apprvDivCdList,
            @Parameter(name = "deptCd", description = "부서코드") @RequestParam(value = "deptCd", required = false) Long deptCd,
            @Parameter(name = "artclCateCd", description = "기사 카테고리 코드") @RequestParam(value = "artclCateCd", required = false) String artclCateCd,
            @Parameter(name = "artclTypDtlCd", description = "기사 유형 상세 코드") @RequestParam(value = "artclTypDtlCd", required = false) String artclTypDtlCd,
            @Parameter(name = "delYn", description = "삭제 여부") @RequestParam(value = "delYn", required = false) String delYn,
            @Parameter(name = "artclId", description = "기사아이디") @RequestParam(value = "artclId", required = false) Long artclId,
            @Parameter(name = "orgArtclId", description = "원본기사 아이디") @RequestParam(value = "orgArtclId", required = false) Long orgArtclId,
            @Parameter(name = "copyYn", description = "기사 복사여부[오리지날 기사 : N, 복사기사 : Y]") @RequestParam(value = "copyYn", required = false) String copyYn,
            @Parameter(name = "cueId", description = "큐시트 아이디") @RequestParam(value = "cueId", required = false) Long cueId) throws Exception {

        Long count = 0L;

        //검색조건 날짜형식이 들어왔을경우
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {

            SearchDate searchDate = new SearchDate(sdate, edate);

            count = articleService.findAllStatisticsCount(searchDate.getStartDate(), searchDate.getEndDate(), rptrId, inputrId,
                    brdcPgmId, artclDivCd, artclTypCd, searchDivCd, searchWord, apprvDivCdList, deptCd,
                    artclCateCd, artclTypDtlCd, delYn, artclId, copyYn, orgArtclId, cueId);


        } else {

            count = articleService.findAllStatisticsCount(null, null, rptrId, inputrId, brdcPgmId, artclDivCd,
                    artclTypCd, searchDivCd, searchWord, apprvDivCdList, deptCd, artclCateCd,
                    artclTypDtlCd, delYn, artclId, copyYn, orgArtclId, cueId);


        }

        return new AnsApiResponse<>(count);
    }

    @Operation(summary = "기사 목록조회[큐시트]", description = "기사 목록조회[큐시트]")
    @GetMapping(path = "/searchcue")
    public AnsApiResponse<PageResultDTO<ElasticSearchArticleDTO, ElasticSearchArticle>> findAllElasticsearchCue(@Parameter(name = "sdate", description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = true)
                                                                                                                @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                                                                                @Parameter(name = "edate", description = "검색 종료 날짜(yyyy-MM-dd)", required = true)
                                                                                                                @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                                                                                @Parameter(name = "searchWord", description = "검색키워드")
                                                                                                                @RequestParam(value = "searchWord", required = false) String searchWord,
                                                                                                                @Parameter(name = "cueId", description = "검색키워드", required = false)
                                                                                                                @RequestParam(value = "cueId") Long cueId,
                                                                                                                @Parameter(name = "brdcPgmId", description = "방송프로그램 아이디")
                                                                                                                @RequestParam(value = "brdcPgmId", required = false) String brdcPgmId,
                                                                                                                @Parameter(name = "artclTypCd", description = "기사유형코드(01:스트레이트, 02:리포트, 03:C/T, 04:하단롤, 05:긴급자막)")
                                                                                                                @RequestParam(value = "artclTypCd", required = false) String artclTypCd,
                                                                                                                @Parameter(name = "artclTypDtlCd", description = "기사 유형 상세 코드")
                                                                                                                @RequestParam(value = "artclTypDtlCd", required = false) String artclTypDtlCd,
                                                                                                                @Parameter(name = "copyYn", description = "기사 복사여부[오리지날 기사 : N, 복사기사 : Y]")
                                                                                                                @RequestParam(value = "copyYn", required = false) String copyYn,
                                                                                                                @Parameter(name = "deptCd", description = "부서 코드")
                                                                                                                @RequestParam(value = "deptCd", required = false) Long deptCd,
                                                                                                                @Parameter(name = "orgArtclId", description = "원본기사 아이디")
                                                                                                                @RequestParam(value = "orgArtclId", required = false) Long orgArtclId,
                                                                                                                @Parameter(name = "rptrId", description = "기자 아이디")
                                                                                                                @RequestParam(value = "rptrId", required = false) String rptrId,
                                                                                                                @Parameter(name = "page", description = "시작페이지")
                                                                                                                @RequestParam(value = "page", required = false) Integer page,
                                                                                                                @Parameter(name = "limit", description = "한 페이지에 데이터 수")
                                                                                                                @RequestParam(value = "limit", required = false) Integer limit) throws Exception {


        SearchDate searchDate = new SearchDate(sdate, edate);

        PageResultDTO<ElasticSearchArticleDTO, ElasticSearchArticle> pageList = articleService.findAllElasticsearchCue(searchDate.getStartDate(), searchDate.getEndDate(),
                searchWord, cueId, brdcPgmId, artclTypCd, artclTypDtlCd, copyYn, deptCd, orgArtclId, rptrId, page, limit);

        pageList = articleService.confirmArticleListElastic(pageList, cueId);

        //엘라스틱서치 lock데이터 추가
        pageList = articleService.lockInfoAdd(pageList);

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
                                                                      @Parameter(name = "artclTypCd", description = "기사유형코드(01:스트레이트, 02:리포트, 03:C/T, 04:하단롤, 05:긴급자막)")
                                                                      @RequestParam(value = "artclTypCd", required = false) String artclTypCd,
                                                                      @Parameter(name = "artclTypDtlCd", description = "기사 유형 상세 코드")
                                                                      @RequestParam(value = "artclTypDtlCd", required = false) String artclTypDtlCd,
                                                                      @Parameter(name = "copyYn", description = "기사 복사여부[오리지날 기사 : N, 복사기사 : Y]")
                                                                      @RequestParam(value = "copyYn", required = false) String copyYn,
                                                                      @Parameter(name = "deptCd", description = "부서 코드")
                                                                      @RequestParam(value = "deptCd", required = false) Long deptCd,
                                                                      @Parameter(name = "orgArtclId", description = "원본기사 아이디")
                                                                      @RequestParam(value = "orgArtclId", required = false) Long orgArtclId,
                                                                      @Parameter(name = "page", description = "시작페이지")
                                                                      @RequestParam(value = "page", required = false) Integer page,
                                                                      @Parameter(name = "limit", description = "한 페이지에 데이터 수")
                                                                      @RequestParam(value = "limit", required = false) Integer limit) throws Exception {


        SearchDate searchDate = new SearchDate(sdate, edate);

        PageResultDTO<ArticleDTO, Article> pageList = articleService.findCue(searchDate.getStartDate(), searchDate.getEndDate(),
                searchWord, cueId, brdcPgmId, artclTypCd, artclTypDtlCd, copyYn, deptCd, orgArtclId, page, limit);

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
            @Parameter(name = "deptCd", description = "부서 코드") @RequestParam(value = "deptCd", required = false) Long deptCd,
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

    @Operation(summary = "기사 상세조회[ 삭제 기사 ]", description = "기사 상세조회[ 삭제 기사 ]")
    @GetMapping(path = "/{artclId}/deletearticle")
    public AnsApiResponse<ArticleDTO> findDeleteArticle(@Parameter(name = "artclId", required = true, description = "기사 아이디")
                                                        @PathVariable("artclId") Long artclId) {

        ArticleDTO articleDTO = articleService.findDeleteArticle(artclId);

        return new AnsApiResponse<>(articleDTO);
    }

    @Operation(summary = "기사 등록", description = "기사 등록")
    @PostMapping(name = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<ArticleSimpleDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                                   @RequestBody @Valid ArticleCreateDTO articleCreateDTO,
                                                   @Parameter(name = "userId", description = "사용자 아이디")
                                                   @RequestParam(value = "userId", required = false) String userId,
                                                   @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {


        String getUserId = jwtGetUserService.getUser(Authorization);

        //String getuserIp = userAuthService.userip;
        //String filterPasingUserId = userAuthService.authUser.getUserId();

        if (getUserId.equals(userId) == false) {

            log.error("User Un matching client userId : " + userId + " serverUserId : " + getUserId + " token : " + Authorization);

            if (userId == null || userId.trim().isEmpty()) {

                Article article = articleService.create(articleCreateDTO, getUserId);

                //엘라스틱서치 등록
                elasticSearchArticleService.elasticPush(article);

                //기사 등록 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
                ArticleSimpleDTO articleDTO = new ArticleSimpleDTO();
                articleDTO.setArtclId(article.getArtclId());
                articleDTO.setOrgArtclId(article.getOrgArtclId());


                log.info("Article Create Success ID : " + articleDTO);

                return new AnsApiResponse<>(articleDTO);

            }

            Article article = articleService.create(articleCreateDTO, userId);

            //엘라스틱서치 등록
            elasticSearchArticleService.elasticPush(article);

            //기사 등록 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
            ArticleSimpleDTO articleDTO = new ArticleSimpleDTO();
            articleDTO.setArtclId(article.getArtclId());
            articleDTO.setOrgArtclId(article.getOrgArtclId());


            log.info("Article Create Success ID : " + articleDTO);

            return new AnsApiResponse<>(articleDTO);
        }else {

            log.info("Article Create : User Id - " + getUserId + "<br>" + "Create Model - " + articleCreateDTO);


            Article article = articleService.create(articleCreateDTO, getUserId);

            //엘라스틱서치 등록
            elasticSearchArticleService.elasticPush(article);

            //기사 등록 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
            ArticleSimpleDTO articleDTO = new ArticleSimpleDTO();
            articleDTO.setArtclId(article.getArtclId());
            articleDTO.setOrgArtclId(article.getOrgArtclId());


            log.info("Article Create Success ID : " + articleDTO);

            return new AnsApiResponse<>(articleDTO);
        }
    }

    @Operation(summary = "기사 수정", description = "기사 수정")
    @PutMapping(path = "/{artclId}")
    public AnsApiResponse<?> update(@Parameter(description = "필수값<br> ", required = true)
                                    @RequestBody @Valid ArticleUpdateDTO articleUpdateDTO,
                                    @Parameter(name = "artclId", required = true, description = "기사 아이디")
                                    @PathVariable("artclId") Long artclId,
                                    @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        String userId = jwtGetUserService.getUser(Authorization);

        log.info("Article Update : User Id - " + userId + "<br>" +
                " Article Model -" + articleUpdateDTO);

        //수정. 잠금사용자확인
        ArticleAuthConfirmDTO articleAuthConfirmDTO = articleService.chkOrderLock(artclId, userId);

        if (ObjectUtils.isEmpty(articleAuthConfirmDTO) == false) {
            return new AnsApiResponse<>(articleAuthConfirmDTO);
        }

        Article article = articleService.update(articleUpdateDTO, artclId, userId);

        //엘라스틱서치 등록
        elasticSearchArticleService.elasticPush(article);

        //기사 수정 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        ArticleSimpleDTO articleDTO = new ArticleSimpleDTO();
        articleDTO.setArtclId(artclId);

        log.info("Article Update Success Id : " + artclId + "Update Model" + articleDTO);

        return new AnsApiResponse<>(articleDTO);

    }

    @Operation(summary = "기사 수정[extra time]", description = "기사 수정[extra time]")
    @PutMapping(path = "/{artclId}/extratime")
    public AnsApiResponse<?> updateExtraTime(@Parameter(description = "필수값<br> ", required = true)
                                             @RequestBody @Valid ArticleExtraTimeUpdateDTO articleExtraTimeUpdateDTO,
                                             @Parameter(name = "artclId", required = true, description = "기사 아이디")
                                             @PathVariable("artclId") Long artclId,
                                             @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        String userId = jwtGetUserService.getUser(Authorization);

        log.info("Article Extra Time Update : User Id - " + userId + "<br>" +
                " Article Update Model -" + articleExtraTimeUpdateDTO.toString());

        //수정. 잠금사용자확인

        Article article = articleService.updateExtraTime(articleExtraTimeUpdateDTO, artclId, userId);

        //엘라스틱서치 등록
        elasticSearchArticleService.elasticPush(article);

        //기사 수정 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        ArticleSimpleDTO articleDTO = new ArticleSimpleDTO();
        articleDTO.setArtclId(artclId);

        log.info("Article Extra Time Update Success Id : " + artclId + "Update Model" + articleDTO);

        return new AnsApiResponse<>(articleDTO);

    }

    @Operation(summary = "기사 삭제", description = "기사 삭제")
    @DeleteMapping(path = "/{artclId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "artclId", required = true, description = "기사 아이디")
                                    @PathVariable("artclId") Long artclId,
                                    @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        String userId = jwtGetUserService.getUser(Authorization);
        log.info("Article Delete  : User Id - " + userId + "<br>" +
                " Article Id" + artclId);

        Article article = articleService.delete(artclId, userId);

        //엘라스틱서치 등록
        elasticSearchArticleService.elasticPush(article);

        return AnsApiResponse.noContent();

    }

    @Operation(summary = "기사 삭제자 확인", description = "기사 삭제자 확인")
    @PutMapping(path = "/{artclId}/confirmuser")
    public AnsApiResponse<?> confirmUser(@Parameter(description = "필수값<br> lckYn ", required = true)
                                         @RequestBody @Valid ArticleDeleteConfirmDTO articleDeleteConfirmDTO,
                                         @Parameter(name = "artclId", description = "기사 아이디")
                                         @PathVariable("artclId") Long artclId,
                                         @RequestHeader(value = "Authorization", required = false) String Authorization
    ) throws Exception {

        String userId =jwtGetUserService.getUser(Authorization); //토큰에서 유저 아이디를 가져온다.
        log.info(" Article Delete User Confirm : Article Id - " + artclId + "User Id :" + userId);

        articleService.confirmUser(articleDeleteConfirmDTO, artclId, userId);

        return AnsApiResponse.ok();
    }

    @Operation(summary = "기사 수정권한 확인", description = "기사 수정권한 확인")
    @PutMapping(path = "/{artclId}/confirm")
    public AnsApiResponse<ArticleAuthConfirmDTO> articleConfirm(@Parameter(name = "artclId", required = true, description = "기사 아이디")
                                                                @PathVariable("artclId") Long artclId,
                                                                @RequestHeader(value = "Authorization", required = false)String Authorization) throws Exception {

        // 사용자 정보
        String userId =jwtGetUserService.getUser(Authorization);//토큰에서 유저 아이디를 가져온다.

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
    public AnsApiResponse<?> articleLock(@Parameter(name = "artclId", required = true, description = "기사 아이디")
                                         @PathVariable("artclId") Long artclId,
                                         @Parameter(description = "필수값<br> lckYn ", required = true)
                                         @RequestBody @Valid ArticleLockDTO articleLockDTO,
                                         @RequestHeader(value = "Authorization", required = false)String Authorization) throws Exception {

        String userId =jwtGetUserService.getUser(Authorization);
        log.info("Article Lock : ArticleId - " + artclId + " User Id - " + userId
                + "<br>" + " Lock Info : " + articleLockDTO);

        //권한체크(기사 쓰기)
        if (userAuthChkService.authChk(AuthEnum.ArticleWrite.getAuth(), userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        ArticleAuthConfirmDTO articleAuthConfirmDTO = articleService.articleLock(artclId, articleLockDTO, userId);

        if (ObjectUtils.isEmpty(articleAuthConfirmDTO) == false) {
            return new AnsApiResponse<>(articleAuthConfirmDTO);
        }

        ArticleDTO articleDTO = articleService.find(artclId);

        return new AnsApiResponse<>(articleDTO);
    }

    @Operation(summary = "기사 잠금해제", description = "기사 잠금해제")
    @PutMapping(path = "/{artclId}/unlock")
    public AnsApiResponse<ArticleDTO> articleUnlock(@Parameter(name = "artclId", required = true, description = "기사 아이디")
                                                    @PathVariable("artclId") Long artclId,
                                                    @Parameter(description = "필수값<br> lckYn ", required = true)
                                                    @RequestBody @Valid ArticleLockDTO articleLockDTO,
                                                    @RequestHeader(value = "Authorization", required = false)String Authorization) throws Exception {

        String userId =jwtGetUserService.getUser(Authorization);
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
                                          @RequestParam(value = "apprvDivCd", required = true) String apprvDivCd,
                                          @RequestHeader(value = "Authorization", required = false)String Authorization) throws Exception {

        // 사용자 정보
        String userId =jwtGetUserService.getUser(Authorization);

        //픽스정보
        log.info("Article Fix Info : ArticleId = " + artclId /*+" OrgApprvDivcd : " + orgApprvDivcd*/ + "NewApprvDivcd : " + apprvDivCd
                + " User Id : " + userId);

        Article article = articleService.vaildFixStaus(artclId, apprvDivCd, userId);

        //엘라스틱서치 등록
        elasticSearchArticleService.elasticPush(article);

        ArticleDTO articleDTO = articleService.find(artclId);

        return new AnsApiResponse<>(articleDTO);
    }
}
