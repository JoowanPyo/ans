package com.gemiso.zodiac.app.articleCapHist;

import com.gemiso.zodiac.app.articleCapHist.dto.ArticleCapHistDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(description = "기사자막 이력 API")
@RestController
@RequestMapping("/articlecaphist")
@Slf4j
@RequiredArgsConstructor
public class ArticleCapHistController {

    private final ArticleCapHistService articleCapHistService;


    @Operation(summary = "기사자막 이력 목록조회", description = "기사자막 이력 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<ArticleCapHistDTO>> findAll(@Parameter(name = "artclHistId", description = "기사이력 아이디")
                                                           @RequestParam(value = "artclHistId", required = false) Long artclHistId) {

        List<ArticleCapHistDTO> articleCapHistDTOList = new ArrayList<>();

        if (ObjectUtils.isEmpty(artclHistId)){
            return new AnsApiResponse<>(articleCapHistDTOList);
        }

        articleCapHistDTOList = articleCapHistService.findAll(artclHistId);

        return new AnsApiResponse<>(articleCapHistDTOList);
    }

    @Operation(summary = "기사자막 이력 상세조회", description = "기사자막 이력 상세조회")
    @GetMapping(path = "/{ancCapHistId}")
    public AnsApiResponse<ArticleCapHistDTO> find(@Parameter(name = "artclCapHistId", required = true, description = "기사자막 이력 아이디")
                                                  @PathVariable("artclCapHistId") Long artclCapHistId) {

        ArticleCapHistDTO articleCapHistDTO = articleCapHistService.find(artclCapHistId);

        return new AnsApiResponse<>(articleCapHistDTO);
    }
}
