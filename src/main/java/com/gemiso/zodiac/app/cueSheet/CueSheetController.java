package com.gemiso.zodiac.app.cueSheet;

import com.gemiso.zodiac.app.cueSheet.dto.CueSheetCreateDTO;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetDTO;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetUpdateDTO;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupCreateDTO;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupUpdateDTO;
import com.gemiso.zodiac.core.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Api(description = "큐시트 API")
@RestController
@RequestMapping("/cuesheet")
@Log4j2
@RequiredArgsConstructor
public class CueSheetController {

    private final CueSheetService cueSheetService;


    @Operation(summary = "큐시트 목록조회", description = "큐시트 목록조회")
    @GetMapping(path = "")
    public ApiResponse<List<CueSheetDTO>> findAll(@Parameter(description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                  @Parameter(description = "검색 종료 날짜(yyyy-MM-dd)", required = false)
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                  @Parameter(name = "brdcPgmId", description = "프로그램구분코드")
                                                  @RequestParam(value = "brdcPgmId", required = false) Long brdcPgmId,
                                                  @Parameter(name = "brdcPgmNm", description = "프로그램구분코드")
                                                  @RequestParam(value = "brdcPgmNm", required = false) String brdcPgmNm,
                                                  @Parameter(name = "searchWord", description = "프로그램구분코드")
                                                  @RequestParam(value = "searchWord", required = false) String searchWord) {

        List<CueSheetDTO> cueSheetDTOList = cueSheetService.findAll(sdate, edate, brdcPgmId, brdcPgmNm, searchWord);

        return new ApiResponse<>(cueSheetDTOList);

    }

    @Operation(summary = "큐시트 상세조회", description = "큐시트 상세조회")
    @GetMapping(path = "/{cueId}")
    public ApiResponse<CueSheetDTO> find(@Parameter(name = "cueId", description = "큐시트 아이디")
                                         @PathVariable("cueId") Long cueId) {


        CueSheetDTO cueSheetDTO = cueSheetService.find(cueId);

        return new ApiResponse<>(cueSheetDTO);
    }


    @Operation(summary = "큐시트 등록", description = "큐시트 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CueSheetDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                           @RequestBody @Valid CueSheetCreateDTO cueSheetCreateDTO,
                                           @Parameter(name = "cueTmpltId", description = "큐시트템플릿아이디", in = ParameterIn.QUERY)
                                           @RequestParam(value = "cueTmpltId", required = false) Long cueTmpltId
    ) {

        Long cueId = cueSheetService.create(cueSheetCreateDTO);

        //수정! 큐시트아이템복사.

        CueSheetDTO cueSheetDTO = cueSheetService.find(cueId);

        return new ApiResponse<>(cueSheetDTO);
    }

    @Operation(summary = "큐시트 수정", description = "큐시트 수정")
    @PutMapping(path = "/{cueId}")
    public ApiResponse<CueSheetDTO> update(@Parameter(name = "cueSheetUpdateDTO", required = true, description = "필수값<br>")
                                           @Valid @RequestBody CueSheetUpdateDTO cueSheetUpdateDTO,
                                           @Parameter(name = "cueId", required = true, description = "큐시트 아이디")
                                           @PathVariable("cueId") Long cueId) {

        cueSheetService.update(cueSheetUpdateDTO, cueId);

        CueSheetDTO cueSheetDTO = cueSheetService.find(cueId);

        return new ApiResponse<>(cueSheetDTO);

    }

    @Operation(summary = "큐시트 삭제", description = "큐시트 삭제")
    @DeleteMapping(path = "/{cueId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> delete(@Parameter(name = "cueId", required = true, description = "큐시트 아이디")
                                           @PathVariable("cueId") Long cueId) {

        cueSheetService.delete(cueId);

        return ApiResponse.noContent();
    }


}
