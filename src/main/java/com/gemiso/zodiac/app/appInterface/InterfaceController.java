package com.gemiso.zodiac.app.appInterface;

import com.gemiso.zodiac.app.appInterface.codeDTO.TakerCodeDTO;
import com.gemiso.zodiac.app.appInterface.mediaTransferDTO.MediaTransferDTO;
import com.gemiso.zodiac.app.appInterface.prompterCueDTO.PrompterCueSheetDataDTO;
import com.gemiso.zodiac.app.appInterface.prompterProgramDTO.PrompterProgramDTO;
import com.gemiso.zodiac.app.appInterface.takerCueFindAllDTO.TakerCueSheetDataDTO;
import com.gemiso.zodiac.app.appInterface.takerCueRefreshDTO.TakerCueRefreshDataDTO;
import com.gemiso.zodiac.app.appInterface.takerCueRefreshDTO.TakerSpareCueRefreshDataDTO;
import com.gemiso.zodiac.app.appInterface.takerProgramDTO.ParentProgramDTO;
import com.gemiso.zodiac.app.appInterface.takerUpdateDTO.TakerCdUpdateDTO;
import com.gemiso.zodiac.app.appInterface.takerUpdateDTO.TakerToCueBodyDTO;
import com.gemiso.zodiac.app.article.ArticleService;
import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import com.gemiso.zodiac.app.code.CodeService;
import com.gemiso.zodiac.app.code.dto.CodeDTO;
import com.gemiso.zodiac.app.cueSheet.CueSheetService;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetDTO;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetFindAllDTO;
import com.gemiso.zodiac.app.user.UserService;
import com.gemiso.zodiac.app.user.dto.UserDTO;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.helper.SearchDateInterface;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.gemiso.zodiac.exception.InterfaceException;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(description = "?????? ??????????????? API")
@RestController
@RequestMapping(path = "/interface")
@Slf4j
@RequiredArgsConstructor
public class InterfaceController {

    private final InterfaceService interfaceService;

    private final CueSheetService cueSheetService;
    private final CodeService codeService;
    private final UserService userService;
    private final ArticleService articleService;
    //private final CueSheetItemService cueSheetItemService;


    @Operation(summary = "????????? ????????????[Taker]", description = "????????? ???????????? ????????????[Taker]")
    @GetMapping(path = "/dailypgm")
    public String dailyPgmFindAll(@Parameter(name = "brdc_pgm_id", description = "???????????? ?????????")
                                  @RequestParam(value = "brdc_pgm_id", required = false) String brdc_pgm_id,
                                  @Parameter(name = "pgm_nm", description = "???????????? ???")
                                  @RequestParam(value = "pgm_nm", required = false) String pgm_nm,
                                  @Parameter(description = "?????? ?????? ????????? ??????(yyyy-MM-dd)", required = false)
                                  @DateTimeFormat(pattern = "yyyy-MM-dd") String sdate,
                                  @Parameter(description = "?????? ?????? ??????(yyyy-MM-dd)", required = false)
                                  @DateTimeFormat(pattern = "yyyy-MM-dd") String edate,
                                  @Parameter(name = "brdc_pgm_div_cd", description = "???????????? ?????? ??????")
                                  @RequestParam(value = "brdc_pgm_div_cd", required = false) String brdc_pgm_div_cd,
                                  @Parameter(name = "del_yn", description = "????????????")
                                  @RequestParam(value = "del_yn", required = false) String del_yn,
                                  @Parameter(name = "ch_div_cd", description = "???????????? ??????")
                                  @RequestParam(value = "ch_div_cd", required = false) String ch_div_cd,
                                  @Parameter(name = "usr_id", description = "????????? ?????????")
                                  @RequestParam(value = "usr_id", required = false) String usr_id,
                                  @Parameter(name = "token", description = "?????????(?????? ????????????)")
                                  @RequestParam(value = "token", required = false) String token,
                                  @Parameter(name = "usr_ip", description = "????????? ?????????")
                                  @RequestParam(value = "usr_ip", required = false) String usr_ip,
                                  @Parameter(name = "format", description = "?????? ?????? XML")
                                  @RequestParam(value = "format", required = false) String format,
                                  @Parameter(name = "lang", description = "????????? ?????? KO")
                                  @RequestParam(value = "lang", required = false) String lang,
                                  @Parameter(name = "os_type", description = "os?????? SCU")
                                  @RequestParam(value = "os_type", required = false) String os_type,
                                  @RequestHeader(value = "securityKey") String securityKey) throws Exception {

        log.info("Taker FindAll : brdc_pgm_id - " + brdc_pgm_id + " pgm_nm -" + pgm_nm);

        List<ParentProgramDTO> parentProgramDTOList = new ArrayList<>();
        String takerCueSheetDTO = "";

        try {

            //????????? ??????????????? ????????? ??????
            if (sdate != null && sdate.trim().isEmpty() == false && edate != null && edate.trim().isEmpty() == false) {

                //???????????? ???????????? (???????????? Date = yyyy-MM-dd 00:00:00 / ???????????? Date yyyy-MM-dd 23:59:59)
                SearchDateInterface searchDate = new SearchDateInterface(sdate, edate);
                parentProgramDTOList = interfaceService.dailyPgmFindAll(searchDate.getStartDate(), searchDate.getEndDate(), brdc_pgm_id, pgm_nm);

                takerCueSheetDTO = interfaceService.takerPgmToXml(parentProgramDTOList);
            } else { //????????? ??????????????? ???????????? ?????? ??????
                parentProgramDTOList = interfaceService.dailyPgmFindAll(null, null, brdc_pgm_id, pgm_nm);

                takerCueSheetDTO = interfaceService.takerPgmToXml(parentProgramDTOList);
            }

            return takerCueSheetDTO;

        } catch (IOException | RuntimeException e) {

            throw new InterfaceException("????????? ????????????[Taker] ERROR ");

        }
    }

    /*@Operation(summary = "????????? ????????????[Taker]", description = "????????? ????????????[Taker]")
    @GetMapping(path = "/cuesheet/{cueId}")
    public AnsApiResponse<CueSheetDTO> cueFind(@Parameter(name = "cueId", description = "????????? ?????????")
                                            @PathVariable("cueId") Long cueId,
                                            @RequestHeader(value = "securityKey") String securityKey) {


        CueSheetDTO cueSheetDTO = interfaceService.cueFind(cueId);

        return new AnsApiResponse<>(cueSheetDTO);
    }

    @Operation(summary = "????????? ????????? ????????????[Taker]", description = "????????? ????????? ????????????[Taker]")
    @GetMapping(path = "/cuesheet/{cueId}/cuesheetitems")
    public AnsApiResponse<List<CueSheetItemDTO>> cueItemFindAll(@Parameter(name = "artlcId", description = "?????? ?????????")
                                                             @RequestParam(value = "artlcId", required = false) Long artlcId,
                                                             @Parameter(name = "cueId", description = "????????? ?????????")
                                                             @PathVariable("cueId") Long cueId,
                                                             @RequestHeader(value = "securityKey") String securityKey) {

        List<CueSheetItemDTO> cueSheetItemDTOList = interfaceService.cueItemFindAll(artlcId, cueId);

        return new AnsApiResponse<>(cueSheetItemDTOList);
    }*/

    @Operation(summary = "????????? ????????????[Taker]", description = "????????? ????????????[Taker]")
    @GetMapping(path = "/cuesheet")
    public String cueFindAll(@Parameter(name = "rd_id", description = "???????????? ?????????")
                             @RequestParam(value = "rd_id", required = false) String rd_id,
                             @Parameter(name = "play_seq", description = "????????? ???????????")
                             @RequestParam(value = "play_seq", required = false) String play_seq,
                             @Parameter(name = "cued_seq", description = "????????? ????????????")
                             @RequestParam(value = "cued_seq", required = false) String cued_seq,
                             @Parameter(name = "vplay_seq", description = "v????????? ????????????")
                             @RequestParam(value = "vplay_seq", required = false) String vplay_seq,
                             @Parameter(name = "vcued_seq", description = "v????????? ????????????")
                             @RequestParam(value = "vcued_seq", required = false) String vcued_seq,
                             @Parameter(name = "del_yn", description = "????????????")
                             @RequestParam(value = "del_yn", required = false) String del_yn,
                             @Parameter(name = "ch_div_cd", description = "?????? ?????? ??????")
                             @RequestParam(value = "ch_div_cd", required = false) String ch_div_cd,
                             @Parameter(name = "usr_id", description = "????????? ?????????")
                             @RequestParam(value = "usr_id", required = false) String usr_id,
                             @Parameter(name = "token", description = "?????????(?????? ????????????)")
                             @RequestParam(value = "token", required = false) String token,
                             @Parameter(name = "usr_ip", description = "????????? ?????????")
                             @RequestParam(value = "usr_ip", required = false) String usr_ip,
                             @Parameter(name = "format", description = "?????? ?????? XML")
                             @RequestParam(value = "format", required = false) String format,
                             @Parameter(name = "lang", description = "????????? ?????? KO")
                             @RequestParam(value = "lang", required = false) String lang,
                             @Parameter(name = "os_type", description = "os?????? SCU")
                             @RequestParam(value = "os_type", required = false) String os_type,
                             @RequestHeader(value = "securityKey") String securityKey) {

        log.info("Taker Find : rd_id - " + rd_id);
        String takerCue = "";

        try {

            TakerCueSheetDataDTO takerCueSheetDataDTO = interfaceService.cuefindAll(rd_id, play_seq, cued_seq, vplay_seq, vcued_seq, del_yn,
                    ch_div_cd, usr_id, token, usr_ip, format, lang, os_type);

            takerCue = interfaceService.takerCueToXml(takerCueSheetDataDTO);

        } catch (RuntimeException e) {

            throw new InterfaceException("????????? ????????????[Taker] ERROR ");

        }
        return takerCue;
    }

    @Operation(summary = "????????? ????????? ????????? Refresh", description = "????????? ????????? ????????? Refresh")
    @GetMapping(path = "/takerrefresh")
    public String takerCueItemRefresh(@Parameter(name = "rd_id", description = "???????????? ?????????")
                                      @RequestParam(value = "rd_id", required = false) Long rd_id,
                                      @Parameter(name = "rd_seq", description = "????????? ????????? ?????????")
                                      @RequestParam(value = "rd_seq", required = false) Integer rd_seq,
                                      @Parameter(name = "spare_yn", description = "????????? ?????????(N, Y)")
                                      @RequestParam(value = "spare_yn", required = false) String spare_yn,
                                      @RequestHeader(value = "securityKey") String securityKey) {

        log.info("Taker Refresh : rd_id - " + rd_id + " rd_seq - " + rd_seq + " spare_yn - " + spare_yn);

        String returnData = "";

        try {


            if ("N".equals(spare_yn)) {

                TakerCueRefreshDataDTO takerCueSheetDTO = interfaceService.takerCueItemRefresh(rd_id, rd_seq);

                returnData = interfaceService.takerCueRefresh(takerCueSheetDTO);

            } else {

                TakerSpareCueRefreshDataDTO takerSpareCueSheetDTO = interfaceService.takerSpareCueItemRefresh(rd_id, rd_seq);

                returnData = interfaceService.takerSpareCueRefresh(takerSpareCueSheetDTO);

            }

        } catch (RuntimeException e) {

            throw new InterfaceException("????????? ????????? ????????? Refresh ERROR ");

        }
        return returnData;
    }

    @Operation(summary = "?????????????????? ??????[Taker]", description = "?????????????????? ??????[Taker]")
    @GetMapping(path = "/code")
    public String codeFindAll(@Parameter(name = "key", description = "??? ??")
                              @RequestParam(value = "key", required = false) String key,
                              @Parameter(name = "ch_div_cd", description = "?????? ?????? ??????")
                              @RequestParam(value = "ch_div_cd", required = false) String ch_div_cd,
                              @Parameter(name = "usr_id", description = "????????? ?????????")
                              @RequestParam(value = "usr_id", required = false) String usr_id,
                              @Parameter(name = "token", description = "?????????")
                              @RequestParam(value = "token", required = false) String token,
                              @Parameter(name = "usr_ip", description = "????????? ?????????")
                              @RequestParam(value = "usr_ip", required = false) String usr_ip,
                              @Parameter(name = "format", description = "?????? ?????? XML")
                              @RequestParam(value = "format", required = false) String format,
                              @Parameter(name = "lang", description = "????????? ?????? KO")
                              @RequestParam(value = "lang", required = false) String lang,
                              @Parameter(name = "os_type", description = "os?????? SCU")
                              @RequestParam(value = "os_type", required = false) String os_type,
                              @RequestHeader(value = "securityKey") String securityKey) {

        String takerCode = "";
        try {


            TakerCodeDTO takerCodeDTO = interfaceService.codeFindAll(key, ch_div_cd, usr_id, token, usr_ip, format, lang, os_type);

            takerCode = interfaceService.codeToTakerCodeXml(takerCodeDTO);

        } catch (RuntimeException e) {

            throw new InterfaceException("?????????????????? ??????[Taker] ERROR ");

        }
        return takerCode;
    }

    @Operation(summary = "?????????????????? ??????[Taker]", description = "?????????????????? ??????[Taker]")
    @GetMapping(path = "/mediatransrate")
    public String mediaTransRateFindAll(@Parameter(name = "media_cd", description = "????????? ?????????")
                                        @RequestParam(value = "media_cd", required = false) String media_cd,
                                        @Parameter(name = "rd_id", description = "????????? ????????????")
                                        @RequestParam(value = "rd_id", required = false) String rd_id,
                                        @Parameter(name = "rd_seq", description = "????????????")
                                        @RequestParam(value = "rd_seq", required = false) String rd_seq,
                                        @Parameter(name = "plyout_id", description = "???????????? ????????????")
                                        @RequestParam(value = "plyout_id", required = false) String plyout_id,
                                        @Parameter(name = "ch_div_cd", description = "?????? ?????? ??????")
                                        @RequestParam(value = "ch_div_cd", required = false) String ch_div_cd,
                                        @Parameter(name = "usr_id", description = "????????? ?????????")
                                        @RequestParam(value = "usr_id", required = false) String usr_id,
                                        @Parameter(name = "token", description = "?????????")
                                        @RequestParam(value = "token", required = false) String token,
                                        @Parameter(name = "usr_ip", description = "????????? ?????????")
                                        @RequestParam(value = "usr_ip", required = false) String usr_ip,
                                        @Parameter(name = "format", description = "?????? ?????? XML")
                                        @RequestParam(value = "format", required = false) String format,
                                        @Parameter(name = "lang", description = "????????? ?????? KO")
                                        @RequestParam(value = "lang", required = false) String lang,
                                        @Parameter(name = "os_type", description = "os?????? SCU")
                                        @RequestParam(value = "os_type", required = false) String os_type,
                                        @RequestHeader(value = "securityKey") String securityKey) {

        return null;
    }

    @Operation(summary = "???????????? ???????????? ????????????", description = "???????????? ???????????? ????????????")
    @GetMapping(path = "/getmstlistservice")
    public String getMstListService(@Parameter(name = "media_id", description = "????????? ????????????")
                                    @RequestParam(value = "media_id", required = false) String media_id,
                                    @Parameter(name = "pro_id", description = "???????????? ????????????")
                                    @RequestParam(value = "pro_id", required = false) String pro_id,
                                    @Parameter(name = "sdate", description = "?????? ???????????????")
                                    @RequestParam(value = "sdate", required = false) String sdate,
                                    @Parameter(name = "fdate", description = "?????? ???????????????")
                                    @RequestParam(value = "fdate", required = false) String fdate,
                                    @Parameter(name = "usr_id", description = "????????? ????????????")
                                    @RequestParam(value = "usr_id", required = false) String usr_id,
                                    @RequestHeader(value = "securityKey") String securityKey) throws Exception {


        log.info("Prompter FindAll : Start - " + sdate + " End - " + fdate + " pro_id - " + pro_id);

        List<PrompterProgramDTO> prompterProgramDTOList = new ArrayList<>();
        String prompterProgram = "";

        try {

            //????????? ??????????????? ????????? ??????
            if (sdate != null && sdate.trim().isEmpty() == false && fdate != null && fdate.trim().isEmpty() == false) {

                //???????????? ???????????? (???????????? Date = yyyy-MM-dd 00:00:00 / ???????????? Date yyyy-MM-dd 23:59:59)
                SearchDateInterface searchDate = new SearchDateInterface(sdate, fdate);

                prompterProgramDTOList = interfaceService.getMstListService(pro_id, searchDate.getStartDate(), searchDate.getEndDate());

                prompterProgram = interfaceService.prompterProgramToXml(prompterProgramDTOList);
            } else {
                prompterProgramDTOList = interfaceService.getMstListService(pro_id, null, null);

                prompterProgram = interfaceService.prompterProgramToXml(prompterProgramDTOList);
            }

        } catch (IOException | RuntimeException e) {

            throw new InterfaceException("???????????? ???????????? ???????????? ERROR ");

        }

        return prompterProgram;
    }

    @Operation(summary = "???????????? ????????? ????????????", description = "???????????? ????????? ????????????")
    @GetMapping(path = "/getcuesheetservice")
    public String getCuesheetService(@Parameter(name = "cs_id", description = "????????? ????????????")
                                     @RequestParam(value = "cs_id", required = false) Long cs_id,
                                     @Parameter(name = "usr_id", description = "????????? ????????????")
                                     @RequestParam(value = "usr_id", required = false) String usr_id,
                                     @Parameter(name = "user_ip", description = "????????? ????????????")
                                     @RequestParam(value = "user_ip", required = false) String user_ip,
                                     @RequestHeader(value = "securityKey") String securityKey) {

        log.info("Prompter Find : cs_id - " + cs_id);

        String prompterCueSheetXml = "";
        try {

            //set Lsit<PrompterCueRefreshDTO>
            PrompterCueSheetDataDTO prompterCueSheetDataDTO = interfaceService.getCuesheetService(cs_id);

            prompterCueSheetXml = interfaceService.prompterCueSheetXml(prompterCueSheetDataDTO);

        } catch (RuntimeException e) {

            throw new InterfaceException("???????????? ????????? ???????????? ERROR ");

        }
        return prompterCueSheetXml;
    }

    @Operation(summary = "???????????? ????????? ????????????[ ??????, ?????? Base64 ????????? ] ", description = "???????????? ????????? ????????????[ ??????, ?????? Base64 ????????? ]")
    @GetMapping(path = "/getcuesheetservice/prompter")
    public String getCuesheetServicePrompter(@Parameter(name = "cs_id", description = "????????? ????????????")
                                     @RequestParam(value = "cs_id", required = false) Long cs_id,
                                     @Parameter(name = "usr_id", description = "????????? ????????????")
                                     @RequestParam(value = "usr_id", required = false) String usr_id,
                                     @Parameter(name = "user_ip", description = "????????? ????????????")
                                     @RequestParam(value = "user_ip", required = false) String user_ip,
                                     @RequestHeader(value = "securityKey") String securityKey) {

        log.info("Prompter Find : cs_id - " + cs_id);

        String prompterCueSheetXml = "";
        try {

            //set Lsit<PrompterCueRefreshDTO>
            PrompterCueSheetDataDTO prompterCueSheetDataDTO = interfaceService.getCuesheetServiceIncoding(cs_id);

            prompterCueSheetXml = interfaceService.prompterCueSheetXml(prompterCueSheetDataDTO);

        } catch (RuntimeException e) {

            throw new InterfaceException("???????????? ????????? ????????????[ Base64 ????????? ] ERROR ");

        }
        return prompterCueSheetXml;
    }

    //?????? ???????????? ???????????? ?????????x ????????? ?????? (2022.03.15)
    /*@Operation(summary = "???????????? ????????? ?????? ????????????", description = "???????????? ????????? ?????? ????????????")
    @GetMapping(path = "/prompter_refresh")
    public String prompterRefresh(@Parameter(name = "rd_id", description = "????????? ????????? ?????????")
                                  @RequestParam(value = "rd_id", required = false) Long rd_id) {

        PrompterCueRefreshDTO prompterCueRefreshDTO = interfaceService.prompterRefresh(rd_id);

    }*/

    @Operation(summary = "????????? ????????????[PS TAKER]", description = "????????? ????????????[PS TAKER]")
    @GetMapping(path = "/pstakerlist")
    public AnsApiResponse<CueSheetFindAllDTO> findAll(@Parameter(description = "?????? ?????? ????????? ??????(yyyy-MM-dd)", required = false)
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                      @Parameter(description = "?????? ?????? ??????(yyyy-MM-dd)", required = false)
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                      @Parameter(name = "brdcPgmId", description = "???????????? ?????????")
                                                      @RequestParam(value = "brdcPgmId", required = false) String brdcPgmId,
                                                      @Parameter(name = "brdcPgmNm", description = "???????????? ???")
                                                      @RequestParam(value = "brdcPgmNm", required = false) String brdcPgmNm,
                                                      @Parameter(name = "searchWord", description = "???????????????")
                                                      @RequestParam(value = "searchWord", required = false) String searchWord,
                                                      @RequestHeader(value = "securityKey") String securityKey) throws Exception {

        log.info("PS Taker FindAll : Start - " + sdate + " End - " + edate + " brdcPgmId - " + brdcPgmId +
                " brdcPgmNm - " + brdcPgmNm + " searchWord - " + searchWord);

        CueSheetFindAllDTO cueSheetFindAllDTO = new CueSheetFindAllDTO();


        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            //???????????? ???????????? (???????????? Date = yyyy-MM-dd 00:00:00 / ???????????? Date yyyy-MM-dd 24:00:00)
            SearchDate searchDate = new SearchDate(sdate, edate);
            cueSheetFindAllDTO = cueSheetService.psTakerFindAll(searchDate.getStartDate(), searchDate.getEndDate(), brdcPgmId, brdcPgmNm, searchWord);

        } else {
            cueSheetFindAllDTO = cueSheetService.psTakerFindAll(null, null, brdcPgmId, brdcPgmNm, searchWord);
        }

        return new AnsApiResponse<>(cueSheetFindAllDTO);
    }

    @Operation(summary = "????????? ????????????[PS TAKER]", description = "????????? ????????????[PS TAKER]")
    @GetMapping(path = "/pstaker")
    public AnsApiResponse<CueSheetDTO> find(@Parameter(name = "cueId", description = "????????? ?????????")
                                            @RequestParam(value = "cueId", required = false) Long cueId,
                                            @RequestHeader(value = "securityKey") String securityKey) {

        log.info("PS Taker Find : cueId - " + cueId);

        CueSheetDTO cueSheetDTO = cueSheetService.psFind(cueId);

        return new AnsApiResponse<>(cueSheetDTO);
    }

    /*@Operation(summary = "????????? ????????? ?????? ????????????[ on_air ]", description = "????????? ????????? ?????? ????????????[ on_air ]")
    @GetMapping(path = "/cuestcdupdate")
    public String cueStCdUpdate(@Parameter(name = "rd_id", description = "???????????? ?????????")
                                @RequestParam(value = "rd_id", required = false) Long rd_id,
                                @Parameter(name = "cue_st_cd", description = "???????????? ?????? [ on_air : ?????????]")
                                @RequestParam(value = "cue_st_cd", required = false) String cue_st_cd,
                                @RequestHeader(value = "securityKey") String securityKey) {

        log.info("Taker CueSheet State Code Update : rd_id - " + rd_id + " cue_st_cd : " + cue_st_cd);

        ParentProgramDTO parentProgramDTO = interfaceService.cueStCdUpdate(rd_id, cue_st_cd);

        String takerCueSheetDTO = interfaceService.takerPgmToXmlOne(parentProgramDTO);

        return takerCueSheetDTO;
    }*/

    @Operation(summary = "????????? ????????? ?????? ????????????[ on_air, end_on_air ]", description = "????????? ????????? ?????? ????????????[ on_air, end_on_air ]")
    @PutMapping(path = "/cuestcdupdate/{rd_id}")
    public String cueStCdUpdate(@Parameter(name = "rd_id", required = true, description = "???????????? ?????????")
                                @PathVariable("rd_id") Long rd_id,
                                @Parameter(description = "?????????<br> on_air ", required = true)
                                @RequestBody @Valid TakerCdUpdateDTO takerCdUpdateDTO,
                                @RequestHeader(value = "securityKey") String securityKey) throws Exception {

        log.info("Taker CueSheet State Code Update : rd_id - " + rd_id + " cue_st_cd : " + takerCdUpdateDTO.toString());
        String takerCueSheetDTO = "";
        try {


            ParentProgramDTO parentProgramDTO = interfaceService.cueStCdUpdate(rd_id, takerCdUpdateDTO);

            takerCueSheetDTO = interfaceService.takerPgmToXmlOne(parentProgramDTO);

        } catch (IOException | RuntimeException e) {

            throw new InterfaceException("????????? ????????? ?????? ???????????? ERROR ");

        }

        return takerCueSheetDTO;
    }

    @Operation(summary = "?????? ?????? ?????? ????????????", description = "?????? ?????? ?????? ????????????")
    @PutMapping(path = "/mediatransfer/updatestate")
    public AnsApiResponse<?> stateChange(@Parameter(description = "?????????<br> lckYn ", required = true)
                                         @RequestBody @Valid MediaTransferDTO mediaTransferDTO,
                                         @RequestHeader(value = "securityKey") String securityKey) throws Exception {

        //????????? ???????????? ?????? ????????? ????????? ?????? [ ????????? ???????????? ????????????. ]
        log.info("Media State Update : ContentId : " + mediaTransferDTO.getContentId() + " Code : " + mediaTransferDTO.getTrnsfStCd() + " val : "
                + mediaTransferDTO.getTrnasfVal());

        interfaceService.stateChange(mediaTransferDTO);
        return new AnsApiResponse<>("complete");
    }

    /*@Operation(summary = "????????? ????????? ????????? ?????????", description = "????????? ????????? ????????? ?????????")
    @PostMapping(path = "/takersetcue")
    public AnsApiResponse<?> takerSetCue(@Parameter(description = "?????????<br> ", required = true)
                                         @RequestBody @Valid TakerToCueBodyDTO takerToCueBodyDTO,
                                         @RequestHeader(value = "securityKey") String securityKey) throws Exception {

        log.info("Taker On Air status  : rd_id - " + takerToCueBodyDTO.toString());

        interfaceService.takerSetCue(takerToCueBodyDTO);

        return AnsApiResponse.ok();
    }*/

    /*@Operation(summary = "????????? ????????? ????????? ?????????( ?????? )", description = "????????? ????????? ????????? ?????????( ?????? )")
    @PostMapping(path = "/takersetcue")
    public AnsApiResponse<?> takerSetCue2(@Parameter(description = "?????????<br> ", required = true)
                                         @RequestBody @Valid TakerToCueBody2DTO takerToCueBodyDTO,
                                         @RequestHeader(value = "securityKey") String securityKey) throws JsonProcessingException {

        log.info("Taker On Air status  : rd_id - " + takerToCueBodyDTO.toString());

        interfaceService.takerSetCue(takerToCueBodyDTO);

        return AnsApiResponse.ok();
    }*/

    @Operation(summary = "????????? ????????? ????????? ?????????( ?????? )", description = "????????? ????????? ????????? ?????????( ?????? )")
    @PostMapping(path = "/takersetcue")
    public AnsApiResponse<?> takerSetCue(@Parameter(description = "?????????<br> ", required = true)
                                         @RequestBody @Valid TakerToCueBodyDTO takerToCueBodyDTO,
                                         @RequestHeader(value = "securityKey") String securityKey) throws Exception {

        log.info("Taker On Air status  : rd_id - " + takerToCueBodyDTO.toString());

        try {


            interfaceService.takerSetCue2(takerToCueBodyDTO);

        } catch (IOException | RuntimeException e) {

            throw new InterfaceException("????????? ????????? ????????? ????????? ERROR ");

        }
        return AnsApiResponse.ok();
    }

    @Operation(summary = "????????? ???????????? ????????? ?????????", description = "????????? ???????????? ????????? ?????????")
    @PostMapping(path = "/promptersetcue")
    public AnsApiResponse<?> prompterSetCue(@Parameter(description = "?????????<br> ", required = true)
                                         @RequestBody @Valid TakerToCueBodyDTO takerToCueBodyDTO,
                                         @RequestHeader(value = "securityKey") String securityKey) throws Exception {

        log.info("Prompter On Air status  : rd_id - " + takerToCueBodyDTO.toString());

        try {


            interfaceService.prompterSetCue(takerToCueBodyDTO);

        } catch (IOException | RuntimeException e) {

            throw new InterfaceException("????????? ????????? ????????? ????????? ERROR ");

        }
        return AnsApiResponse.ok();
    }

   /* @Operation(summary = "????????? ????????? ????????? ????????? test", description = "????????? ????????? ????????? ????????? test")
    @PostMapping(path = "/takersetcuetest")
    public AnsApiResponse<?> takerSetCueTest(@Parameter(description = "?????????<br> ", required = true)
                                         @RequestBody @Valid TakerToCueBodyDTO takerToCueBodyDTO,
                                         @RequestHeader(value = "securityKey") String securityKey) throws JsonProcessingException {

        log.info("Taker On Air status  : rd_id - " + takerToCueBodyDTO.toString());

        interfaceService.takerSetCue(takerToCueBodyDTO);

        return AnsApiResponse.ok();
    }*/

    @Operation(summary = "S MAM ????????? ????????????", description = "S MAM ????????? ????????????")
    @GetMapping(path = "/smamfindallcue")
    public AnsApiResponse<List<CueSheetDTO>> smamFindAllCue(@Parameter(description = "?????? ?????? ????????? ??????(yyyy-MM-dd)", required = false)
                                                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                            @Parameter(description = "?????? ?????? ??????(yyyy-MM-dd)", required = false)
                                                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                            @Parameter(name = "brdcPgmId", description = "???????????? ?????????")
                                                            @RequestParam(value = "brdcPgmId", required = false) String brdcPgmId,
                                                            @Parameter(name = "brdcPgmNm", description = "???????????? ???")
                                                            @RequestParam(value = "brdcPgmNm", required = false) String brdcPgmNm,
                                                            @Parameter(name = "deptCd", description = "?????? ??????")
                                                            @RequestParam(value = "deptCd", required = false) Long deptCd,
                                                            @Parameter(name = "searchWord", description = "???????????????")
                                                            @RequestParam(value = "searchWord", required = false) String searchWord,
                                                            @RequestHeader(value = "securityKey") String securityKey) throws Exception {

        List<CueSheetDTO> cueSheetDTOList = new ArrayList<>();


        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            //???????????? ???????????? (???????????? Date = yyyy-MM-dd 00:00:00 / ???????????? Date yyyy-MM-dd 24:00:00)
            SearchDate searchDate = new SearchDate(sdate, edate);
            cueSheetDTOList = cueSheetService.takerFindAll(searchDate.getStartDate(), searchDate.getEndDate(),
                    brdcPgmId, brdcPgmNm, deptCd, searchWord);

        } else {
            cueSheetDTOList = cueSheetService.takerFindAll(null, null, brdcPgmId, brdcPgmNm, deptCd, searchWord);
        }


        return new AnsApiResponse<>(cueSheetDTOList);
    }

    @Operation(summary = "S MAM ????????? ????????????", description = "S MAM ????????? ????????????")
    @GetMapping(path = "/smamfindcue")
    public AnsApiResponse<CueSheetDTO> smamFindCue(@Parameter(name = "cueId", description = "????????? ?????????")
                                                   @RequestParam(value = "cueId", required = false) Long cueId,
                                                   @Parameter(name = "articleYn", description = "?????? ????????? ?????? Y, ?????? N")
                                                   @RequestParam(value = "articleYn", required = false) String articleYn,
                                                   @RequestHeader(value = "securityKey") String securityKey) {

        CueSheetDTO cueSheetDTO = cueSheetService.find(cueId);

        if ("Y".equals(articleYn)) {

            cueSheetDTO = interfaceService.getCueSheetItemArticle(cueSheetDTO);
        }

        return new AnsApiResponse<>(cueSheetDTO);
    }

    @Operation(summary = "???????????? ??????( ???????????? ?????? ) ??????", description = "???????????? ??????( ???????????? ?????? ) ??????")
    @GetMapping(path = "/homepagecd")
    public AnsApiResponse<List<CodeDTO>> homePageCd(@Parameter(name = "hrnkCdId", description = "???????????? ?????? ??????( value : categories )")
                                                    @RequestParam(value = "hrnkCdId", required = false) String hrnkCdId,
                                                    @RequestHeader(value = "securityKey") String securityKey) {


        List<String> hrnkCdIds = new ArrayList<>();
        hrnkCdIds.add(hrnkCdId);

        List<CodeDTO> codeDTOList = codeService.findAll(null, "Y", hrnkCdIds);

        return new AnsApiResponse<>(codeDTOList);
    }

    @Operation(summary = "????????? ???????????? ( ?????? )", description = "????????? ???????????? ( ?????? )")
    @GetMapping(path = "/users")
    public AnsApiResponse<List<UserDTO>> findAllUsers(@Parameter(name = "userId", description = "????????? ?????????", in = ParameterIn.QUERY)
                                                      @RequestParam(value = "userId", required = false) String userId,
                                                      @Parameter(name = "userNm", description = "????????????", in = ParameterIn.QUERY)
                                                      @RequestParam(value = "userNm", required = false) String userNm,
                                                      @Parameter(name = "searchWord", description = "???????????????")
                                                      @RequestParam(value = "searchWord", required = false) String searchWord,
                                                      @Parameter(name = "email", description = "?????????")
                                                      @RequestParam(value = "email", required = false) String email,
                                                      @Parameter(name = "delYn", description = "?????? ??????", in = ParameterIn.QUERY)
                                                      @RequestParam(value = "delYn", required = false) String delYn,
                                                      @RequestHeader(value = "securityKey") String securityKey) {

        List<UserDTO> result = userService.findAll(userId, userNm, searchWord, email, delYn);


        return new AnsApiResponse<>(result);

    }

    @Operation(summary = "????????? ???????????? ( ?????? )", description = "????????? ???????????? ( ?????? )")
    @GetMapping(path = "/user")
    public AnsApiResponse<UserDTO> findUser(@Parameter(name = "userId", description = "????????? ?????????", in = ParameterIn.QUERY)
                                            @RequestParam(value = "userId", required = false) String userId,
                                            @RequestHeader(value = "securityKey") String securityKey) {


        UserDTO returnUser = userService.find(userId);

        return new AnsApiResponse<UserDTO>(returnUser);

    }

    @Operation(summary = "?????? ???????????? ( ?????? )", description = "?????? ???????????? ( ?????? )")
    @GetMapping(path = "/article")
    public AnsApiResponse<ArticleDTO> findArticle(@Parameter(name = "artclId", description = "?????? ?????????", in = ParameterIn.QUERY)
                                            @RequestParam(value = "artclId", required = false) Long artclId,
                                            @RequestHeader(value = "securityKey") String securityKey) {


        ArticleDTO articleDTO = articleService.find(artclId);

        return new AnsApiResponse<ArticleDTO>(articleDTO);

    }
}
