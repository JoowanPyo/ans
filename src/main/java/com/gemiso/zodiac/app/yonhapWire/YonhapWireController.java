package com.gemiso.zodiac.app.yonhapWire;

import com.gemiso.zodiac.app.yonhapPhoto.dto.YonhapExceptionDomain;
import com.gemiso.zodiac.app.yonhapWire.dto.*;
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
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/yonhapinternational")
@Slf4j
@RequiredArgsConstructor
//@Tag(name = "MisUser Controllers", description = "MisUser API")
@Api(description = "연합 외신 API")
public class YonhapWireController {

    private final YonhapWireService yonhapWireService;

    @Operation(summary = "연합외신 목록조회", description = "연합외신 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<YonhapWireDTO>> findAll(@Parameter(name = "sdate", description = "검색시작일[yyyy-MM-dd]", required = false)
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                       @Parameter(name = "edate", description = "검색종료일[yyyy-MM-dd]", required = false)
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                       @Parameter(name = "agcyCd", description = "통신사코드")
                                                       @RequestParam(value = "agcyCd", required = false) String agcyCd,
                                                       @Parameter(name = "searchWord", description = "검색어")
                                                       @RequestParam(value = "searchWord", required = false) String searchWord,
                                                       @Parameter(name = "imprt", description = "중요도 List<String>", required = false)
                                                       @RequestParam(value = "imprt", required = false) List<String> imprtList) throws Exception {

        List<YonhapWireDTO> yonhapWireDTOList = new ArrayList<>();

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            //검색날짜 시간설정 (검색시작 Date = yyyy-MM-dd 00:00:00 / 검색종료 Date yyyy-MM-dd 23:59:59)
            SearchDate searchDate = new SearchDate(sdate, edate);

            yonhapWireDTOList = yonhapWireService.findAll(searchDate.getStartDate(), searchDate.getEndDate(), agcyCd, searchWord, imprtList);

        } else {

            yonhapWireDTOList = yonhapWireService.findAll(null, null, agcyCd, searchWord, imprtList);
        }
        return new AnsApiResponse<>(yonhapWireDTOList);
    }

    @Operation(summary = "연합외신 상세조회", description = "연합외신 상세조회")
    @GetMapping(path = "/{wireId}")
    public AnsApiResponse<YonhapWireDTO> find(@Parameter(name = "wireId", description = "연합외신 아이디") @PathVariable("wireId") Long wireId) {

        YonhapWireDTO yonhapWireDTO = yonhapWireService.find(wireId);

        return new AnsApiResponse<>(yonhapWireDTO);
    }

    @Operation(summary = "연합외신 등록", description = "연합외신 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Parameter(description = "필수값<br> ", required = true)
                                    @RequestBody YonhapWireCreateDTO yonhapWireCreateDTO, UriComponentsBuilder ucBuilder
    ) throws Exception {
        HttpHeaders headers = null;

        YonhapWireResponseDTO yonhapWireResponseDTO = new YonhapWireResponseDTO();

        /*try {*/
        Long wireId = yonhapWireService.create(yonhapWireCreateDTO);

        if (ObjectUtils.isEmpty(wireId) == false) {

            headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/yonhapInternational/{yh_artcl_id}").buildAndExpand(wireId).toUri());
            YonhapWireDTO yonhapWireDTO = yonhapWireService.find(wireId);
            yonhapWireResponseDTO = yonhapWireService.formatWire(yonhapWireDTO);
        }

        return new ResponseEntity<YonhapWireResponseDTO>(yonhapWireResponseDTO, headers, HttpStatus.CREATED);
    }

    @Operation(summary = "연합외신 등록 APTN", description = "연합외신 등록 APTN")
    @PostMapping(path = "/aptn")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createAptn(@Parameter(description = "필수값<br> ", required = true)
                                        @RequestBody YonhapAptnCreateDTO yonhapAptnCreateDTO, UriComponentsBuilder ucBuilder) throws Exception {


        YonhapExceptionDomain yonhapExceptionDomain = yonhapWireService.createAptn(yonhapAptnCreateDTO);

        if (yonhapExceptionDomain.getCode().equals("2000") == false) {
            return new ResponseEntity<YonhapExceptionDomain>(yonhapExceptionDomain, HttpStatus.CREATED);
        }

        YonhapWireDTO yonhapWireDTO = yonhapWireService.find(yonhapExceptionDomain.getId());
        //YonhapAptnDTO yonhapAptnDTO = yonhapWireService.formatAptn(yonhapWireDTO);

        return new ResponseEntity<>(yonhapWireDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "연합외신 등록 REUTER", description = "연합외신 등록 REUTER")
    @PostMapping(path = "/reuter")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createReuter(@Parameter(description = "필수값<br> ", required = true)
                                          @RequestBody YonhapReuterCreateDTO yonhapReuterCreateDTO) {

        YonhapExceptionDomain yonhapExceptionDomain = yonhapWireService.createReuter(yonhapReuterCreateDTO);

        YonhapWireDTO yonhapWireDTO = yonhapWireService.find(yonhapExceptionDomain.getId());
        YonhapReuterDTO yonhapReuterDTO = yonhapWireService.formatReuter(yonhapWireDTO);


        return new ResponseEntity<>(yonhapReuterDTO, HttpStatus.CREATED);
    }

}
