package com.gemiso.zodiac.app.articleCap;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "기사자막 API")
@RestController
@RequestMapping("/articlecap")
@RequiredArgsConstructor
@Slf4j
public class ArticleCapController {

    private final ArticleCapService articleCapService;
}
