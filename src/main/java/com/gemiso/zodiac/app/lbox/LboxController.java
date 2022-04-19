package com.gemiso.zodiac.app.lbox;

import com.gemiso.zodiac.app.lbox.dto.DataDTO;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Api(description = "엘박스 API")
@RestController
@RequestMapping("/lbox")
@RequiredArgsConstructor
@Slf4j
public class LboxController {

    private final LboxService lboxService;

    @Operation(summary = "엘박스 영상 목록 조회", description = "엘박스 영상 목록 조회")
    @GetMapping(path = "")
    public AnsApiResponse<DataDTO> findAll(@Parameter(name = "sdate", description = "검색 시작 데이터 날짜(yyyy-MM-dd)")
                                     @RequestParam(value = "sdate", required = false) String sdate,
                                     @Parameter(name = "edate", description = "검색 시작 데이터 날짜(yyyy-MM-dd)")
                                     @RequestParam(value = "edate", required = false) String edate) throws Exception {



            DataDTO dataDTO = lboxService.findAll(sdate, edate);



        return new AnsApiResponse<>(dataDTO);
    }
}
