package com.gemiso.zodiac.app.yonhapPhoto;


import com.gemiso.zodiac.app.yonhapPhoto.dto.YonhapExceptionDomain;
import com.gemiso.zodiac.app.yonhapPhoto.dto.YonhapPhotoCreateDTO;
import com.gemiso.zodiac.app.yonhapPhoto.dto.YonhapPhotoDTO;
import com.gemiso.zodiac.app.yonhapPhoto.dto.YonhapPhotoDomainDTO;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/yonhapphoto")
@Slf4j
@RequiredArgsConstructor
@Api(description = "연합 포토 API")
public class YonhapPhotoController {

    private final YonhapPhotoService yonhapPhotoService;

    @Operation(summary = "연합 포토 목록조회", description = "연합 포토 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<YonhapPhotoDTO>> findAll(@Parameter(description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
                                                             @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                        @Parameter(description = "검색 종료 날짜(yyyy-MM-dd)", required = false)
                                                             @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                        @Parameter(name = "searchWord", description = "통신사코드")
                                                             @RequestParam(value = "searchWord", required = false) String searchWord) throws Exception {

        List<YonhapPhotoDTO> yonhapPhotoDTOList = new ArrayList<>();

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            //검색날짜 시간설정 (검색시작 Date = yyyy-MM-dd 00:00:00 / 검색종료 Date yyyy-MM-dd 23:59:59)
            SearchDate searchDate = new SearchDate(sdate, edate);

            yonhapPhotoDTOList = yonhapPhotoService.findAll(searchDate.getStartDate(), searchDate.getEndDate(), searchWord);
        } else {
            yonhapPhotoDTOList = yonhapPhotoService.findAll(null, null, searchWord);
        }

        return new AnsApiResponse<>(yonhapPhotoDTOList);
    }

    @Operation(summary = "연합 포토 등록", description = "연합 포토 등록")
    @PostMapping(path = "/extend")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Parameter(description = "필수값<br> ", required = true) @RequestBody YonhapPhotoCreateDTO yonhapPhotoCreateDTO) throws Exception {

        YonhapExceptionDomain yonhapExceptionDomain = yonhapPhotoService.create(yonhapPhotoCreateDTO);

        if (yonhapExceptionDomain.getCode().equals("2000") == false) {
            return new ResponseEntity<YonhapExceptionDomain>(yonhapExceptionDomain, HttpStatus.CREATED);
        }

        YonhapPhotoDTO YonhapPhotoDTO = yonhapPhotoService.find(yonhapExceptionDomain.getId());

        YonhapPhotoDomainDTO yonhapPhotoDomainDTO = yonhapPhotoService.formatYonhapPhoto(YonhapPhotoDTO);

        return new ResponseEntity<>(yonhapPhotoDomainDTO, HttpStatus.CREATED);
    }
}
