package com.gemiso.zodiac.app.stats;

import com.gemiso.zodiac.app.stats.dto.StatsCreateDTO;
import com.gemiso.zodiac.app.stats.dto.StatsDTO;
import com.gemiso.zodiac.app.stats.dto.StatsListDTO;
import com.gemiso.zodiac.app.stats.dto.StatsTotalDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Api(description = "기사 통계 API")
@RestController
@RequestMapping("/stats")
@Slf4j
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;


    @Operation(summary = "기사통계 목록조회", description = "기사통계 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<StatsListDTO> findAll(@Parameter(name = "sdate", description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                @Parameter(name = "edate", description = "검색 종료 날짜(yyyy-MM-dd)", required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate) throws Exception {

        List<StatsDTO> statsList = statsService.findAll(sdate, edate);

        //조회된 기사통계 리스트들의 카테고리 카운터를 계산한다
        StatsListDTO statsTotalDTO = statsService.totalStats(statsList);


        return new AnsApiResponse<>(statsTotalDTO);

    }

    @Operation(summary = "기사통계 등록", description = "기사통계 등록")
    @PostMapping(name = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<?> create(@Parameter(description = "필수값<br>, ", required = true)
                                    @RequestBody StatsCreateDTO statsCreateDTO,
                                    @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        statsService.create(statsCreateDTO);

        return AnsApiResponse.ok();
    }

    @Operation(summary = "기사통계 엑셀 다운로드", description = "기사통계 엑셀 다운로드")
    @GetMapping(path = "/excel")
    public void findAllStatisticsCount(HttpServletResponse response,
                                                    @Parameter(name = "sdate", description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = true)
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                    @Parameter(name = "edate", description = "검색 종료 날짜(yyyy-MM-dd)", required = true)
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate) throws Exception {

        List<StatsDTO> statsList = statsService.findAll(sdate, edate);

        //조회된 기사통계 리스트들의 카테고리 카운터를 계산한다
        //StatsListDTO statsTotalDTO = statsService.totalStats(statsList);

        statsService.excelDownload(response, statsList);

    }
}
