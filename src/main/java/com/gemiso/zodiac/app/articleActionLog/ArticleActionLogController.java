package com.gemiso.zodiac.app.articleActionLog;

import com.gemiso.zodiac.app.articleActionLog.dto.ArticleActionLogDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "기사액션 로그 API")
@RestController
@RequestMapping("/articleactionlog")
@Slf4j
@RequiredArgsConstructor
public class ArticleActionLogController {

    private final ArticleActionLogService articleActionLogService;


    @Operation(summary = "기사액션로그 목록조회", description = "기사액션로그 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<ArticleActionLogDTO>> findAll(@Parameter(name = "artclId", description = "기사 아이디")
                                                             @RequestParam(value = "artclId", required = false) Long artclId) {

        List<ArticleActionLogDTO> articleActionLogDTOList = articleActionLogService.findAll(artclId);

        return new AnsApiResponse<>(articleActionLogDTOList);
    }

    @Operation(summary = "기사액션로그 상세조회", description = "기사액션로그 상세조회")
    @GetMapping(path = "/{id}")
    public AnsApiResponse<ArticleActionLogDTO> find(@Parameter(name = "id", description = "기사액션로그 아이디")
                                                    @PathVariable("id") Long id) {

        ArticleActionLogDTO articleActionLogDTO = articleActionLogService.find(id);

        return new AnsApiResponse<>(articleActionLogDTO);
    }
}
