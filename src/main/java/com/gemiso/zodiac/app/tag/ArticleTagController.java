package com.gemiso.zodiac.app.tag;

import com.gemiso.zodiac.app.tag.dto.ArticleTagDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "기사테그 API")
@RestController
@RequestMapping("/articletags")
@Slf4j
@RequiredArgsConstructor
public class ArticleTagController {

    private final ArticleTagService articleTagService;


    @Operation(summary = "기사 테그 등록", description = "기사 테그 등록")
    @PostMapping(path = "/{artclId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<List<ArticleTagDTO>> create(@Parameter(name = "artclId", required = true, description = "기사 아이디")
                                                   @PathVariable("artclId") long artclId,
                                                      @Parameter(name = "tag", description = "테그")
                                                   @RequestParam(value = "tag", required = false) List<String> tagList) {

        articleTagService.create(artclId, tagList);

        List<ArticleTagDTO> articleTagDTOList = articleTagService.find(artclId);

        return new AnsApiResponse<>(articleTagDTOList);
    }
}
