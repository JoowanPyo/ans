package com.gemiso.zodiac.app.issue;

import com.gemiso.zodiac.app.issue.dto.IssueCopyDTO;
import com.gemiso.zodiac.app.issue.dto.IssueCreateDTO;
import com.gemiso.zodiac.app.issue.dto.IssueDTO;
import com.gemiso.zodiac.app.issue.dto.IssueUpdateDTO;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.response.AnsApiResponse;
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
    public AnsApiResponse<List<IssueDTO>> findAll(@Parameter(description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
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

        } else {
            issueList = issueService.findAll(null, null, issuDelYn);
        }
        return new AnsApiResponse<>(issueList);
    }


    @Operation(summary = "이슈 상세정보 조회", description = "이슈아이디로 이슈상세 정보 조회")
    @GetMapping(path = "/{issuId}")
    public AnsApiResponse<IssueDTO> find(@Parameter(name = "issuId", required = true, description = "이슈아이디", in = ParameterIn.PATH)
                                         @PathVariable("issuId") Long issuId) {

        IssueDTO issu = issueService.find(issuId);

        return new AnsApiResponse<>(issu);

    }

    @Operation(summary = "이슈 등록", description = "신규이슈 등록")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<IssueDTO> create(
            @Parameter(description = "필수값<br>이슈제목, 이슈일자", required = true) @RequestBody IssueCreateDTO issueCreateDTO) throws Exception {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        log.info(" Issue Create : userId - "+userId +"<br>" +" Issue Model - "+issueCreateDTO);

        IssueDTO issuDto = issueService.create(issueCreateDTO, userId);

        return new AnsApiResponse<>(issuDto);
    }

    @Operation(summary = "이슈 수정", description = "이슈 수정")
    @PutMapping(path = "/{issuId}")
    public AnsApiResponse<IssueDTO> update(
            @Parameter(description = "Update issue object", required = true) @RequestBody IssueUpdateDTO issueDTO,
            @Parameter(description = "이슈 아이디", required = true) @PathVariable("issuId") Long issuId) {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        log.info(" Issue Update : userId - "+userId +"<br>" +" Issue Model - "+issueDTO);

        issueService.update(issueDTO, issuId, userId);

        IssueDTO issueDto = issueService.find(issuId);

        return new AnsApiResponse<>(issueDto);
    }

    @Operation(summary = "이슈 삭제", description = "기존이슈 삭제")
    @PatchMapping(path = "/{issuId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(
            @Parameter(name = "issuId", required = true, description = "이슈아이디") @PathVariable("issuId") Long issuId) {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        log.info(" Issue Delete : userId - "+userId +"<br>" +" Issue Id - "+issuId);

        issueService.delete(issuId, userId);

        return AnsApiResponse.noContent();
    }

    @Operation(summary = "이슈 복사", description = "기본에 등록되어 있던 이슈 선택 날짜로 복사")
    @PostMapping(path = "/{targetDate}")
    public AnsApiResponse<List<IssueDTO>> copy(
            @Parameter(description = "복사할 날짜(yyyy-MM-dd HH:mm:ss)", required = true)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @PathVariable("targetDate") Date targetDate,
            @Parameter(description = "복사할 이슈", required = true) @RequestBody List<IssueCopyDTO> issueCopyDTO
    ) throws Exception {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        log.info(" Issue Copy : userId - "+userId +" targetDate - "+targetDate+"<br>" +" Issue Model - "+issueCopyDTO.toString());

        Date endDate = issueService.copy(issueCopyDTO, targetDate, userId);

        List<IssueDTO> issueDTOList = issueService.findAll(targetDate, endDate, "N");

        return new AnsApiResponse<>(issueDTOList);

    }

    @Operation(summary = "이슈 순서 변경", description = "이슈 순서 변경")
    @PutMapping("/{issuId}/changeOrder")
    public AnsApiResponse<List<IssueDTO>> changeOrder(@Parameter(name = "issuId", required = true, description = "이슈아이디")
                                                      @PathVariable("issuId") Long issuId,
                                                      @Parameter(name = "issuOrd", required = true, description = "이슈 순번")
                                                      @RequestParam(value = "issuOrd") Integer issuOrd) throws Exception {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        log.info(" Issue Change Ord : userId - "+userId +" Issue Id - "+issuId + " IssueOrd - "+issuOrd);


        List<IssueDTO> issueDTOList = issueService.changeOrder(issuId, issuOrd);

        return new AnsApiResponse<>(issueDTOList);
    }

    @Operation(summary = "삭제 이슈 복구", description = "삭제 이슈 복구")
    @PutMapping(path = "/{issuId}/restore")
    public AnsApiResponse<IssueDTO> restoreIssue(@Parameter(name = "issuId", required = true, description = "이슈아이디")
                                                 @PathVariable("issuId") Long issuId) {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        log.info(" Issue Restore : userId - "+userId +" Issue Id - "+issuId );

        issueService.restoreIssue(issuId);

        IssueDTO issueDto = issueService.find(issuId);

        return new AnsApiResponse<>(issueDto);

    }


}
