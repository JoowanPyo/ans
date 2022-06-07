package com.gemiso.zodiac.app.appInterface;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gemiso.zodiac.app.appInterface.codeDTO.TakerCodeDTO;
import com.gemiso.zodiac.app.appInterface.mediaTransferDTO.MediaTransferDTO;
import com.gemiso.zodiac.app.appInterface.prompterCueDTO.PrompterCueSheetDTO;
import com.gemiso.zodiac.app.appInterface.prompterCueDTO.PrompterCueSheetDataDTO;
import com.gemiso.zodiac.app.appInterface.prompterProgramDTO.PrompterProgramDTO;
import com.gemiso.zodiac.app.appInterface.takerCueFindAllDTO.TakerCueSheetDataDTO;
import com.gemiso.zodiac.app.appInterface.takerCueRefreshDTO.TakerCueRefreshDataDTO;
import com.gemiso.zodiac.app.appInterface.takerCueRefreshDTO.TakerSpareCueRefreshDataDTO;
import com.gemiso.zodiac.app.appInterface.takerProgramDTO.ParentProgramDTO;
import com.gemiso.zodiac.app.appInterface.takerUpdateDTO.TakerCdUpdateDTO;
import com.gemiso.zodiac.app.appInterface.takerUpdateDTO.TakerToCueBodyDTO;
import com.gemiso.zodiac.app.article.dto.ArticleCreateDTO;
import com.gemiso.zodiac.app.article.dto.ArticleDeleteConfirmDTO;
import com.gemiso.zodiac.app.article.dto.ArticleUpdateDTO;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaDTO;
import com.gemiso.zodiac.app.cueSheet.CueSheetService;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetDTO;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetFindAllDTO;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItemService;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemDTO;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.helper.SearchDateInterface;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(description = "연계 인터페이스 API")
@RestController
@RequestMapping(path = "/interface")
@Slf4j
@RequiredArgsConstructor
public class InterfaceController {

    private final InterfaceService interfaceService;

    private final CueSheetService cueSheetService;
    private final CueSheetItemService cueSheetItemService;


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

        log.info("Taker FindAll : brdc_pgm_id - " + brdc_pgm_id + " pgm_nm -" + pgm_nm);

        List<ParentProgramDTO> parentProgramDTOList = new ArrayList<>();
        String takerCueSheetDTO = "";

        //날짜로 조회조건이 들어온 경우
        if (sdate != null && sdate.trim().isEmpty() == false && edate != null && edate.trim().isEmpty() == false) {

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

        log.info("Taker Find : rd_id - " + rd_id);

        TakerCueSheetDataDTO takerCueSheetDataDTO = interfaceService.cuefindAll(rd_id, play_seq, cued_seq, vplay_seq, vcued_seq, del_yn,
                ch_div_cd, usr_id, token, usr_ip, format, lang, os_type);

        String takerCue = interfaceService.takerCueToXml(takerCueSheetDataDTO);

        return takerCue;
    }

    @Operation(summary = "테이커 큐시트 아이템 Refresh", description = "테이커 큐시트 아이템 Refresh")
    @GetMapping(path = "/takerrefresh")
    public String takerCueItemRefresh(@Parameter(name = "rd_id", description = "프로그램 아이디")
                                      @RequestParam(value = "rd_id", required = false) Long rd_id,
                                      @Parameter(name = "rd_seq", description = "큐시트 아이템 순서값")
                                      @RequestParam(value = "rd_seq", required = false) Integer rd_seq,
                                      @Parameter(name = "spare_yn", description = "스페어 여부값(N, Y)")
                                      @RequestParam(value = "spare_yn", required = false) String spare_yn,
                                      @RequestHeader(value = "securityKey") String securityKey) {

        log.info("Taker Refresh : rd_id - " + rd_id + " rd_seq - " + rd_seq + " spare_yn - " + spare_yn);

        String returnData = "";

        if ("N".equals(spare_yn)) {

            TakerCueRefreshDataDTO takerCueSheetDTO = interfaceService.takerCueItemRefresh(rd_id, rd_seq);

            returnData = interfaceService.takerCueRefresh(takerCueSheetDTO);

        } else {

            TakerSpareCueRefreshDataDTO takerSpareCueSheetDTO = interfaceService.takerSpareCueItemRefresh(rd_id, rd_seq);

            returnData = interfaceService.takerSpareCueRefresh(takerSpareCueSheetDTO);

        }
        return returnData;
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
    @GetMapping(path = "/getmstlistservice")
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


        log.info("Prompter FindAll : Start - " + sdate + " End - " + fdate + " pro_id - " + pro_id);

        List<PrompterProgramDTO> prompterProgramDTOList = new ArrayList<>();
        String prompterProgram = "";

        //날짜로 조회조건이 들어온 경우
        if (sdate != null && sdate.trim().isEmpty() == false && fdate != null && fdate.trim().isEmpty() == false) {

            //검색날짜 시간설정 (검색시작 Date = yyyy-MM-dd 00:00:00 / 검색종료 Date yyyy-MM-dd 23:59:59)
            SearchDateInterface searchDate = new SearchDateInterface(sdate, fdate);

            prompterProgramDTOList = interfaceService.getMstListService(pro_id, searchDate.getStartDate(), searchDate.getEndDate());

            prompterProgram = interfaceService.prompterProgramToXml(prompterProgramDTOList);
        } else {
            prompterProgramDTOList = interfaceService.getMstListService(pro_id, null, null);

            prompterProgram = interfaceService.prompterProgramToXml(prompterProgramDTOList);
        }


        return prompterProgram;
    }

    @Operation(summary = "프롬프트 큐시트 상세조회", description = "프롬프트 큐시트 상세조회")
    @GetMapping(path = "/getcuesheetservice")
    public String getCuesheetService(@Parameter(name = "cs_id", description = "큐시트 아이디???")
                                     @RequestParam(value = "cs_id", required = false) Long cs_id,
                                     @Parameter(name = "usr_id", description = "사용자 아이디???")
                                     @RequestParam(value = "usr_id", required = false) String usr_id,
                                     @Parameter(name = "user_ip", description = "사용자 아이피???")
                                     @RequestParam(value = "user_ip", required = false) String user_ip,
                                     @RequestHeader(value = "securityKey") String securityKey) {

        log.info("Prompter Find : cs_id - " + cs_id);

        //set Lsit<PrompterCueRefreshDTO>
        PrompterCueSheetDataDTO prompterCueSheetDataDTO = interfaceService.getCuesheetService(cs_id);

        String prompterCueSheetXml = interfaceService.prompterCueSheetXml(prompterCueSheetDataDTO);

        return prompterCueSheetXml;
    }

    //일단 프롬프터 리플레시 기능은x 테이커 먼저 (2022.03.15)
    /*@Operation(summary = "프롬프트 큐시트 기사 리플레시", description = "프롬프트 큐시트 기사 리플레시")
    @GetMapping(path = "/prompter_refresh")
    public String prompterRefresh(@Parameter(name = "rd_id", description = "큐시트 아이템 아이디")
                                  @RequestParam(value = "rd_id", required = false) Long rd_id) {

        PrompterCueRefreshDTO prompterCueRefreshDTO = interfaceService.prompterRefresh(rd_id);

    }*/

    @Operation(summary = "큐시트 목록조회[PS TAKER]", description = "큐시트 목록조회[PS TAKER]")
    @GetMapping(path = "/pstakerlist")
    public AnsApiResponse<CueSheetFindAllDTO> findAll(@Parameter(description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                      @Parameter(description = "검색 종료 날짜(yyyy-MM-dd)", required = false)
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                      @Parameter(name = "brdcPgmId", description = "프로그램 아이디")
                                                      @RequestParam(value = "brdcPgmId", required = false) String brdcPgmId,
                                                      @Parameter(name = "brdcPgmNm", description = "프로그램 명")
                                                      @RequestParam(value = "brdcPgmNm", required = false) String brdcPgmNm,
                                                      @Parameter(name = "searchWord", description = "검색키워드")
                                                      @RequestParam(value = "searchWord", required = false) String searchWord,
                                                      @RequestHeader(value = "securityKey") String securityKey) throws Exception {

        log.info("PS Taker FindAll : Start - " + sdate + " End - " + edate + " brdcPgmId - " + brdcPgmId +
                " brdcPgmNm - " + brdcPgmNm + " searchWord - " + searchWord);

        CueSheetFindAllDTO cueSheetFindAllDTO = new CueSheetFindAllDTO();


        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            //검색날짜 시간설정 (검색시작 Date = yyyy-MM-dd 00:00:00 / 검색종료 Date yyyy-MM-dd 24:00:00)
            SearchDate searchDate = new SearchDate(sdate, edate);
            cueSheetFindAllDTO = cueSheetService.psTakerFindAll(searchDate.getStartDate(), searchDate.getEndDate(), brdcPgmId, brdcPgmNm, searchWord);

        } else {
            cueSheetFindAllDTO = cueSheetService.psTakerFindAll(null, null, brdcPgmId, brdcPgmNm, searchWord);
        }

        return new AnsApiResponse<>(cueSheetFindAllDTO);
    }

    @Operation(summary = "큐시트 상세조회[PS TAKER]", description = "큐시트 상세조회[PS TAKER]")
    @GetMapping(path = "/pstaker")
    public AnsApiResponse<CueSheetDTO> find(@Parameter(name = "cueId", description = "큐시트 아이디")
                                            @RequestParam(value = "cueId", required = false) Long cueId,
                                            @RequestHeader(value = "securityKey") String securityKey) {

        log.info("PS Taker Find : cueId - " + cueId);

        CueSheetDTO cueSheetDTO = cueSheetService.psFind(cueId);

        return new AnsApiResponse<>(cueSheetDTO);
    }

    /*@Operation(summary = "테이커 방송중 상태 업데이트[ on_air ]", description = "테이커 방송중 상태 업데이트[ on_air ]")
    @GetMapping(path = "/cuestcdupdate")
    public String cueStCdUpdate(@Parameter(name = "rd_id", description = "프로그램 아이디")
                                @RequestParam(value = "rd_id", required = false) Long rd_id,
                                @Parameter(name = "cue_st_cd", description = "방송상태 코드 [ on_air : 방송중]")
                                @RequestParam(value = "cue_st_cd", required = false) String cue_st_cd,
                                @RequestHeader(value = "securityKey") String securityKey) {

        log.info("Taker CueSheet State Code Update : rd_id - " + rd_id + " cue_st_cd : " + cue_st_cd);

        ParentProgramDTO parentProgramDTO = interfaceService.cueStCdUpdate(rd_id, cue_st_cd);

        String takerCueSheetDTO = interfaceService.takerPgmToXmlOne(parentProgramDTO);

        return takerCueSheetDTO;
    }*/

    @Operation(summary = "테이커 방송중 상태 업데이트[ on_air, end_on_air ]", description = "테이커 방송중 상태 업데이트[ on_air, end_on_air ]")
    @PutMapping(path = "/cuestcdupdate/{rd_id}")
    public String cueStCdUpdate(@Parameter(name = "rd_id", required = true, description = "프로그램 아이디")
                                @PathVariable("rd_id") Long rd_id,
                                @Parameter(description = "필수값<br> on_air ", required = true)
                                @RequestBody @Valid TakerCdUpdateDTO takerCdUpdateDTO,
                                @RequestHeader(value = "securityKey") String securityKey) throws JsonProcessingException {

        log.info("Taker CueSheet State Code Update : rd_id - " + rd_id + " cue_st_cd : " + takerCdUpdateDTO.toString());

        ParentProgramDTO parentProgramDTO = interfaceService.cueStCdUpdate(rd_id, takerCdUpdateDTO);

        String takerCueSheetDTO = interfaceService.takerPgmToXmlOne(parentProgramDTO);

        return takerCueSheetDTO;
    }

    @Operation(summary = "영상 전송 상태 업데이트", description = "영상 전송 상태 업데이트")
    @PutMapping(path = "/mediatransfer/updatestate")
    public AnsApiResponse<?> stateChange(@Parameter(description = "필수값<br> lckYn ", required = true)
                                         @RequestBody @Valid MediaTransferDTO mediaTransferDTO,
                                         @RequestHeader(value = "securityKey") String securityKey) {

        //콘텐츠 아이디로 찾은 정보가 있으면 처리 [ 무조건 성공으로 넘어간다. ]
        log.info("Media State Update : ContentId : " + mediaTransferDTO.getContentId() + " Code : " + mediaTransferDTO.getTrnsfStCd() + " val : "
                + mediaTransferDTO.getTrnasfVal());

        interfaceService.stateChange(mediaTransferDTO);
        return new AnsApiResponse<>("complete");
    }

    @Operation(summary = "방송중 테이커 큐시트 동기화", description = "방송중 테이커 큐시트 동기화")
    @PostMapping(path = "/takersetcue")
    public AnsApiResponse<?> takerSetCue(@Parameter(description = "필수값<br> ", required = true)
                                         @RequestBody @Valid TakerToCueBodyDTO takerToCueBodyDTO,
                                         @RequestHeader(value = "securityKey") String securityKey) throws JsonProcessingException {

        log.info("Taker On Air status  : rd_id - " + takerToCueBodyDTO.toString());

        interfaceService.takerSetCue(takerToCueBodyDTO);

        return AnsApiResponse.ok();
    }

   /* @Operation(summary = "방송중 테이커 큐시트 동기화 test", description = "방송중 테이커 큐시트 동기화 test")
    @PostMapping(path = "/takersetcuetest")
    public AnsApiResponse<?> takerSetCueTest(@Parameter(description = "필수값<br> ", required = true)
                                         @RequestBody @Valid TakerToCueBodyDTO takerToCueBodyDTO,
                                         @RequestHeader(value = "securityKey") String securityKey) throws JsonProcessingException {

        log.info("Taker On Air status  : rd_id - " + takerToCueBodyDTO.toString());

        interfaceService.takerSetCue(takerToCueBodyDTO);

        return AnsApiResponse.ok();
    }*/

    @Operation(summary = "S MAM 큐시트 목록조회", description = "S MAM 큐시트 목록조회")
    @GetMapping(path = "/smamfindallcue")
    public AnsApiResponse<List<CueSheetDTO>> smamFindAllCue(@Parameter(description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
                                                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                            @Parameter(description = "검색 종료 날짜(yyyy-MM-dd)", required = false)
                                                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                            @Parameter(name = "brdcPgmId", description = "프로그램 아이디")
                                                            @RequestParam(value = "brdcPgmId", required = false) String brdcPgmId,
                                                            @Parameter(name = "brdcPgmNm", description = "프로그램 명")
                                                            @RequestParam(value = "brdcPgmNm", required = false) String brdcPgmNm,
                                                            @Parameter(name = "deptCd", description = "부서 코드")
                                                            @RequestParam(value = "deptCd", required = false) Integer deptCd,
                                                            @Parameter(name = "searchWord", description = "검색키워드")
                                                            @RequestParam(value = "searchWord", required = false) String searchWord,
                                                            @RequestHeader(value = "securityKey") String securityKey) throws Exception {

        List<CueSheetDTO> cueSheetDTOList = new ArrayList<>();


        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            //검색날짜 시간설정 (검색시작 Date = yyyy-MM-dd 00:00:00 / 검색종료 Date yyyy-MM-dd 24:00:00)
            SearchDate searchDate = new SearchDate(sdate, edate);
            cueSheetDTOList = cueSheetService.takerFindAll(searchDate.getStartDate(), searchDate.getEndDate(),
                    brdcPgmId, brdcPgmNm, deptCd, searchWord);

        } else {
            cueSheetDTOList = cueSheetService.takerFindAll(null, null, brdcPgmId, brdcPgmNm, deptCd, searchWord);
        }



        return new AnsApiResponse<>(cueSheetDTOList);
    }

    @Operation(summary = "S MAM 큐시트 상세조회", description = "S MAM 큐시트 상세조회")
    @GetMapping(path = "/smamfindcue")
    public AnsApiResponse<CueSheetDTO> smamFindCue(@Parameter(name = "cueId", description = "큐시트 아이디")
                                                   @RequestParam(value = "cueId", required = false) Long cueId,
                                                   @Parameter(name = "articleYn", description = "기사 항목만 검색 Y, 전부 N")
                                                   @RequestParam(value = "articleYn", required = false) String articleYn,
                                                   @RequestHeader(value = "securityKey") String securityKey) {

        CueSheetDTO cueSheetDTO = cueSheetService.find(cueId);

        if ("Y".equals(articleYn)){

            cueSheetDTO = interfaceService.getCueSheetItemArticle(cueSheetDTO);
        }

        return new AnsApiResponse<>(cueSheetDTO);
    }
}
