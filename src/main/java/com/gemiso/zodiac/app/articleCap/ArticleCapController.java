package com.gemiso.zodiac.app.articleCap;

import com.gemiso.zodiac.app.articleCap.dto.ArticleCapCreateDTO;
import com.gemiso.zodiac.app.articleCap.dto.ArticleCapDTO;
import com.gemiso.zodiac.app.articleCap.dto.ArticleCapUpdateDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "기사자막 API")
@RestController
@RequestMapping("/articlecaps")
@RequiredArgsConstructor
@Slf4j
public class ArticleCapController {

    private final ArticleCapService articleCapService;


    @Operation(summary = "기사자막 목록조회", description = "기사자막 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<ArticleCapDTO>> findAll(@Parameter(name = "artclId", description = "기사 아이디")
                                                    @RequestParam(value = "artclId", required = false) Long artclId) {

        List<ArticleCapDTO> articleCapDTOList = articleCapService.findAll(artclId);

        return new AnsApiResponse<>(articleCapDTOList);
    }

    @Operation(summary = "기사자막 상세조회", description = "기사자막 상세조회")
    @GetMapping(path = "/{articleCapId}")
    public AnsApiResponse<ArticleCapDTO> find(@Parameter(name = "articleCapId", required = true, description = "기사자막 아이디")
                                           @PathVariable("articleCapId") Long articleCapId) {

        ArticleCapDTO articleCapDTO = articleCapService.find(articleCapId);

        return new AnsApiResponse<>(articleCapDTO);
    }

    @Operation(summary = "기사자막 등록", description = "기사자막 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<ArticleCapDTO> create(@Parameter(description = "필수값<br>, ", required = true)
                                             @RequestBody ArticleCapCreateDTO articleCapCreateDTO) {

        Long articleCapId = articleCapService.create(articleCapCreateDTO);

        //기사자막 등록 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        ArticleCapDTO articleCapDTO = new ArticleCapDTO();
        articleCapDTO.setArtclCapId(articleCapId);

        return new AnsApiResponse<>(articleCapDTO);
    }

    @Operation(summary = "기사자막 수정", description = "기사자막 수정")
    @PutMapping(path = "/{articleCapId}")
    public AnsApiResponse<ArticleCapDTO> update(@Parameter(description = "필수값<br>, ", required = true)
                                             @RequestBody ArticleCapUpdateDTO articleCapUpdateDTO,
                                                @Parameter(name = "articleCapId", required = true, description = "기사자막 아이디")
                                             @PathVariable("articleCapId") Long articleCapId) {

        articleCapService.update(articleCapUpdateDTO, articleCapId);

        //기사자막 수정 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        ArticleCapDTO articleCapDTO = new ArticleCapDTO();
        articleCapDTO.setArtclCapId(articleCapId);

        return new AnsApiResponse<>(articleCapDTO);

    }

    @Operation(summary = "기사자막 삭제", description = "기사자막 삭제")
    @DeleteMapping(path = "/{articleCapId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "articleCapId", required = true, description = "기사자막 아이디")
                                     @PathVariable("articleCapId") long articleCapId){

        articleCapService.delete(articleCapId);

        return AnsApiResponse.noContent();
    }
}
