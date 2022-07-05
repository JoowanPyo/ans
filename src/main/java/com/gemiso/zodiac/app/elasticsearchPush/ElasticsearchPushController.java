package com.gemiso.zodiac.app.elasticsearchPush;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.ArticleService;
import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.page.PageResultDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Api(description = "엘라스틱서치 마이그레이션 API")
@RestController
@RequestMapping("/elasticsearchpush")
@Slf4j
@RequiredArgsConstructor
public class ElasticsearchPushController {

    private final ElasticsearchPushService elasticsearchPushService;
    private final ArticleService articleService;

    @Operation(summary = "기사 목록조회", description = "기사 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<?> findAll(
            @Parameter(name = "sdate", description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date sdate,
            @Parameter(name = "edate", description = "검색 종료 날짜(yyyy-MM-dd)", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date edate,
            @Parameter(name = "page", description = "시작페이지") @RequestParam(value = "page", required = false) Integer page,
            @Parameter(name = "limit", description = "한 페이지에 데이터 수") @RequestParam(value = "limit", required = false) Integer limit) throws Exception {


        long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기
        Long count = 0L;

        for (;;) {

            PageResultDTO<ArticleDTO, Article> pageList = articleService.findAllElasticPush(sdate, edate, page, limit);
            //검색조건 날짜형식이 안들어왔을경우

            List<ArticleDTO> articleDTOList = pageList.getDtoList();

            count = count + Optional.ofNullable(articleDTOList.stream().count()).orElse(0L);

            if (CollectionUtils.isEmpty(articleDTOList)){
                break;
            }

            elasticsearchPushService.pushElastic(articleDTOList);

            //페이지값 증가
            ++page;
        }

        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
        System.out.println("시간차이(m) : "+secDiffTime+"    총갯수  : "+ count);
        log.info(" 엘라스틱 서치 API 시간 -----------------" + secDiffTime+"    총갯수  : "+ count);

        return AnsApiResponse.ok();
    }
}
