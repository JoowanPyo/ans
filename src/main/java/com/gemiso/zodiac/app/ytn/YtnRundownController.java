package com.gemiso.zodiac.app.ytn;

import com.gemiso.zodiac.app.ytn.dto.YonhapYtnExceptionDomain;
import com.gemiso.zodiac.app.ytn.dto.YtnRundownCreateDTO;
import com.gemiso.zodiac.app.ytn.dto.YtnRundownDTO;
import com.gemiso.zodiac.app.ytn.dto.YtnRundownResponseDTO;
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
@RequestMapping("/ytnrundown")
@Slf4j
@RequiredArgsConstructor
@Api(description = "YTN 런다운 API")
public class YtnRundownController {

    private final YtnRundownService ytnRundownService;


    @Operation(summary = "YTN 런다운 목록조회", description = "YTN 런다운 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<YtnRundownDTO>> findAll(@Parameter(name = "sdate", description = "검색시작일[yyyy-MM-dd]", required = false)
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                       @Parameter(name = "edate", description = "검색종료일[yyyy-MM-dd]", required = false)
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                       @Parameter(name = "contId", description = "콘텐츠 아이디")
                                                       @RequestParam(value = "contId", required = false) String contId,
                                                       @Parameter(name = "reporterId", description = "리포터 아이디")
                                                       @RequestParam(value = "reporterId", required = false) String reporterId) throws Exception {

        List<YtnRundownDTO> ytnRundownDTOList = new ArrayList<>();

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            //검색날짜 시간설정 (검색시작 Date = yyyy-MM-dd 00:00:00 / 검색종료 Date yyyy-MM-dd 23:59:59)
            SearchDate searchDate = new SearchDate(sdate, edate);
            ytnRundownDTOList = ytnRundownService.findAll(searchDate.getStartDate(), searchDate.getEndDate(), contId, reporterId);

        }else {
            ytnRundownDTOList = ytnRundownService.findAll(null, null, contId, reporterId);
        }


        return new AnsApiResponse<>(ytnRundownDTOList);

    }

    @Operation(summary = "YTN 런다운 등록", description = "YTN 런다운 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Parameter(description = "필수값<br> ", required = true)
                                    @RequestBody YtnRundownCreateDTO ytnRundownCreateDTO) {

        YtnRundownResponseDTO ytnRundownResponseDTO = new YtnRundownResponseDTO();

        YonhapYtnExceptionDomain yonhapYtnExceptionDomain = ytnRundownService.create(ytnRundownCreateDTO);

        if (yonhapYtnExceptionDomain.getCode().equals("2000") == false) {

            return new ResponseEntity<>(ytnRundownResponseDTO, HttpStatus.CREATED);

        }

        List<YtnRundownDTO> ytnRundownDTOList = ytnRundownService.findAll(null, null, yonhapYtnExceptionDomain.getCont_id(), null);

        ytnRundownResponseDTO = ytnRundownService.formatYtn(ytnRundownDTOList);

        return new ResponseEntity<>(ytnRundownResponseDTO, HttpStatus.CREATED);
    }
}
