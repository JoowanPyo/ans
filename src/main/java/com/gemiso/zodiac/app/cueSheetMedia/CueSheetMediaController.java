package com.gemiso.zodiac.app.cueSheetMedia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaCreateDTO;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaDTO;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaUpdateDTO;
import com.gemiso.zodiac.app.cueSheetTemplateMedia.dto.CueTmpltMediaDTO;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@Api(description = "큐시트 미디어 API")
@RestController
@RequestMapping("/cuesheetmedia")
@Slf4j
@RequiredArgsConstructor
public class CueSheetMediaController {

    private final CueSheetMediaService cueSheetMediaService;

    @Operation(summary = "큐시트 템플릿 미디어 목록조회", description = "큐시트 템플릿 미디어 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<CueSheetMediaDTO>> findAll(@Parameter(description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
                                                          @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                          @Parameter(description = "검색 종료 날짜(yyyy-MM-dd)", required = false)
                                                          @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                          @Parameter(name = "trnsfFileNm", description = "전송 파일 명")
                                                          @RequestParam(value = "trnsfFileNm", required = false) String trnsfFileNm,
                                                          @Parameter(name = "cueItemId", description = "큐시트 아이템 아이")
                                                          @RequestParam(value = "cueItemId", required = false) Long cueItemId,
                                                          @Parameter(name = "mediaTypCd", description = "미디어 유형 코드[media_typ_001 : 영상, media_typ_002 : 백드롭]")
                                                          @RequestParam(value = "mediaTypCd", required = false) String mediaTypCd,
                                                          @Parameter(name = "delYn", description = "큐시트 미디어 삭제 여부 ( N, Y )")
                                                              @RequestParam(value = "delYn", required = false) String delYn) throws Exception {

        List<CueSheetMediaDTO> cueSheetMediaDTOList = new ArrayList<>();

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            //검색날짜 시간설정 (검색시작 Date = yyyy-MM-dd 00:00:00 / 검색종료 Date yyyy-MM-dd 23:59:59)
            SearchDate searchDate = new SearchDate(sdate, edate);

            cueSheetMediaDTOList = cueSheetMediaService.findAll(searchDate.getStartDate(), searchDate.getEndDate(), trnsfFileNm, cueItemId, mediaTypCd, delYn);

        } else {
            cueSheetMediaDTOList = cueSheetMediaService.findAll(null, null, trnsfFileNm, cueItemId, mediaTypCd, delYn);
        }
        return new AnsApiResponse<>(cueSheetMediaDTOList);
    }

    @Operation(summary = "큐시트 미디어 상세조회", description = "큐시트 미디어 상세조회")
    @GetMapping("/{cueMediaId}")
    public AnsApiResponse<CueSheetMediaDTO> find(@Parameter(name = "cueMediaId", required = true, description = "큐시트 미디어 아이디")
                                              @PathVariable("cueMediaId") long cueMediaId) {

        CueSheetMediaDTO cueSheetMediaDTO = cueSheetMediaService.find(cueMediaId);

        return new AnsApiResponse<>(cueSheetMediaDTO);
    }

    @Operation(summary = "큐시트 미디어 등록", description = "큐시트 미디어 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<CueSheetMediaDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                                @RequestBody @Valid CueSheetMediaCreateDTO cueSheetMediaCreateDTO) throws Exception {

        Long cueMediaId = cueSheetMediaService.create(cueSheetMediaCreateDTO);

        CueSheetMediaDTO cueSheetMediaDTO = cueSheetMediaService.find(cueMediaId);

        return new AnsApiResponse<>(cueSheetMediaDTO);

    }

    @Operation(summary = "큐시트 미디어 수정", description = "큐시트 미디어 수정")
    @PutMapping(path = "/{cueMediaId}")
    public AnsApiResponse<CueSheetMediaDTO> update(@Parameter(description = "필수값<br> ", required = true)
                                                @RequestBody @Valid CueSheetMediaUpdateDTO cueSheetMediaUpdateDTO,
                                                   @Parameter(name = "cueMediaId", required = true, description = "큐시트 미디어 아이디")
                                                @PathVariable("cueMediaId") long cueMediaId) {

        cueSheetMediaService.update(cueSheetMediaUpdateDTO, cueMediaId);

        CueSheetMediaDTO cueSheetMediaDTO = cueSheetMediaService.find(cueMediaId);

        return new AnsApiResponse<>(cueSheetMediaDTO);

    }

    @Operation(summary = "큐시트 미디어 삭제", description = "큐시트 미디어 삭제")
    @DeleteMapping(path = "/{cueMediaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "cueMediaId", required = true, description = "큐시트 미디어 아이디")
                                 @PathVariable("cueMediaId") long cueMediaId) throws JsonProcessingException {

        cueSheetMediaService.delete(cueMediaId);

        return AnsApiResponse.noContent();
    }

}
