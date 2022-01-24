package com.gemiso.zodiac.app.yonhapPoto;


import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/yonhappoto")
@Slf4j
@RequiredArgsConstructor
@Api(description = "연합 포토 API")
public class YonhapPotoController {
}
