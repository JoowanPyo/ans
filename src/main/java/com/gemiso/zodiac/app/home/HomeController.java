package com.gemiso.zodiac.app.home;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.ArticleService;
import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import com.gemiso.zodiac.app.articleOrder.ArticleOrderService;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderDTO;
import com.gemiso.zodiac.core.page.PageResultDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public AnsApiResponse<?> findMyArticle(@Parameter(name = "rptrId", description = "기자 아이디")
                                           @RequestParam(value = "rptrId", required = false) String rptrId,
                                           @Parameter(name = "inputrId", description = "등록자 아이디")
                                           @RequestParam(value = "inputrId", required = false) String inputrId) {

        PageResultDTO<ArticleDTO, Article> pageList = articleService.findAll(null, null, null, rptrId, inputrId,
                null, null, null, null, null, null, null,
                null, null, null, null);

        return new AnsApiResponse<>(pageList);
    }

    @Operation(summary = "홈화면 내기사 목록", description = "홈화면 내기사 목록")
    @GetMapping(path = "/myorder")
    public AnsApiResponse<List<ArticleOrderDTO>> findMyOrder(@Parameter(name = "workrId", description = "작업자 아이디")
                                                             @RequestParam(value = "workrId", required = false) String workrId,
                                                             @Parameter(name = "inputrId", description = "등록자 아이디")
                                                             @RequestParam(value = "inputrId", required = false) String inputrId) {

        List<ArticleOrderDTO> articleOrderDTOList = articleOrderService.findAll(null, null,
                null, null, workrId, inputrId, null);

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
