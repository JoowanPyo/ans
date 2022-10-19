package com.gemiso.zodiac.app.stats;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "기사 통계 API")
@RestController
@RequestMapping("/stats")
@Slf4j
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;
}
