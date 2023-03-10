package com.gemiso.zodiac.app.breakingNews;

import com.gemiso.zodiac.app.breakingNews.dto.BreakingNewsCreateDTO;
import com.gemiso.zodiac.app.breakingNews.dto.BreakingNewsDTO;
import com.gemiso.zodiac.app.breakingNews.dto.BreakingNewsSimplerDTO;
import com.gemiso.zodiac.app.breakingNews.dto.BreakingNewsUpdateDTO;
import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsDTO;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.gemiso.zodiac.core.service.JwtGetUserService;
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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(description = "속보뉴스 API")
@RestController
@RequestMapping("/breakingnews")
@RequiredArgsConstructor
@Slf4j
public class BreakingNewsController {

    private final BreakingNewsService breakingNewsService;

    private final JwtGetUserService jwtGetUserService;

    @Operation(summary = "속보뉴스 목록조회", description = "속보뉴스 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<BreakingNewsDTO>> findAll(@Parameter(description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                         @Parameter(description = "검색 종료 날짜(yyyy-MM-dd)", required = false)
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                         @Parameter(name = "delYn", description = "삭제여부 (N , Y)")
                                                         @RequestParam(value = "delYn", required = false) String delYn) throws Exception {

        List<BreakingNewsDTO> breakingNewsDTOList = new ArrayList<>();

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {

            SearchDate searchDate = new SearchDate(sdate, edate);

            breakingNewsDTOList = breakingNewsService.findAll(searchDate.getStartDate(), searchDate.getEndDate(), delYn);
        } else {
            breakingNewsDTOList = breakingNewsService.findAll(null, null, delYn);
        }

        return new AnsApiResponse<>(breakingNewsDTOList);
    }

    @Operation(summary = "속보뉴스 상세조회", description = "속보뉴스 상세조회")
    @GetMapping(path = "/{breakingNewsId}")
    public AnsApiResponse<BreakingNewsDTO> find(@Parameter(name = "breakingNewsId", required = true, description = "속보뉴스 아이디")
                                                @PathVariable("breakingNewsId") Long breakingNewsId) {

        BreakingNewsDTO breakingNewsDTO = breakingNewsService.find(breakingNewsId);

        return new AnsApiResponse<>(breakingNewsDTO);
    }

    @Operation(summary = "속보뉴스 등록", description = "속보뉴스 등록")
    @PostMapping(name = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<BreakingNewsSimplerDTO> create(@Parameter(description = "필수값<br>방송일자[brdcDtm],제목[titl]," +
            "속보구분[breakingNewsDiv],라인형식[lnTypCd],전송상태[trnsfStCd] ", required = true)
                                                         @RequestBody @Valid BreakingNewsCreateDTO breakingNewsCreateDTO,
                                                         @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        String userId = jwtGetUserService.getUser(Authorization);

        Long breakingNewsId = breakingNewsService.create(breakingNewsCreateDTO, userId);

        //속보뉴스 등록 후 만들어진 아이디 set
        BreakingNewsSimplerDTO breakingNewsSimplerDTO = new BreakingNewsSimplerDTO();
        breakingNewsSimplerDTO.setBreakingNewsId(breakingNewsId);

        return new AnsApiResponse<>(breakingNewsSimplerDTO);
    }

    @Operation(summary = "속보뉴스 수정", description = "속보뉴스 수정")
    @PutMapping(path = "/{breakingNewsId}")
    public AnsApiResponse<BreakingNewsSimplerDTO> update(
            @Parameter(description = "필수값<br>방송일자[brdcDtm],제목[titl],속보구분[breakingNewsDiv],라인형식[lnTypCd]," +
                    "전송상태[trnsfStCd] ", required = true)
            @RequestBody @Valid BreakingNewsUpdateDTO breakingNewsUpdateDTO,
            @Parameter(name = "breakingNewsId", required = true, description = "속보뉴스 아이디")
            @PathVariable("breakingNewsId") Long breakingNewsId,
            @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        String userId = jwtGetUserService.getUser(Authorization);

        breakingNewsService.update(breakingNewsUpdateDTO, breakingNewsId, userId);

        //들어온 속보뉴스 아이디 다시 리턴.
        BreakingNewsSimplerDTO breakingNewsSimplerDTO = new BreakingNewsSimplerDTO();
        breakingNewsSimplerDTO.setBreakingNewsId(breakingNewsId);

        return new AnsApiResponse<>(breakingNewsSimplerDTO);
    }

    @Operation(summary = "속보뉴스 삭제", description = "속보뉴스 삭제")
    @DeleteMapping(path = "/{breakingNewsId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "breakingNewsId", required = true, description = "속보뉴스 아이디")
                                    @PathVariable("breakingNewsId") Long breakingNewsId,
                                    @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        String userId = jwtGetUserService.getUser(Authorization);

        breakingNewsService.delete(breakingNewsId, userId);

        return AnsApiResponse.noContent();
    }

    @Operation(summary = "속보뉴스 FTP 전송", description = "속보뉴스 FTP 전송")
    @GetMapping(path = "/send")
    public AnsApiResponse<BreakingNewsDTO> send(@Parameter(name = "breakingNewsId", required = true, description = "속보뉴스 아이디")
                     @RequestParam(value = "breakingNewsId", required = false) Long breakingNewsId,
                     @RequestHeader(value = "Authorization", required = false) String Authorization) throws UnsupportedEncodingException {

        String userId = jwtGetUserService.getUser(Authorization);

        log.info("속보뉴스 XML 생성 및 FTP 전송 : 속보뉴스 아이디 - "+breakingNewsId+" 사용자 아이디 - "+userId);

        BreakingNewsDTO breakingNewsDTO = breakingNewsService.find(breakingNewsId);

        breakingNewsDTO = breakingNewsService.send(breakingNewsDTO);

        //BreakingNewsDTO breakingNewsDTO = breakingNewsService.find(breakingNewsId);

        return new AnsApiResponse<>(breakingNewsDTO);

    }
}
