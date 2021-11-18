package com.gemiso.zodiac.app.home;

import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.querydsl.core.types.Order;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "홈화면 API")
@RestController
@RequestMapping("/home")
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

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
    }
}
