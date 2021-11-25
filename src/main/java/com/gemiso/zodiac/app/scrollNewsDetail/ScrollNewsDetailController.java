package com.gemiso.zodiac.app.scrollNewsDetail;

import com.gemiso.zodiac.app.scrollNews.ScrollNewsService;
import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsDTO;
import com.gemiso.zodiac.app.scrollNewsDetail.dto.ScrollNewsDetailDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "스크롤 뉴스 상세 API")
@RestController
@RequestMapping("/scrollnewsdetail")
@RequiredArgsConstructor
@Slf4j
public class ScrollNewsDetailController {

    private final ScrollNewsDetailService scrollNewsDetailService;


    @Operation(summary = "스크롤 뉴스 상세 목록조회", description = "스크롤 뉴스 상세 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<ScrollNewsDetailDTO>> findAll(@Parameter(name = "scrlNewsId", description = "스크롤 뉴스 아이디")
                                                             @RequestParam(value = "scrlNewsId", required = false) Long scrlNewsId) {

        List<ScrollNewsDetailDTO> scrollNewsDetailList = scrollNewsDetailService.findAll(scrlNewsId);

        return new AnsApiResponse<>(scrollNewsDetailList);
    }

    @Operation(summary = "스크롤 뉴스 상세 상세조회", description = "스크롤 뉴스 상세 상세조회")
    @GetMapping(path = "/{id}")
    public AnsApiResponse<ScrollNewsDetailDTO> find(@Parameter(name = "id", required = true, description = "스크롤 뉴스 상세 아이디",
            in = ParameterIn.PATH) @PathVariable("id") Long id) {

        ScrollNewsDetailDTO scrollNewsDetailDTO = scrollNewsDetailService.find(id);

        return new AnsApiResponse<>(scrollNewsDetailDTO);
    }

    @Operation(summary = "스크롤 뉴스 상세 삭제", description = "스크롤 뉴스 상세 삭제")
    @DeleteMapping(path = "/{id}")
    public AnsApiResponse<?> delete(@Parameter(name = "id", required = true, description = "스크롤 뉴스 상세 아이디",
            in = ParameterIn.PATH) @PathVariable("id") Long id) {

        scrollNewsDetailService.delete(id);

        return AnsApiResponse.noContent();
    }
}
