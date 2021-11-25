package com.gemiso.zodiac.app.breakingNewsDetail;

import com.gemiso.zodiac.app.breakingNewsDetail.dto.BreakingNewsDtlDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "속보뉴스 상세 API")
@RestController
@RequestMapping("/breakingnewsdtl")
@RequiredArgsConstructor
@Slf4j
public class BreakingNewsDtlController {

    private final BreakingNewsDtlService breakingNewsDtlService;


    @Operation(summary = "속보뉴스 상세 목록조회", description = "속보뉴스 상세 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<BreakingNewsDtlDTO>> findAll(@Parameter(name = "breakingNewsId", description = "속보뉴스 아이디")
                                                            @RequestParam(value = "breakingNewsId", required = false) Long breakingNewsId) {

        List<BreakingNewsDtlDTO> breakingNewsDtlDTOList = breakingNewsDtlService.findAll(breakingNewsId);

        return new AnsApiResponse<>(breakingNewsDtlDTOList);
    }

    @Operation(summary = "속보뉴스 상세 상세조회", description = "속보뉴스 상세 상세조회")
    @GetMapping(path = "/{id}")
    public AnsApiResponse<BreakingNewsDtlDTO> find(@Parameter(name = "id", required = true, description = "속보뉴스 상세 아이디",
            in = ParameterIn.PATH) @PathVariable("id") Long id) {

        BreakingNewsDtlDTO breakingNewsDtlDTO = breakingNewsDtlService.find(id);

        return new AnsApiResponse<>(breakingNewsDtlDTO);
    }

    @Operation(summary = "속보뉴스 상세 삭제", description = "속보뉴스 상세 삭제")
    @DeleteMapping(path = "/{id}")
    public AnsApiResponse<?> delete(@Parameter(name = "id", required = true, description = "속보뉴스 상세 아이디",
            in = ParameterIn.PATH) @PathVariable("id") Long id){

        breakingNewsDtlService.delete(id);

        return AnsApiResponse.noContent();
    }
}
