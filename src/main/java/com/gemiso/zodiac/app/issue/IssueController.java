package com.gemiso.zodiac.app.issue;

import com.gemiso.zodiac.app.issue.dto.IssueCopyDTO;
import com.gemiso.zodiac.app.issue.dto.IssueCreateDTO;
import com.gemiso.zodiac.app.issue.dto.IssueDTO;
import com.gemiso.zodiac.app.issue.dto.IssueUpdateDTO;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.response.ApiResponse;
import com.gemiso.zodiac.core.service.UserAuthService;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Tag(name = "issue", description = "이슈 API")
@Api(description = "이슈 API")
@RestController
@RequestMapping("/issues")
@RequiredArgsConstructor
@Slf4j
public class IssueController {

    private final IssueService issueService;
    private final UserAuthService userAuthService;

    @Operation(summary = "이슈 목록 조회", description = "조회조건으로 이슈 목록 조회")
    @GetMapping(path = "")
    public ApiResponse<List<IssueDTO>> findAll(@Parameter(description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                               @Parameter(description = "검색 종료 날짜(yyyy-MM-dd)", required = false)
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                               @Parameter(name = "issuDelYn", description = "삭제여부 (N , Y)")
                                               @RequestParam(value = "issuDelYn", required = false) String issuDelYn) throws Exception {

        List<IssueDTO> issueList = new ArrayList<>();

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            //검색날짜 시간설정 (검색시작 Date = yyyy-MM-dd 00:00:00 / 검색종료 Date yyyy-MM-dd 23:59:59)
            SearchDate searchDate = new SearchDate(sdate, edate);

            issueList = issueService.findAll(searchDate.getStartDate(), searchDate.getEndDate(), issuDelYn);

        }else {
            issueList = issueService.findAll(null, null, issuDelYn);
        }
        return new ApiResponse<>(issueList);
    }


    @Operation(summary = "이슈 상세정보 조회", description = "이슈아이디로 이슈상세 정보 조회")
    @GetMapping(path = "/{issuId}")
    public ApiResponse<IssueDTO> find(@Parameter(name = "issuId", required = true, description = "이슈아이디", in = ParameterIn.PATH)
                                      @PathVariable("issuId") long issuId) {

        IssueDTO issu = issueService.find(issuId);

        return new ApiResponse<>(issu);

    }

    @Operation(summary = "이슈 등록", description = "신규이슈 등록")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<IssueDTO> create(
            @Parameter(description = "필수값<br>이슈제목, 이슈일자", required = true) @RequestBody IssueCreateDTO issueCreateDTO) throws Exception {

        IssueDTO issuDto = issueService.create(issueCreateDTO);

        return new ApiResponse<>(issuDto);
    }

    @Operation(summary = "이슈 수정", description = "이슈 수정")
    @PutMapping(path = "/{issuId}")
    public ApiResponse<IssueDTO> update(
            @Parameter(description = "Update issue object", required = true) @RequestBody IssueUpdateDTO issueDTO,
            @Parameter(description = "이슈 아이디", required = true) @PathVariable("issuId") Long issuId) {

        issueService.update(issueDTO, issuId);

        IssueDTO issueDto = issueService.find(issuId);

        return new ApiResponse<>(issueDto);
    }

    @Operation(summary = "이슈 삭제", description = "기존이슈 삭제")
    @PatchMapping(path = "/{issuId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> delete(
            @Parameter(name = "issuId", required = true, description = "이슈아이디") @PathVariable("issuId") Long issuId) {

        issueService.delete(issuId);

        return ApiResponse.noContent();
    }

    @Operation(summary = "이슈 복사", description = "기본에 등록되어 있던 이슈 선택 날짜로 복사")
    @PostMapping(path = "/{targetDate}")
    public ApiResponse<List<IssueDTO>> copy(
            @Parameter(description = "복사할 날짜(yyyy-MM-dd HH:mm:ss)", required = true)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @PathVariable("targetDate") Date targetDate,
            @Parameter(description = "복사할 이슈", required = true) @RequestBody List<IssueCopyDTO> issueCopyDTO
    ) throws Exception {

        Date endDate = issueService.copy(issueCopyDTO, targetDate);

        List<IssueDTO> issueDTOList = issueService.findAll(targetDate, endDate, "N");

        return new ApiResponse<>(issueDTOList);

    }

    @Operation(summary = "이슈 순서 변경", description = "이슈 순서 변경")
    @PutMapping("/{issuId}/changeOrder")
    public ApiResponse<List<IssueDTO>> changeOrder(@Parameter(name = "issuId", required = true, description = "이슈아이디") @PathVariable("issuId") Long issuId,
                                                   @Parameter(name = "issuOrd", required = true, description = "이슈 순번")
                                                   @RequestParam(value = "issuOrd") Integer issuOrd) throws Exception {

        List<IssueDTO> issueDTOList = issueService.changeOrder(issuId, issuOrd);

        return new ApiResponse<>(issueDTOList);
    }



}
