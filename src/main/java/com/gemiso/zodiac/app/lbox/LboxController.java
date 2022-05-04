package com.gemiso.zodiac.app.lbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gemiso.zodiac.app.article.dto.ArticleAuthConfirmDTO;
import com.gemiso.zodiac.app.articleMedia.ArticleMedia;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaDTO;
import com.gemiso.zodiac.app.lbox.mediaTransportDTO.TransportResponseDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "엘박스 API")
@RestController
@RequestMapping("/lbox")
@RequiredArgsConstructor
@Slf4j
public class LboxController {

    private final LboxService lboxService;

    @Operation(summary = "엘박스 영상 목록 조회", description = "엘박스 영상 목록 조회")
    @GetMapping(path = "")
    public AnsApiResponse<JSONObject> findAll(@Parameter(name = "sdate", description = "검색 시작 데이터 날짜(yyyy-MM-dd)")
                                              @RequestParam(value = "sdate", required = false) String sdate,
                                              @Parameter(name = "edate", description = "검색 시작 데이터 날짜(yyyy-MM-dd)")
                                              @RequestParam(value = "edate", required = false) String edate,
                                              @Parameter(name = "userId", description = "사용자 아이디")
                                              @RequestParam(value = "userId", required = false) String userId,
                                              @Parameter(name = "keyword", description = "검색어")
                                              @RequestParam(value = "keyword", required = false) String keyword,
                                              @Parameter(name = "videoId", description = "비디오 아이디.")
                                              @RequestParam(value = "videoId", required = false) String videoId,
                                              @Parameter(name = "categoryId", description = "카테고리 아이디.")
                                              @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                              @Parameter(name = "isForever", description = "포에버 영상 여부(1 or 0).")
                                              @RequestParam(value = "isForever", required = false) Integer isForever,
                                              @Parameter(name = "page", description = "페이지.")
                                              @RequestParam(value = "page", required = false) Integer page,
                                              @Parameter(name = "limit", description = "한 페이지에 불러올 데이터 수. Default to 10.")
                                              @RequestParam(value = "limit", required = false) Integer limit,
                                              @Parameter(name = "sort", description = "정렬 필드(구현안됨). Example:.")
                                              @RequestParam(value = "sort", required = false) String sort) throws Exception {

        JSONObject content = lboxService.findAll(sdate, edate, userId, keyword, videoId, categoryId, isForever, page, limit, sort);


        return new AnsApiResponse<>(content);
    }

    @Operation(summary = "엘박스 카테고리 조회", description = "엘박스 카테고리 조회")
    @GetMapping(path = "/categories")
    public AnsApiResponse<JSONObject> findCategories(@Parameter(name = "parentId", description = "parentId")
                                                     @RequestParam(value = "parentId", required = false) Integer parentId) throws Exception {

        JSONObject categoriesData = lboxService.findCategories(parentId);


        return new AnsApiResponse<>(categoriesData);
    }

    @Operation(summary = "엘박스 사용자정보 조회", description = "엘박스 사용자정보 조회")
    @GetMapping(path = "/userinfo")
    public AnsApiResponse<JSONObject> findUserInfo() throws Exception {

        JSONObject userInfoData = lboxService.findUserInfo();


        return new AnsApiResponse<>(userInfoData);
    }

    @Operation(summary = "부조 전송", description = "부조 전송")
    @PutMapping(path = "/mediatransfer/{mediaId}")
    public AnsApiResponse<TransportResponseDTO> mediaTransfer(@Parameter(name = "mediaId", required = true, description = "미디어 아이디")
                                                         @PathVariable("mediaId") Long mediaId,
                                                         @Parameter(name = "contentId", description = "콘텐츠 아이디")
                                                         @RequestParam(value = "contentId", required = true) Integer contentId,
                                                         @Parameter(name = "subrmNm", description = "부조명")
                                                         @RequestParam(value = "subrmNm", required = true) String subrmNm,
                                                                /*@Parameter(name = "destinations", description = "전송대상(NS, PS_A, PS_B, PS_C) required.")
                                                                @RequestParam(value = "destinations", required = false) List<String> destinations,*/
                                                         @Parameter(name = "isUrgent", description = "긴급 여부")
                                                         @RequestParam(value = "isUrgent", required = false) Boolean isUrgent,
                                                         @Parameter(name = "isRetry", description = "재전송 여부")
                                                         @RequestParam(value = "isRetry", required = false) Boolean isRetry) throws JsonProcessingException {

        log.info("부조전송 = "+"미디어 아이디 : "+mediaId+" 텐츠 아이디 : "+contentId+
                " 부조명 : "+subrmNm+" 긴급 여부 : "+isUrgent+" 재전송 여부 : "+isRetry);

        String destinations = "T";
        TransportResponseDTO transportResponseDTO = lboxService.mediaTransfer(mediaId, contentId, subrmNm, destinations, isUrgent, isRetry);


        return new AnsApiResponse<>(transportResponseDTO);
    }

    @Operation(summary = "PS 긴급전송", description = "PS 긴급전송")
    @PutMapping(path = "/mediatransfer/{mediaId}/psemergency")
    public AnsApiResponse<TransportResponseDTO> psEmergencyTransfer(@Parameter(name = "mediaId", required = true, description = "미디어 아이디")
                                                               @PathVariable("mediaId") Long mediaId,
                                                               @Parameter(name = "contentId", description = "콘텐츠 아이디")
                                                               @RequestParam(value = "contentId", required = true) Integer contentId,
                                                               @Parameter(name = "subrmNm", description = "부조명")
                                                               @RequestParam(value = "subrmNm", required = true) String subrmNm,
                                                                /*@Parameter(name = "destinations", description = "전송대상(NS, PS_A, PS_B, PS_C) required.")
                                                                @RequestParam(value = "destinations", required = false) List<String> destinations,*/
                                                               @Parameter(name = "isUrgent", description = "긴급 여부")
                                                               @RequestParam(value = "isUrgent", required = false) Boolean isUrgent,
                                                               @Parameter(name = "isRetry", description = "재전송 여부")
                                                               @RequestParam(value = "isRetry", required = false) Boolean isRetry) throws JsonProcessingException {

        log.info("PS 긴급전송 = "+"미디어 아이디 : "+mediaId+" 텐츠 아이디 : "+contentId+
                " 부조명 : "+subrmNm+" 긴급 여부 : "+isUrgent+" 재전송 여부 : "+isRetry);

        String destinations = "P";
        TransportResponseDTO transportResponseDTO = lboxService.mediaTransfer(mediaId, contentId, subrmNm, destinations, isUrgent, isRetry);


        return new AnsApiResponse<>(transportResponseDTO);
    }

    @Operation(summary = "NS 긴급전송", description = "NS 긴급전송")
    @PutMapping(path = "/mediatransfer/{mediaId}/nsemergency")
    public AnsApiResponse<TransportResponseDTO> nsEmergencyTransfer(@Parameter(name = "mediaId", required = true, description = "미디어 아이디")
                                                               @PathVariable("mediaId") Long mediaId,
                                                               @Parameter(name = "contentId", description = "콘텐츠 아이디")
                                                               @RequestParam(value = "contentId", required = true) Integer contentId,
                                                               @Parameter(name = "subrmNm", description = "부조명")
                                                               @RequestParam(value = "subrmNm", required = true) String subrmNm,
                                                                /*@Parameter(name = "destinations", description = "전송대상(NS, PS_A, PS_B, PS_C) required.")
                                                                @RequestParam(value = "destinations", required = false) List<String> destinations,*/
                                                               @Parameter(name = "isUrgent", description = "긴급 여부")
                                                               @RequestParam(value = "isUrgent", required = false) Boolean isUrgent,
                                                               @Parameter(name = "isRetry", description = "재전송 여부")
                                                               @RequestParam(value = "isRetry", required = false) Boolean isRetry) throws JsonProcessingException {

        log.info("NS 긴급전송 = "+"미디어 아이디 : "+mediaId+" 텐츠 아이디 : "+contentId+
                " 부조명 : "+subrmNm+" 긴급 여부 : "+isUrgent+" 재전송 여부 : "+isRetry);

        String destinations = "N";
        TransportResponseDTO transportResponseDTO = lboxService.mediaTransfer(mediaId, contentId, subrmNm, destinations, isUrgent, isRetry);


        return new AnsApiResponse<>(transportResponseDTO);
    }

    //외부연동으로 외부연동 클래스로 이동[InterfaceController]
   /* @Operation(summary = "영상 전송 상태 업데이트", description = "영상 전송 상태 업데이트")
    @PutMapping(path = "/mediatransfer/updatestate")
    public AnsApiResponse<?> stateChange(@Parameter(name = "contentId", description = "콘텐츠 아이디.")
                                         @RequestParam(value = "contentId", required = false) Integer contentId,
                                         @Parameter(name = "videoId", description = "비디오 아이디.")
                                         @RequestParam(value = "videoId", required = false) String videoId,
                                         @Parameter(name = "trnsfFileNm", description = "전송 파일명.")
                                         @RequestParam(value = "trnsfFileNm", required = false) String trnsfFileNm,
                                         @Parameter(name = "mediaTypCd", description = "match_ready:준비됨, match_inprocess : 시작됨,<br> " +
                                                 "match_failed : 실패, match_completed : 완료")
                                         @RequestParam(value = "mediaTypCd", required = false) String mediaTypCd) {


        lboxService.stateChange(contentId, videoId, trnsfFileNm, mediaTypCd);
        return null;
    }*/

    @Operation(summary = "부조 전송 [백드롭]", description = "부조 전송 [백드롭]")
    @PutMapping(path = "/mediatransfer/{mediaId}/backdrop")
    public AnsApiResponse<TransportResponseDTO> psBackDrop(@Parameter(name = "mediaId", required = true, description = "미디어 아이디")
                                                                    @PathVariable("mediaId") Long mediaId,
                                                                    @Parameter(name = "contentId", description = "콘텐츠 아이디")
                                                                    @RequestParam(value = "contentId", required = true) Integer contentId,
                                                                    @Parameter(name = "subrmNm", description = "부조명")
                                                                    @RequestParam(value = "subrmNm", required = true) String subrmNm,
                                                                /*@Parameter(name = "destinations", description = "전송대상(NS, PS_A, PS_B, PS_C) required.")
                                                                @RequestParam(value = "destinations", required = false) List<String> destinations,*/
                                                                    @Parameter(name = "isUrgent", description = "긴급 여부")
                                                                    @RequestParam(value = "isUrgent", required = false) Boolean isUrgent,
                                                                    @Parameter(name = "isRetry", description = "재전송 여부")
                                                                    @RequestParam(value = "isRetry", required = false) Boolean isRetry) throws JsonProcessingException {

        log.info("PS 긴급전송 = "+"미디어 아이디 : "+mediaId+" 텐츠 아이디 : "+contentId+
                " 부조명 : "+subrmNm+" 긴급 여부 : "+isUrgent+" 재전송 여부 : "+isRetry);

        String destinations = "B";
        TransportResponseDTO transportResponseDTO = lboxService.mediaTransfer(mediaId, contentId, subrmNm, destinations, isUrgent, isRetry);


        return new AnsApiResponse<>(transportResponseDTO);
    }


}
