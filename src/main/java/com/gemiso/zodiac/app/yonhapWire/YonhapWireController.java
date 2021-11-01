package com.gemiso.zodiac.app.yonhapWire;

import com.gemiso.zodiac.app.yonhapWire.dto.YonhapWireCreateDTO;
import com.gemiso.zodiac.app.yonhapWire.dto.YonhapWireDTO;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.response.ApiResponse;
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
@RequestMapping("/yonhapInternational")
@Slf4j
@RequiredArgsConstructor
//@Tag(name = "User Controllers", description = "User API")
@Api(description = "연합 외신 API")
public class YonhapWireController {

    private final YonhapWireService yonhapWireService;

    @Operation(summary = "연합외신 목록조회", description = "연합외신 목록조회")
    @GetMapping(path = "")
    public ApiResponse<List<YonhapWireDTO>> findAll(@Parameter(name = "sdate", description = "검색시작일[yyyy-MM-dd]", required = false)
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

        }else {

            yonhapWireDTOList = yonhapWireService.findAll(null, null, agcyCd, searchWord, imprtList);
        }
        return new ApiResponse<>(yonhapWireDTOList);
    }

    @Operation(summary = "연합외신 등록", description = "연합외신 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Parameter(description = "필수값<br> ", required = true)
                                    @RequestBody YonhapWireCreateDTO yonhapWireCreateDTO, UriComponentsBuilder ucBuilder
    ) {
         HttpHeaders headers = null;

        YonhapWireDTO yonhapWireDTO = new YonhapWireDTO();

        try {
            Long yh_artcl_id = yonhapWireService.create(yonhapWireCreateDTO);

            if (ObjectUtils.isEmpty(yh_artcl_id) == false) {

                headers = new HttpHeaders();
                headers.setLocation(ucBuilder.path("/yonhapInternational/{yh_artcl_id}").buildAndExpand(yh_artcl_id).toUri());
                yonhapWireDTO = yonhapWireService.find(yh_artcl_id);
            }


        } catch (Exception e) {
            e.printStackTrace();
            log.error("yonhap : " + e.getMessage());
            return new ResponseEntity<YonhapWireDTO>(yonhapWireDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<YonhapWireDTO>(yonhapWireDTO, headers, HttpStatus.CREATED);
    }

}
