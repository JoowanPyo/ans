package com.gemiso.zodiac.app.yonhap;

import com.gemiso.zodiac.app.file.dto.StatusCodeFileDTO;
import com.gemiso.zodiac.app.yonhap.dto.YonhapCreateDTO;
import com.gemiso.zodiac.app.yonhap.dto.YonhapDTO;
import com.gemiso.zodiac.app.yonhap.dto.YonhapFileResponseDTO;
import com.gemiso.zodiac.app.yonhap.dto.YonhapResponseDTO;
import com.gemiso.zodiac.app.yonhapPhoto.dto.YonhapExceptionDomain;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(description = "연합 API")
@RestController
@RequestMapping("/yonhap")
@RequiredArgsConstructor
@Slf4j
public class YonhapController {

    private final YonhapService yonhapService;

    @Operation(summary = "연합 목록조회", description = "연합 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<YonhapDTO>> findAll(@Parameter(name = "sdate", description = "검색시작일[yyyy-MM-dd]", required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                   @Parameter(name = "edate", description = "검색종료일[yyyy-MM-dd]", required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                   @Parameter(name = "artclCateCds", description = "분류코드")
                                                   @RequestParam(value = "artclCateCds", required = false) List<String> artclCateCds,
                                                   @Parameter(name = "regionCds", description = "통신사코드")
                                                   @RequestParam(value = "regionCds", required = false) List<String> regionCds,
                                                   @Parameter(name = "searchWord", description = "검색어")
                                                   @RequestParam(value = "searchWord", required = false) String searchWord,
                                                   @Parameter(name = "svcTyp", description = "서비스 유형 ( 국문 AKRO, 영문 AENO )")
                                                   @RequestParam(value = "svcTyp", required = false) String svcTyp) throws Exception {

        List<YonhapDTO> yonhapDTOList = new ArrayList<>();

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            //검색날짜 시간설정 (검색시작 Date = yyyy-MM-dd 00:00:00 / 검색종료 Date yyyy-MM-dd 23:59:59)
            SearchDate searchDate = new SearchDate(sdate, edate);

            yonhapDTOList = yonhapService.findAll(searchDate.getStartDate(), searchDate.getEndDate(), artclCateCds, regionCds, searchWord, svcTyp);

        } else {
            yonhapDTOList = yonhapService.findAll(null, null, artclCateCds, regionCds, searchWord, svcTyp);

        }
        return new AnsApiResponse<>(yonhapDTOList);
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


        return new ResponseEntity<>(yonhapResponseDTO, HttpStatus.CREATED);

    }

    @Operation(summary = "파일 업로드", description = "파일 업로드")
    @PostMapping(path = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<YonhapFileResponseDTO> fileCreate(@RequestPart MultipartFile file, @RequestParam String fileDivCd) {

        YonhapFileResponseDTO DTO = yonhapService.fileCreate(file, fileDivCd);

        return new AnsApiResponse<>(DTO);
    }
}
