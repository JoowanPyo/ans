package com.gemiso.zodiac.app.articleActionLog;

import com.gemiso.zodiac.app.articleActionLog.dto.ArticleActionLogDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.gemiso.zodiac.core.service.JwtGetUserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "기사액션 로그 API")
@RestController
@RequestMapping("/articleactionlog")
@Slf4j
@RequiredArgsConstructor
public class ArticleActionLogController {

    private final ArticleActionLogService articleActionLogService;
    private final JwtGetUserService jwtGetUserService;


    @Operation(summary = "기사액션로그 목록조회", description = "기사액션로그 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<ArticleActionLogDTO>> findAll(@Parameter(name = "artclId", description = "기사 아이디")
                                                             @RequestParam(value = "artclId", required = false) Long artclId,
                                                             @RequestHeader(value = "Authorization", required = false)String Authorization) throws Exception {

        if (ObjectUtils.isEmpty(artclId)) {
            // 토큰 인증된 사용자 아이디를 입력자로 등록
            String userId =jwtGetUserService.getUser(Authorization);
            log.info("Article Action Log FindAll : id - " + artclId+" userId - "+userId);
        }

        //long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기
        List<ArticleActionLogDTO> articleActionLogDTOList = articleActionLogService.findAll(artclId);

        //long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        //long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
        //System.out.println("시간차이(m) : "+secDiffTime);

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
