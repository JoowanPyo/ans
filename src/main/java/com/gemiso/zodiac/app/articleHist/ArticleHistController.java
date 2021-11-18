package com.gemiso.zodiac.app.articleHist;

import com.gemiso.zodiac.app.articleHist.dto.ArticleHistDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "기사이력 API")
@RestController
@RequestMapping("/articlehist")
@RequiredArgsConstructor
@Slf4j
public class ArticleHistController {

    private final ArticleHistService articleHistService;


    @Operation(summary = "기사이력 목록조회", description = "기사이력 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<ArticleHistDTO>> findAll(@Parameter(name = "artclId", description = "기사 아이디")
                                                        @RequestParam(value = "artclId", required = false) Long artclId,
                                                        @Parameter(name = "orgArtclId", description = "원본 기사 아이디")
                                                        @RequestParam(value = "orgArtclId", required = false) Long orgArtclId,
                                                        @Parameter(name = "searchWord", description = "검색어")
                                                        @RequestParam(value = "searchWord", required = false) String searchWord) {

        List<ArticleHistDTO> articleHistDTOList = articleHistService.findAll(artclId, orgArtclId, searchWord);

        return new AnsApiResponse<>(articleHistDTOList);
    }

    @Operation(summary = "기사이력 상세조회", description = "기사이력 상세조회")
    @GetMapping("/{artclHistId}")
    public AnsApiResponse<ArticleHistDTO> find(@Parameter(name = "artclHistId", required = true, description = "기사이력 아이디")
                                               @PathVariable("artclHistId") Long artclHistId) {

        ArticleHistDTO articleHistDTO = articleHistService.find(artclHistId);

        return new AnsApiResponse<>(articleHistDTO);
    }

}
