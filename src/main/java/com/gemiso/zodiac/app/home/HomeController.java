package com.gemiso.zodiac.app.home;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.ArticleService;
import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import com.gemiso.zodiac.app.articleOrder.ArticleOrderService;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderDTO;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.page.PageResultDTO;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(description = "홈화면 API")
@RestController
@RequestMapping("/home")
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final ArticleService articleService;
    private final ArticleOrderService articleOrderService;

    //작성자 기준?? 기자아이디(rptr_id)기준?
    @Operation(summary = "홈화면 내기사 목록", description = "홈화면 내기사 목록")
    @GetMapping(path = "/myarticle")
    public AnsApiResponse<?> findMyArticle(@Parameter(name = "sdate", description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
                                           @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                           @Parameter(name = "edate", description = "검색 종료 날짜(yyyy-MM-dd)", required = false)
                                           @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate, @Parameter(name = "rptrId", description = "기자 아이디")
                                           @RequestParam(value = "rptrId", required = false) String rptrId,
                                           @Parameter(name = "inputrId", description = "등록자 아이디")
                                           @RequestParam(value = "inputrId", required = false) String inputrId) throws Exception {

        PageResultDTO<ArticleDTO, Article> pageList = null;

        //검색조건 날짜형식이 들어왔을경우
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {

            SearchDate searchDate = new SearchDate(sdate, edate);
            pageList = articleService.findAll(searchDate.getStartDate(), searchDate.getEndDate(), null, rptrId, inputrId,
                    null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null);
        } else {

            pageList = articleService.findAll(null, null, null, rptrId, inputrId,
                    null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null);

        }

        return new AnsApiResponse<>(pageList);
    }

    @Operation(summary = "홈화면 내기사 목록", description = "홈화면 내기사 목록")
    @GetMapping(path = "/myorder")
    public AnsApiResponse<List<ArticleOrderDTO>> findMyOrder(@Parameter(name = "sdate", description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
                                                             @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                             @Parameter(name = "edate", description = "검색 종료 날짜(yyyy-MM-dd)", required = false)
                                                             @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                             @Parameter(name = "workrId", description = "작업자 아이디")
                                                             @RequestParam(value = "workrId", required = false) String workrId,
                                                             @Parameter(name = "inputrId", description = "등록자 아이디")
                                                             @RequestParam(value = "inputrId", required = false) String inputrId,
                                                             @Parameter(name = "orgArtclId", description = "원본기사 아이디")
                                                             @RequestParam(value = "orgArtclId", required = false) Long orgArtclId) throws Exception {

        List<ArticleOrderDTO> articleOrderDTOList = new ArrayList<>();

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {

            //날짜 파싱 startDate yyyy-MM-dd 00:00:00 , endDate yyyy-MM-dd 24:00:00
            SearchDate searchDate = new SearchDate(sdate, edate);

            articleOrderDTOList = articleOrderService.findAll(searchDate.getStartDate(), searchDate.getEndDate(),
                    null, null, workrId, inputrId, null, orgArtclId, null);
        } else {

            articleOrderDTOList = articleOrderService.findAll(null, null,
                    null, null, workrId, inputrId, null, orgArtclId, null);
        }

        return new AnsApiResponse<>(articleOrderDTOList);

    }

    /*private final HomeService homeService;

    //작성자 기준?? 기자아이디(rptr_id)기준?
    @Operation(summary = "홈화면 내기사 목록", description = "홈화면 내기사 목록")
    @GetMapping(path = "/myarticle")
    public AnsApiResponse<List<ArticleDTO>> findMyArticle(){

        List<ArticleDTO> articleDTOList = homeService.findAll();

        return new AnsApiResponse<>(articleDTOList);
    }

    @Operation(summary = "홈화면 내기사 목록", description = "홈화면 내기사 목록")
    @GetMapping(path = "/myorder")
    public AnsApiResponse<List<Order>> findMyOrder(){

        return null;
    }*/
}
