package com.gemiso.zodiac.app.yonhap;

import com.gemiso.zodiac.app.yonhap.dto.*;
import com.gemiso.zodiac.app.yonhapAssign.dto.YonhapAssignSimpleDTO;
import com.gemiso.zodiac.app.yonhapPhoto.dto.YonhapExceptionDomain;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.page.PageResultDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.gemiso.zodiac.core.service.UserAuthService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.List;

@Api(description = "연합 API")
@RestController
@RequestMapping("/yonhap")
@RequiredArgsConstructor
@Slf4j
public class YonhapController {

    private final YonhapService yonhapService;

    private final UserAuthService userAuthService;

    @Operation(summary = "연합 목록조회", description = "연합 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<?> findAll(@Parameter(name = "sdate", description = "검색시작일[yyyy-MM-dd]", required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                   @Parameter(name = "edate", description = "검색종료일[yyyy-MM-dd]", required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                   @Parameter(name = "artclCateCd", description = "분류코드")
                                                   @RequestParam(value = "artclCateCd", required = false) String artclCateCd,
                                                   /*@Parameter(name = "regionCds", description = "통신사코드")
                                                   @RequestParam(value = "regionCds", required = false) List<String> regionCds,*/
                                                   @Parameter(name = "searchWord", description = "검색어")
                                                   @RequestParam(value = "searchWord", required = false) String searchWord,
                                                   @Parameter(name = "svcTyp", description = "서비스 유형 ( 국문 AKRO, 영문 AENO )")
                                                   @RequestParam(value = "svcTyp", required = false) String svcTyp,
                                                   @Parameter(name = "page", description = "시작페이지")
                                                   @RequestParam(value = "page", required = false) Integer page,
                                                   @Parameter(name = "limit", description = "한 페이지에 데이터 수")
                                                   @RequestParam(value = "limit", required = false) Integer limit) throws Exception {

        PageResultDTO<YonhapDTO, Yonhap> pageList = null;

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            //검색날짜 시간설정 (검색시작 Date = yyyy-MM-dd 00:00:00 / 검색종료 Date yyyy-MM-dd 23:59:59)
            SearchDate searchDate = new SearchDate(sdate, edate);

            pageList = yonhapService.findAll(searchDate.getStartDate(), searchDate.getEndDate(),
                    artclCateCd, /*regionCds,*/ searchWord, svcTyp, page, limit);

        } else {
            pageList = yonhapService.findAll(null, null, artclCateCd/*, regionCds*/, searchWord, svcTyp, page, limit);

        }
        return new AnsApiResponse<>(pageList);
    }

    @Operation(summary = "연합 상세조회", description = "연합 상세조회")
    @GetMapping(path = "/{yonhapId}")
    public AnsApiResponse<YonhapDTO> find(@Parameter(name = "yonhapId", description = "연합 아이디") @PathVariable("yonhapId") Long yonhapId) {

        YonhapDTO yonhapDTO = yonhapService.find(yonhapId);

        return new AnsApiResponse<>(yonhapDTO);

    }

    @Operation(summary = "연합 등록", description = "연합 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Parameter(description = "필수값<br> ", required = true) @RequestBody YonhapCreateDTO yonhapCreateDTO,
                                    UriComponentsBuilder ucBuilder
    ) throws Exception {


        YonhapExceptionDomain yonhapExceptionDomain = yonhapService.create(yonhapCreateDTO);

        if (yonhapExceptionDomain.getCode().equals("2000") == false) {
            return new ResponseEntity<YonhapExceptionDomain>(yonhapExceptionDomain, HttpStatus.CREATED);
        }

        YonhapDTO yonhapDTO = yonhapService.find(yonhapExceptionDomain.getId());

        YonhapResponseDTO yonhapResponseDTO = yonhapService.formatYonhap(yonhapDTO);


        return new ResponseEntity<>(yonhapCreateDTO, HttpStatus.CREATED);

    }

    @Operation(summary = "파일 업로드", description = "파일 업로드")
    @PostMapping(path = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<YonhapFileResponseDTO> fileCreate(@RequestPart MultipartFile file, @RequestParam String fileDivCd) {

        YonhapFileResponseDTO DTO = yonhapService.fileCreate(file, fileDivCd);

        return new AnsApiResponse<>(DTO);
    }

    @Operation(summary = "연합 어싸인 등록", description = "연합 어싸인 등록")
    @PostMapping(path = "/assign")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<YonhapAssignSimpleDTO> createAssign(@Parameter(description = "필수값<br> ", required = true)
                                        @RequestBody YonhapAssignCreateDTO yonhapAssignCreateDTO) throws Exception {

        String userId = userAuthService.authUser.getUserId();

        log.info(" Yonhap Assign : UserId - "+userId + " Yonhap Assign articleDTO - "+yonhapAssignCreateDTO.toString());

        YonhapAssignSimpleDTO yonhapAssignSimpleDTO = yonhapService.createAssign(yonhapAssignCreateDTO, userId);

        return new AnsApiResponse<>(yonhapAssignSimpleDTO);
    }
}
