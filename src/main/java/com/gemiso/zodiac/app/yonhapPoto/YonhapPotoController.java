package com.gemiso.zodiac.app.yonhapPoto;


import com.gemiso.zodiac.app.yonhapAssign.dto.YonhapAssignDTO;
import com.gemiso.zodiac.app.yonhapPoto.dto.YonhapPotoDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/yonhappoto")
@Slf4j
@RequiredArgsConstructor
@Api(description = "연합 포토 API")
public class YonhapPotoController {

    private final YonhapPotoService yonhapPotoService;

/*    @Operation(summary = "연합 포토 목록조회", description = "연합 포토 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<YonhapPotoDTO>> findAll(@Parameter(description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                       @Parameter(description = "검색 종료 날짜(yyyy-MM-dd)", required = false)
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate) {

        List<YonhapPotoDTO> yonhapPotoDTOList = yonhapPotoService.findAll();

        return new AnsApiResponse<>(yonhapPotoDTOList);
    }*/
}
