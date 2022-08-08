package com.gemiso.zodiac.app.yonhapAssign;

import com.gemiso.zodiac.app.yonhapAssign.dto.YonhapAssignCreateDTO;
import com.gemiso.zodiac.app.yonhapAssign.dto.YonhapAssignDTO;
import com.gemiso.zodiac.app.yonhapAssign.dto.YonhapAssignResponseDTO;
import com.gemiso.zodiac.app.yonhapAssign.dto.YonhapAssignUpdateDTO;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.gemiso.zodiac.core.service.JwtGetUserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(description = "연합승인 API")
@RestController
@RequestMapping("/yonhapassign")
@RequiredArgsConstructor
@Slf4j
public class YonhapAssignController {

    private final YonhapAssignService yonhapAssignService;

    private final JwtGetUserService jwtGetUserService;


    @Operation(summary = "연합담당자지정 목록조회", description = "연합담당자지정 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<YonhapAssignDTO>> findAll(@Parameter(description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                         @Parameter(description = "검색 종료 날짜(yyyy-MM-dd)", required = false)
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                         @Parameter(name = "yonhapId", description = "연합기사 아이디")
                                                         @RequestParam(value = "yonhapId", required = false) Long yonhapId,
                                                         @Parameter(name = "wireId", description = "연합외신기사 아이디")
                                                         @RequestParam(value = "wireId", required = false) Long wireId,
                                                         @Parameter(name = "designatorId", description = "지정자 아이디")
                                                         @RequestParam(value = "designatorId", required = false) String designatorId,
                                                         @Parameter(name = "assignerId", description = "담당자 아이디")
                                                         @RequestParam(value = "assignerId", required = false) String assignerId) throws Exception {

        List<YonhapAssignDTO> yonhapAssignDTOList = new ArrayList<>();


        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) { //날짜검색 조건이 들어올경우.
            //검색날짜 시간설정 (검색시작 Date = yyyy-MM-dd 00:00:00 / 검색종료 Date yyyy-MM-dd 24:00:00)
            SearchDate searchDate = new SearchDate(sdate, edate);
            yonhapAssignDTOList = yonhapAssignService.findAll(searchDate.getStartDate(), searchDate.getEndDate(),
                    yonhapId, wireId, designatorId, assignerId);
        } else {//날짜 검색조건이 없을 경우.

            yonhapAssignDTOList = yonhapAssignService.findAll(null, null, yonhapId, wireId, designatorId, assignerId);
        }
        return new AnsApiResponse<>(yonhapAssignDTOList);
    }

    @Operation(summary = "연합담당자지정 상세조회", description = "연합담당자지정 상세조회")
    @GetMapping(path = "/{assignId}")
    public AnsApiResponse<YonhapAssignDTO> find(@Parameter(name = "assignId", description = "연합담당자지정 아이디")
                                                @PathVariable("assignId") Long assignId) {

        YonhapAssignDTO yonhapAssignDTO = yonhapAssignService.find(assignId);

        return new AnsApiResponse<>(yonhapAssignDTO);
    }


    @Operation(summary = "연합담당자지정  등록", description = "연합담당자지정 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<YonhapAssignResponseDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                                          @RequestBody YonhapAssignCreateDTO yonhapAssignCreateDTO,
                                                          @RequestHeader(value = "Authorization", required = false)String Authorization) throws Exception {

        String userId =jwtGetUserService.getUser(Authorization);

        YonhapAssignResponseDTO returnId = new YonhapAssignResponseDTO();

        Long assignId = yonhapAssignService.create(yonhapAssignCreateDTO, userId);
        returnId.setAssignId(assignId);

        return new AnsApiResponse<>(returnId);
    }

    @Operation(summary = "연합담당자지정  수정", description = "연합담당자지정 수정")
    @PutMapping(path = "/{assignId}")
    public AnsApiResponse<YonhapAssignResponseDTO> update(@Parameter(name = "assignId", description = "연합담당자지정 아이디")
                                                          @PathVariable("assignId") Long assignId,
                                                          @Parameter(description = "필수값<br> ", required = true)
                                                          @RequestBody YonhapAssignUpdateDTO yonhapAssignUpdateDTO) {

        YonhapAssignResponseDTO returnId = new YonhapAssignResponseDTO();

        yonhapAssignService.update(yonhapAssignUpdateDTO, assignId);

        returnId.setAssignId(assignId);

        return new AnsApiResponse<>(returnId);
    }

    @Operation(summary = "연합담당자지정  삭제", description = "연합담당자지정 삭제")
    @DeleteMapping(path = "/{assignId}")
    public AnsApiResponse<?> delete(@Parameter(name = "assignId", description = "연합담당자지정 아이디")
                                    @PathVariable("assignId") Long assignId) {

        yonhapAssignService.delete(assignId);

        return AnsApiResponse.noContent();
    }
}
