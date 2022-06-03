package com.gemiso.zodiac.app.scrollNews;

import com.gemiso.zodiac.app.issue.dto.IssueCreateDTO;
import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsCreateDTO;
import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsDTO;
import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsSimpleDTO;
import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsUpdateDTO;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(description = "스크롤 뉴스 API")
@RestController
@RequestMapping("/scrollnews")
@RequiredArgsConstructor
@Slf4j
public class ScrollNewsController {

    private final ScrollNewsService scrollNewsService;

    @Operation(summary = "스크롤 뉴스 목록조회", description = "스크롤 뉴스 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<ScrollNewsDTO>> findAll(@Parameter(description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                       @Parameter(description = "검색 종료 날짜(yyyy-MM-dd)", required = false)
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                       @Parameter(name = "delYn", description = "삭제여부 (N , Y)")
                                                       @RequestParam(value = "delYn", required = false) String delYn) throws Exception {

        List<ScrollNewsDTO> scrollNewsDTOList = new ArrayList<>();

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {

            SearchDate searchDate = new SearchDate(sdate, edate);

            scrollNewsDTOList = scrollNewsService.findAll(searchDate.getStartDate(), searchDate.getEndDate(), delYn);
        }
        else {
            scrollNewsDTOList = scrollNewsService.findAll(null, null, delYn);
        }

        return new AnsApiResponse<>(scrollNewsDTOList);
    }

    @Operation(summary = "스크롤 뉴스 상세조회", description = "스크롤 뉴스 상세조회")
    @GetMapping(path = "/{scrlNewsId}")
    public AnsApiResponse<ScrollNewsDTO> find(@Parameter(name = "scrlNewsId", required = true, description = "스크롤 뉴스 아이디", in = ParameterIn.PATH)
                                              @PathVariable("scrlNewsId") Long scrlNewsId) {

        ScrollNewsDTO scrollNewsDTO = scrollNewsService.find(scrlNewsId);

        return new AnsApiResponse<>(scrollNewsDTO);
    }

    @Operation(summary = "스크롤 뉴스 등록", description = "스크롤 뉴스 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<ScrollNewsSimpleDTO> create(@Parameter(description = "필수값<br> titl[제목], fileNm[파일명]" +
            ",trnsfStCd[전송상태]", required = true) @RequestBody @Valid ScrollNewsCreateDTO scrollNewsCreateDTO) throws Exception {

        Long scrlNewsId = scrollNewsService.create(scrollNewsCreateDTO);

        ScrollNewsSimpleDTO scrollNewsSimpleDTO = new ScrollNewsSimpleDTO();
        scrollNewsSimpleDTO.setScrlNewsId(scrlNewsId);

        return new AnsApiResponse<>(scrollNewsSimpleDTO);
    }

    @Operation(summary = "스크롤 뉴스 수정", description = "스크롤 뉴스 수정")
    @PutMapping(path = "/{scrlNewsId}")
    public AnsApiResponse<ScrollNewsSimpleDTO> update(@Parameter(name = "scrlNewsId", required = true, description = "스크롤 뉴스 아이디", in = ParameterIn.PATH)
                                                      @PathVariable("scrlNewsId") Long scrlNewsId,
                                                      @Parameter(description = "필수값<br> titl[제목], fileNm[파일명],trnsfStCd[전송상태]", required = true)
                                                      @RequestBody @Valid ScrollNewsUpdateDTO scrollNewsUpdateDTO) throws Exception {

        scrollNewsService.update(scrollNewsUpdateDTO, scrlNewsId);

        ScrollNewsSimpleDTO scrollNewsSimpleDTO = new ScrollNewsSimpleDTO();
        scrollNewsSimpleDTO.setScrlNewsId(scrlNewsId);

        return new AnsApiResponse<>(scrollNewsSimpleDTO);
    }

    @Operation(summary = "스크롤 뉴스 삭제", description = "스크롤 뉴스 삭제")
    @DeleteMapping(path = "/{scrlNewsId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "scrlNewsId", required = true, description = "스크롤 뉴스 아이디", in = ParameterIn.PATH)
                                    @PathVariable("scrlNewsId") Long scrlNewsId) {

        scrollNewsService.delete(scrlNewsId);

        return AnsApiResponse.noContent();
    }

   /* @Operation(summary = "스크롤 뉴스 상세조회", description = "스크롤 뉴스 상세조회")
    @GetMapping(path = "/{scrlNewsId}")
    public AnsApiResponse<ScrollNewsDTO> findTextFileDownload(@Parameter(name = "scrlNewsId", required = true, description = "스크롤 뉴스 아이디", in = ParameterIn.PATH)
                                              @PathVariable("scrlNewsId") Long scrlNewsId) {

        ScrollNewsDTO scrollNewsDTO = scrollNewsService.find(scrlNewsId);



        return new AnsApiResponse<>(scrollNewsDTO);
    }*/

}
