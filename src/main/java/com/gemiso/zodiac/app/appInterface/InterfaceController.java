package com.gemiso.zodiac.app.appInterface;

import com.gemiso.zodiac.app.appInterface.programDTO.ParentProgramDTO;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.page.PageResultDTO;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api(description = "연계 인터페이스 API")
@RestController
@RequestMapping(path = "/interface")
@Slf4j
@RequiredArgsConstructor
public class InterfaceController {

    private final InterfaceService interfaceService;


    @Operation(summary = "일일편성 목록조회[Taker]", description = "일일편성 목록조회[Taker]")
    @GetMapping(path = "/dailypgm")
    public String dailyPgmFindAll(@Parameter(description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
                                                          @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                          @Parameter(description = "검색 종료 날짜(yyyy-MM-dd)", required = false)
                                                          @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                          @RequestHeader(value = "securityKey") String securityKey) throws Exception {

        PageResultDTO<ParentProgramDTO, CueSheet> pageResultDTO = null;
        String takerCueSheetDTO = "";

        //검색날짜 시간설정 (검색시작 Date = yyyy-MM-dd 00:00:00 / 검색종료 Date yyyy-MM-dd 23:59:59)
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            SearchDate searchDate = new SearchDate(sdate, edate);
            pageResultDTO = interfaceService.cueFindAll(searchDate.getStartDate(), searchDate.getEndDate());

            takerCueSheetDTO = interfaceService.toXml(pageResultDTO);
        } else {
            pageResultDTO = interfaceService.cueFindAll(null, null);

            takerCueSheetDTO = interfaceService.toXml(pageResultDTO);
        }

        System.out.println("controller xml :" + takerCueSheetDTO);

        return takerCueSheetDTO;

    }

    /*@Operation(summary = "큐시트 상세조회[Taker]", description = "큐시트 상세조회[Taker]")
    @GetMapping(path = "/cuesheet/{cueId}")
    public AnsApiResponse<CueSheetDTO> cueFind(@Parameter(name = "cueId", description = "큐시트 아이디")
                                            @PathVariable("cueId") Long cueId,
                                            @RequestHeader(value = "securityKey") String securityKey) {


        CueSheetDTO cueSheetDTO = interfaceService.cueFind(cueId);

        return new AnsApiResponse<>(cueSheetDTO);
    }

    @Operation(summary = "큐시트 아이템 목록조회[Taker]", description = "큐시트 아이템 목록조회[Taker]")
    @GetMapping(path = "/cuesheet/{cueId}/cuesheetitems")
    public AnsApiResponse<List<CueSheetItemDTO>> cueItemFindAll(@Parameter(name = "artlcId", description = "기사 아이디")
                                                             @RequestParam(value = "artlcId", required = false) Long artlcId,
                                                             @Parameter(name = "cueId", description = "큐시트 아이디")
                                                             @PathVariable("cueId") Long cueId,
                                                             @RequestHeader(value = "securityKey") String securityKey) {

        List<CueSheetItemDTO> cueSheetItemDTOList = interfaceService.cueItemFindAll(artlcId, cueId);

        return new AnsApiResponse<>(cueSheetItemDTOList);
    }*/

    @Operation(summary = "큐시트 목록조회[Taker]", description = "큐시트 목록조회[Taker]")
    @GetMapping(path = "/cuesheet")
    public String cueFindAll(){

        return null;
    }

    @Operation(summary = "방송구분코드 조회[Taker]", description = "방송구분코드 조회[Taker]")
    @GetMapping(path = "/code")
    public String codeFindAll(){

        return null;
    }

    @Operation(summary = "영상전송상태 조회[Taker]", description = "영상전송상태 조회[Taker]")
    @GetMapping(path = "/mediatransrate")
    public String mediaTransRateFindAll(){

        return null;
    }


}
