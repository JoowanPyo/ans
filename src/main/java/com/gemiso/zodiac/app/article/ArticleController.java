package com.gemiso.zodiac.app.article;

import com.gemiso.zodiac.app.article.dto.*;
import com.gemiso.zodiac.core.enumeration.AuthEnum;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.page.PageResultDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.gemiso.zodiac.core.response.ApiCollectionResponse;
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

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Api(description = "기사 API")
@RestController
@RequestMapping("/article")
@Slf4j
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    private final UserAuthChkService userAuthService;

    @Operation(summary = "기사 목록조회", description = "기사 목록조회")
    @GetMapping(path = "")
    public ApiCollectionResponse<?> findAll(
            @Parameter(name = "sdate", description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
            @Parameter(name = "edate", description = "검색 종료 날짜(yyyy-MM-dd)", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
            @Parameter(name = "rcvDt", description = "수신일자(yyyyMMdd)", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date rcvDt,
            @Parameter(name = "rptrId", description = "기자 아이디") @RequestParam(value = "rptrId", required = false) String rptrId,
            @Parameter(name = "brdcPgmId", description = "방송프로그램 아이디") @RequestParam(value = "brdcPgmId", required = false) Long brdcPgmId,
            @Parameter(name = "artclDivCd", description = "기사구분코드(01:일반, 02:전체, 03:이슈)") @RequestParam(value = "artclDivCd", required = false) String artclDivCd,
            @Parameter(name = "artclTypCd", description = "기사유형코드(01:스트레이트, 02:리포트, 03:C/T, 04:하단롤, 05:긴급자막)") @RequestParam(value = "artclTypCd", required = false) String artclTypCd,
            @Parameter(name = "searchDivCd", description = "검색구분코드<br>01 - 기사제목<br>02 - 기자명") @RequestParam(value = "searchDivCd", required = false) String searchDivCd,
            @Parameter(name = "searchWord", description = "검색키워드") @RequestParam(value = "searchWord", required = false) String searchWord,
            @Parameter(name = "page", description = "시작페이지") @RequestParam(value = "page", required = false) Integer page,
            @Parameter(name = "limit", description = "한 페이지에 데이터 수") @RequestParam(value = "limit", required = false) Integer limit,
            @Parameter(name = "issuId", description = "이슈아이디") @RequestParam(value = "issuId", required = false) Long issuId) throws Exception {

        PageResultDTO<ArticleDTO, Article> pageList = null;
        //List<ArticleDTO> articleDTOList = new ArrayList<>();

        //기사읽기 권한이 없는 사용자 error.forbidden
        //List<String> userAuth = userAuthService.authChk(AuthEnum.ArticleRead.getAuth(), AuthEnum.AdminRead.getAuth());
        if (userAuthService.authChks(AuthEnum.ArticleRead.getAuth(), AuthEnum.AdminRead.getAuth())) { //기사읽기 권한이거나, 관리자 읽기 권한일 경우 가능.
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        //articleDivCd가 이슈["02"]일 경우에 이슈아이디로 조회.
        //검색조건 날짜형식이 들어왔을경우
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {

            SearchDate searchDate = new SearchDate(sdate, edate);

            pageList = articleService.findAll(searchDate.getStartDate(), searchDate.getEndDate(), rcvDt, rptrId,
                    brdcPgmId, artclDivCd, artclTypCd, searchDivCd, searchWord, page, limit, issuId);
            //검색조건 날짜형식이 안들어왔을경우
        } else {

            pageList = articleService.findAll(null, null, rcvDt, rptrId, brdcPgmId, artclDivCd,
                    artclTypCd, searchDivCd, searchWord, page, limit, issuId);

        }

        return new ApiCollectionResponse<>(pageList);
    }

    @Operation(summary = "기사 목록조회[큐시트]", description = "기사 목록조회[큐시트]")
    @GetMapping(path = "/cuesheet")
    public AnsApiResponse<List<ArticleDTO>> findCue(@Parameter(name = "sdate", description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = true)
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                    @Parameter(name = "edate", description = "검색 종료 날짜(yyyy-MM-dd)", required = true)
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                    @Parameter(name = "searchWord", description = "검색키워드")
                                                    @RequestParam(value = "searchWord", required = false) String searchWord,
                                                    @Parameter(name = "cueId", description = "검색키워드", required = true)
                                                    @RequestParam(value = "cueId") Long cueId) throws Exception {

        SearchDate searchDate = new SearchDate(sdate, edate);

        List<ArticleDTO> articleDTOList = articleService.findCue(searchDate.getStartDate(), searchDate.getEndDate(), searchWord, cueId);

        return new AnsApiResponse<>(articleDTOList);

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
                                                   @RequestBody @Valid ArticleCreateDTO articleCreateDTO) {

        Long artclId = articleService.create(articleCreateDTO);

        //기사 등록 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        ArticleSimpleDTO articleDTO = new ArticleSimpleDTO();
        articleDTO.setArtclId(artclId);


        return new AnsApiResponse<>(articleDTO);
    }

    @Operation(summary = "기사 수정", description = "기사 수정")
    @PutMapping(path = "/{artclId}")
    public AnsApiResponse<ArticleSimpleDTO> update(@Parameter(description = "필수값<br> ", required = true)
                                                   @RequestBody @Valid ArticleUpdateDTO articleUpdateDTO,
                                                   @Parameter(name = "artclId", required = true, description = "기사 아이디")
                                                   @PathVariable("artclId") Long artclId) {

        //수정. 잠금사용자확인
        if (articleService.chkOrderLock(artclId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //해당기사 잠금여부가 Y일 경우 NOT_FOUND EXPCEPTION.
        }

        articleService.update(articleUpdateDTO, artclId);

        /* ArticleDTO articleDTO = articleService.find(artclId);*/
        //기사 수정 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        ArticleSimpleDTO articleDTO = new ArticleSimpleDTO();
        articleDTO.setArtclId(artclId);

        return new AnsApiResponse<>(articleDTO);

    }

    @Operation(summary = "기사 삭제", description = "기사 삭제")
    @DeleteMapping(path = "/{artclId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "artclId", required = true, description = "기사 아이디")
                                    @PathVariable("artclId") Long artclId) {

        articleService.delete(artclId);

        return AnsApiResponse.noContent();

    }

    @Operation(summary = "기사 잠금", description = "기사 잠금")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "FORBIDDEN(사용자 권한이 없다.)"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN(잠금사용자가 다르다)")})
    @PutMapping(path = "/{artclId}/lock")
    public AnsApiResponse<ArticleDTO> articleLock(@Parameter(name = "artclId", required = true, description = "기사 아이디")
                                                  @PathVariable("artclId") Long artclId,
                                                  @Parameter(description = "필수값<br> lckYn ", required = true)
                                                  @RequestBody @Valid ArticleLockDTO articleLockDTO) {

        if (userAuthService.authChk(AuthEnum.ArticleWrite.getAuth())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        articleService.articleLock(artclId, articleLockDTO);

        ArticleDTO articleDTO = articleService.find(artclId);

        return new AnsApiResponse<>(articleDTO);
    }

    @Operation(summary = "기사 잠금해제", description = "기사 잠금해제")
    @PutMapping(path = "/{artclId}/unlock")
    public AnsApiResponse<ArticleDTO> articleUnlock(@Parameter(name = "artclId", required = true, description = "기사 아이디")
                                                    @PathVariable("artclId") Long artclId,
                                                    @Parameter(description = "필수값<br> lckYn ", required = true)
                                                    @RequestBody @Valid ArticleLockDTO articleLockDTO) {

        //권한, 작성자 확인

        articleService.articleUnlock(artclId, articleLockDTO);

        ArticleDTO articleDTO = articleService.find(artclId);

        return new AnsApiResponse<>(articleDTO);
    }


    @Operation(summary = "기사 픽스", description = "기사 픽스")
    @PutMapping(path = "/{artclId}/fix")
    public AnsApiResponse<ArticleDTO> fix(@Parameter(name = "artclId", description = "기사 아이디")
                                          @PathVariable("artclId") Long artclId,
                                          @Parameter(name = "apprvDivCd", description = "픽스 상태 코드(none[픽스가 없는상태]" +
                                                  ", articlefix[기자 픽스], editorfix[에디터 픽스], anchorfix[앵커 픽스], deskfix[데스크 픽스])")
                                          @RequestParam(value = "apprvDivCd", required = true) String apprvDivCd) {


        articleService.vaildFixStaus(artclId, apprvDivCd);

        ArticleDTO articleDTO = articleService.find(artclId);

        return new AnsApiResponse<>(articleDTO);
    }

    @Operation(summary = "기사 삭제자 확인", description = "기사 삭제자 확인")
    @PutMapping(path = "/confirmuser")
    public AnsApiResponse<?> confirmUser(@Parameter(name = "password", description = "사용자 비밀번호")
                                         @RequestParam(value = "password", required = false) String password) {

        articleService.confirmUser(password);

        return AnsApiResponse.ok();
    }
}
