package com.gemiso.zodiac.app.dailyProgram;


import com.gemiso.zodiac.app.dailyProgram.dto.DailyProgramCreateDTO;
import com.gemiso.zodiac.app.dailyProgram.dto.DailyProgramDTO;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.gemiso.zodiac.core.service.JwtGetUserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(description = "일일편성 API")
@RestController
@RequestMapping("/daily")
@Slf4j
@RequiredArgsConstructor
public class DailyProgramController {

    private final DailyProgramService dailyProgramService;

    private final JwtGetUserService jwtGetUserService;

    @Operation(summary = "일일편성 목록조회", description = "일일편성 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<DailyProgramDTO>> findAll(@Parameter(description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                         @Parameter(description = "검색 종료 날짜(yyyy-MM-dd)", required = false)
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                         @Parameter(name = "brdcPgmId", description = "프로그램 아이디")
                                                         @RequestParam(value = "brdcPgmId", required = false) String brdcPgmId,
                                                         @Parameter(name = "brdcPgmNm", description = "프로그램 명")
                                                         @RequestParam(value = "brdcPgmNm", required = false) String brdcPgmNm,
                                                         /*@Parameter(name = "brdcDt", description = "방송 일자")
                                                         @RequestParam(value = "brdcDt", required = false) String brdcDt,*/
                                                         @Parameter(name = "brdcDivCd", description = "방송 구분 코드")
                                                         @RequestParam(value = "brdcDivCd", required = false) String brdcDivCd,
                                                         @Parameter(name = "stdioId", description = "스튜디오 아이디")
                                                         @RequestParam(value = "stdioId", required = false) Long stdioId,
                                                         @Parameter(name = "subrmId", description = "부조아이디")
                                                         @RequestParam(value = "subrmId", required = false) Long subrmId,
                                                         @Parameter(name = "searchWord", description = "검색키워드[방송프로그램명]")
                                                         @RequestParam(value = "searchWord", required = false) String searchWord) throws Exception {

        List<DailyProgramDTO> dailyProgramDTOList = new ArrayList<>();

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {

            //검색날짜 시간설정 (검색시작 Date = yyyy-MM-dd 00:00:00 / 검색종료 Date yyyy-MM-dd 24:00:00)
            SearchDate searchDate = new SearchDate(sdate, edate);

            dailyProgramDTOList = dailyProgramService.findAll(searchDate.getStartDate(), searchDate.getEndDate(),
                    brdcPgmId, brdcPgmNm, brdcDivCd, stdioId, subrmId, searchWord);
        } else {

            dailyProgramDTOList = dailyProgramService.findAll(null, null, brdcPgmId, brdcPgmNm, brdcDivCd, stdioId, subrmId, searchWord);

        }
        return new AnsApiResponse<>(dailyProgramDTOList);
    }

    @Operation(summary = "일일편성 상세조회", description = "일일편성 상세조회")
    @GetMapping(path = "/{id}")
    public AnsApiResponse<DailyProgramDTO> find(@Parameter(name = "id", required = true, description = "일일편성 아이디")
                                                @PathVariable("id") long id) {

        DailyProgramDTO dailyProgramDTO = dailyProgramService.find(id);

        return new AnsApiResponse<>(dailyProgramDTO);
    }

    @Operation(summary = "일일편성 등록", description = "일일편성 등록")
    @PostMapping(path = "")
    public AnsApiResponse<DailyProgramDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                                  @RequestBody @Valid DailyProgramCreateDTO dailyProgramCreateDTO,
                                                  @RequestHeader(value = "Authorization", required = false)String Authorization) throws Exception {

        String userId =jwtGetUserService.getUser(Authorization);

        Long id = dailyProgramService.create(dailyProgramCreateDTO, userId);

        DailyProgramDTO dailyProgramDTO = dailyProgramService.find(id);

        return new AnsApiResponse<>(dailyProgramDTO);
    }
}
