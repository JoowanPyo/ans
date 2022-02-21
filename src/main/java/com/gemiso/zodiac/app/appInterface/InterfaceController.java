package com.gemiso.zodiac.app.appInterface;

import com.gemiso.zodiac.app.appInterface.codeDTO.TakerCodeDTO;
import com.gemiso.zodiac.app.appInterface.prompterCue.PrompterCueSheetDTO;
import com.gemiso.zodiac.app.appInterface.prompterProgram.PrompterProgramDTO;
import com.gemiso.zodiac.app.appInterface.takerCueFindAllDTO.TakerCueSheetDTO;
import com.gemiso.zodiac.app.appInterface.takerCueFindAllDTO.TakerCueSheetDataDTO;
import com.gemiso.zodiac.app.appInterface.takerProgramDTO.ParentProgramDTO;
import com.gemiso.zodiac.core.helper.SearchDateInterface;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Api(description = "연계 인터페이스 API")
@RestController
@RequestMapping(path = "/interface")
@Slf4j
@RequiredArgsConstructor
public class InterfaceController {

    private final InterfaceService interfaceService;


    @Operation(summary = "큐시트 일일편성 목록조회[Taker]", description = "큐시트 일일편성 목록조회[Taker]")
    @GetMapping(path = "/dailypgm")
    public String dailyPgmFindAll(@Parameter(name = "brdc_pgm_id", description = "프로그램 아이디")
                                  @RequestParam(value = "brdc_pgm_id", required = false) String brdc_pgm_id,
                                  @Parameter(name = "pgm_nm", description = "프로그램 명")
                                  @RequestParam(value = "pgm_nm", required = false) String pgm_nm,
                                  @Parameter(description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
                                  @DateTimeFormat(pattern = "yyyy-MM-dd") String sdate,
                                  @Parameter(description = "검색 종료 날짜(yyyy-MM-dd)", required = false)
                                  @DateTimeFormat(pattern = "yyyy-MM-dd") String edate,
                                  @Parameter(name = "brdc_pgm_div_cd", description = "프로그램 구분 코드")
                                  @RequestParam(value = "brdc_pgm_div_cd", required = false) String brdc_pgm_div_cd,
                                  @Parameter(name = "del_yn", description = "삭제여부")
                                  @RequestParam(value = "del_yn", required = false) String del_yn,
                                  @Parameter(name = "ch_div_cd", description = "채널구분 코드")
                                  @RequestParam(value = "ch_div_cd", required = false) String ch_div_cd,
                                  @Parameter(name = "usr_id", description = "사용자 아이디")
                                  @RequestParam(value = "usr_id", required = false) String usr_id,
                                  @Parameter(name = "token", description = "토큰???(빈값 하드코딩)")
                                  @RequestParam(value = "token", required = false) String token,
                                  @Parameter(name = "usr_ip", description = "사용자 아이피")
                                  @RequestParam(value = "usr_ip", required = false) String usr_ip,
                                  @Parameter(name = "format", description = "포맷 타입 XML")
                                  @RequestParam(value = "format", required = false) String format,
                                  @Parameter(name = "lang", description = "랭퀴지 타입 KO")
                                  @RequestParam(value = "lang", required = false) String lang,
                                  @Parameter(name = "os_type", description = "os타입 SCU")
                                  @RequestParam(value = "os_type", required = false) String os_type,
                                  @RequestHeader(value = "securityKey") String securityKey) throws Exception {

        List<ParentProgramDTO> parentProgramDTOList = new ArrayList<>();
        String takerCueSheetDTO = "";

        //날짜로 조회조건이 들어온 경우
        if (sdate != null && sdate.trim().isEmpty() ==false && edate != null && edate.trim().isEmpty() == false) {

            //검색날짜 시간설정 (검색시작 Date = yyyy-MM-dd 00:00:00 / 검색종료 Date yyyy-MM-dd 23:59:59)
            SearchDateInterface searchDate = new SearchDateInterface(sdate, edate);
            parentProgramDTOList = interfaceService.dailyPgmFindAll(searchDate.getStartDate(), searchDate.getEndDate(), brdc_pgm_id, pgm_nm);

            takerCueSheetDTO = interfaceService.takerPgmToXml(parentProgramDTOList);
        } else { //날짜로 조회조건이 들어오지 않은 경우
            parentProgramDTOList = interfaceService.dailyPgmFindAll(null, null, brdc_pgm_id, pgm_nm);

            takerCueSheetDTO = interfaceService.takerPgmToXml(parentProgramDTOList);
        }

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

    @Operation(summary = "큐시트 상세조회[Taker]", description = "큐시트 상세조회[Taker]")
    @GetMapping(path = "/cuesheet")
    public String cueFindAll(@Parameter(name = "rd_id", description = "프로그램 아이디")
                             @RequestParam(value = "rd_id", required = false) String rd_id,
                             @Parameter(name = "play_seq", description = "플레이 시퀀스??")
                             @RequestParam(value = "play_seq", required = false) String play_seq,
                             @Parameter(name = "cued_seq", description = "큐시트 시퀀스???")
                             @RequestParam(value = "cued_seq", required = false) String cued_seq,
                             @Parameter(name = "vplay_seq", description = "v플레이 시퀀스???")
                             @RequestParam(value = "vplay_seq", required = false) String vplay_seq,
                             @Parameter(name = "vcued_seq", description = "v큐시트 시퀀스???")
                             @RequestParam(value = "vcued_seq", required = false) String vcued_seq,
                             @Parameter(name = "del_yn", description = "삭제여부")
                             @RequestParam(value = "del_yn", required = false) String del_yn,
                             @Parameter(name = "ch_div_cd", description = "채널 구분 코드")
                             @RequestParam(value = "ch_div_cd", required = false) String ch_div_cd,
                             @Parameter(name = "usr_id", description = "사용자 아이디")
                             @RequestParam(value = "usr_id", required = false) String usr_id,
                             @Parameter(name = "token", description = "토큰???(빈값 하드코딩)")
                             @RequestParam(value = "token", required = false) String token,
                             @Parameter(name = "usr_ip", description = "사용자 아이피")
                             @RequestParam(value = "usr_ip", required = false) String usr_ip,
                             @Parameter(name = "format", description = "포맷 타입 XML")
                             @RequestParam(value = "format", required = false) String format,
                             @Parameter(name = "lang", description = "랭퀴지 타입 KO")
                             @RequestParam(value = "lang", required = false) String lang,
                             @Parameter(name = "os_type", description = "os타입 SCU")
                             @RequestParam(value = "os_type", required = false) String os_type,
                             @RequestHeader(value = "securityKey") String securityKey) {


        TakerCueSheetDataDTO takerCueSheetDataDTO = interfaceService.cuefindAll(rd_id, play_seq, cued_seq, vplay_seq, vcued_seq, del_yn,
                ch_div_cd, usr_id, token, usr_ip, format, lang, os_type);

        String takerCue = interfaceService.takerCueToXml(takerCueSheetDataDTO);

        return takerCue;
    }

    @Operation(summary = "방송구분코드 조회[Taker]", description = "방송구분코드 조회[Taker]")
    @GetMapping(path = "/code")
    public String codeFindAll(@Parameter(name = "key", description = "키 ??")
                              @RequestParam(value = "key", required = false) String key,
                              @Parameter(name = "ch_div_cd", description = "채널 구분 코드")
                              @RequestParam(value = "ch_div_cd", required = false) String ch_div_cd,
                              @Parameter(name = "usr_id", description = "사용자 아이디")
                              @RequestParam(value = "usr_id", required = false) String usr_id,
                              @Parameter(name = "token", description = "토큰???")
                              @RequestParam(value = "token", required = false) String token,
                              @Parameter(name = "usr_ip", description = "사용자 아이피")
                              @RequestParam(value = "usr_ip", required = false) String usr_ip,
                              @Parameter(name = "format", description = "포맷 타입 XML")
                              @RequestParam(value = "format", required = false) String format,
                              @Parameter(name = "lang", description = "랭퀴지 타입 KO")
                              @RequestParam(value = "lang", required = false) String lang,
                              @Parameter(name = "os_type", description = "os타입 SCU")
                              @RequestParam(value = "os_type", required = false) String os_type,
                              @RequestHeader(value = "securityKey") String securityKey) {

        TakerCodeDTO takerCodeDTO = interfaceService.codeFindAll(key, ch_div_cd, usr_id, token, usr_ip, format, lang, os_type);

        String takerCode = interfaceService.codeToTakerCodeXml(takerCodeDTO);

        return takerCode;
    }

    @Operation(summary = "영상전송상태 조회[Taker]", description = "영상전송상태 조회[Taker]")
    @GetMapping(path = "/mediatransrate")
    public String mediaTransRateFindAll(@Parameter(name = "media_cd", description = "미디어 코드???")
                                        @RequestParam(value = "media_cd", required = false) String media_cd,
                                        @Parameter(name = "rd_id", description = "큐시트 아이디???")
                                        @RequestParam(value = "rd_id", required = false) String rd_id,
                                        @Parameter(name = "rd_seq", description = "시퀀스???")
                                        @RequestParam(value = "rd_seq", required = false) String rd_seq,
                                        @Parameter(name = "plyout_id", description = "페이로드 아이디???")
                                        @RequestParam(value = "plyout_id", required = false) String plyout_id,
                                        @Parameter(name = "ch_div_cd", description = "채널 구분 코드")
                                        @RequestParam(value = "ch_div_cd", required = false) String ch_div_cd,
                                        @Parameter(name = "usr_id", description = "사용자 아이디")
                                        @RequestParam(value = "usr_id", required = false) String usr_id,
                                        @Parameter(name = "token", description = "토큰???")
                                        @RequestParam(value = "token", required = false) String token,
                                        @Parameter(name = "usr_ip", description = "사용자 아이피")
                                        @RequestParam(value = "usr_ip", required = false) String usr_ip,
                                        @Parameter(name = "format", description = "포맷 타입 XML")
                                        @RequestParam(value = "format", required = false) String format,
                                        @Parameter(name = "lang", description = "랭퀴지 타입 KO")
                                        @RequestParam(value = "lang", required = false) String lang,
                                        @Parameter(name = "os_type", description = "os타입 SCU")
                                        @RequestParam(value = "os_type", required = false) String os_type,
                                        @RequestHeader(value = "securityKey") String securityKey) {

        return null;
    }

    @Operation(summary = "프롬프터 프로그램 목록조회", description = "프롬프터 프로그램 목록조회")
    @GetMapping(path = "/getMstListService")
    public String getMstListService(@Parameter(name = "media_id", description = "미디어 아이디???")
                                    @RequestParam(value = "media_id", required = false) String media_id,
                                    @Parameter(name = "pro_id", description = "프로그램 아이디???")
                                    @RequestParam(value = "pro_id", required = false) String pro_id,
                                    @Parameter(name = "sdate", description = "검색 시작일자???")
                                    @RequestParam(value = "sdate", required = false) String sdate,
                                    @Parameter(name = "fdate", description = "검색 종료일자???")
                                    @RequestParam(value = "fdate", required = false) String fdate,
                                    @Parameter(name = "usr_id", description = "사용자 아이디???")
                                    @RequestParam(value = "usr_id", required = false) String usr_id,
                                    @RequestHeader(value = "securityKey") String securityKey) throws Exception {

        List<PrompterProgramDTO> prompterProgramDTOList = new ArrayList<>();
        String prompterProgram = "";

        //날짜로 조회조건이 들어온 경우
        if (sdate != null && sdate.trim().isEmpty() ==false && fdate != null && fdate.trim().isEmpty() == false) {

            //검색날짜 시간설정 (검색시작 Date = yyyy-MM-dd 00:00:00 / 검색종료 Date yyyy-MM-dd 23:59:59)
            SearchDateInterface searchDate = new SearchDateInterface(sdate, fdate);

            prompterProgramDTOList = interfaceService.getMstListService(pro_id, searchDate.getStartDate(), searchDate.getEndDate());

            prompterProgram = interfaceService.prompterProgramToXml(prompterProgramDTOList);
        }else {
            prompterProgramDTOList = interfaceService.getMstListService(pro_id, null, null);

            prompterProgram = interfaceService.prompterProgramToXml(prompterProgramDTOList);
        }


        return prompterProgram;
    }

    @Operation(summary = "프롬프트 큐시트 상세조회", description = "프롬프트 큐시트 상세조회")
    @GetMapping(path = "/getCuesheetService")
    public String getCuesheetService(@Parameter(name = "cs_id", description = "큐시트 아이디???")
                                     @RequestParam(value = "cs_id", required = false) Long cs_id,
                                     @Parameter(name = "usr_id", description = "사용자 아이디???")
                                     @RequestParam(value = "usr_id", required = false) String usr_id,
                                     @Parameter(name = "user_ip", description = "사용자 아이피???")
                                     @RequestParam(value = "user_ip", required = false) String user_ip,
                                     @RequestHeader(value = "securityKey") String securityKey) {

        List<PrompterCueSheetDTO> prompterCueSheetDTOList = interfaceService.getCuesheetService(cs_id);

        String prompterCueSheetXml = interfaceService.prompterCueSheetXml(prompterCueSheetDTOList);

        return prompterCueSheetXml;
    }
}
