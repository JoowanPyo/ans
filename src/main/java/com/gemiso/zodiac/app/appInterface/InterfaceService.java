package com.gemiso.zodiac.app.appInterface;

import com.gemiso.zodiac.app.appInterface.codeDTO.*;
import com.gemiso.zodiac.app.appInterface.prompterCue.PrompterCueSheetDTO;
import com.gemiso.zodiac.app.appInterface.prompterCue.PrompterCueSheetDataDTO;
import com.gemiso.zodiac.app.appInterface.prompterCue.PrompterCueSheetResultDTO;
import com.gemiso.zodiac.app.appInterface.prompterCue.PrompterCueSheetXML;
import com.gemiso.zodiac.app.appInterface.prompterProgram.PrompterProgramDTO;
import com.gemiso.zodiac.app.appInterface.prompterProgram.PrompterProgramDataDTO;
import com.gemiso.zodiac.app.appInterface.prompterProgram.PrompterProgramResultDTO;
import com.gemiso.zodiac.app.appInterface.prompterProgram.PrompterProgramXML;
import com.gemiso.zodiac.app.appInterface.takerCueFindAllDTO.*;
import com.gemiso.zodiac.app.appInterface.takerProgramDTO.ParentProgramDTO;
import com.gemiso.zodiac.app.appInterface.takerProgramDTO.TakerProgramDTO;
import com.gemiso.zodiac.app.appInterface.takerProgramDTO.TakerProgramDataDTO;
import com.gemiso.zodiac.app.appInterface.takerProgramDTO.TakerProgramResultDTO;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.dto.ArticleCueItemDTO;
import com.gemiso.zodiac.app.code.Code;
import com.gemiso.zodiac.app.code.CodeRepository;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.cueSheet.CueSheetRepository;
import com.gemiso.zodiac.app.cueSheet.CueSheetService;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetDTO;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetFindAllDTO;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItemRepository;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemDTO;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemMapper;
import com.gemiso.zodiac.app.cueSheetItemSymbol.CueSheetItemSymbol;
import com.gemiso.zodiac.app.cueSheetItemSymbol.CueSheetItemSymbolRepository;
import com.gemiso.zodiac.app.cueSheetMedia.CueSheetMedia;
import com.gemiso.zodiac.app.dailyProgram.DailyProgramRepository;
import com.gemiso.zodiac.app.dailyProgram.dto.DailyProgramDTO;
import com.gemiso.zodiac.app.dailyProgram.mapper.DailyProgramMapper;
import com.gemiso.zodiac.app.issue.Issue;
import com.gemiso.zodiac.app.program.Program;
import com.gemiso.zodiac.app.program.dto.ProgramDTO;
import com.gemiso.zodiac.app.symbol.Symbol;
import com.gemiso.zodiac.core.helper.JAXBXmlHelper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class InterfaceService {

    private final CueSheetRepository cueSheetRepository;
    private final CodeRepository codeRepository;
    private final CueSheetItemSymbolRepository cueSheetItemSymbolRepository;
    private final CueSheetItemRepository cueSheetItemRepository;

    private final CueSheetItemMapper cueSheetItemMapper;

    private final CueSheetService cueSheetService;


    public List<ParentProgramDTO> dailyPgmFindAll(Date sdate, Date edate, String brdc_pgm_id, String pgm_nm) throws ParseException {

        /*Date formatSdate = stringToDate(sdate);
        Date formatEdate = stringToDate(edate);*/

        CueSheetFindAllDTO cueSheetFindAllDTO = cueSheetService.findAll(sdate, edate, brdc_pgm_id, pgm_nm, "");

        List<ParentProgramDTO> parentProgramDTOList = toTakerCueSheetList(cueSheetFindAllDTO);

        return parentProgramDTOList;

    }

    //테이커 큐시트&일일편성 목록 테이커DTO 리스트로 변환
    public List<ParentProgramDTO> toTakerCueSheetList(CueSheetFindAllDTO cueSheetFindAllDTO) {

        List<ParentProgramDTO> parentProgramDTOList = new ArrayList<>();

        List<CueSheetDTO> cueSheetDTOList = cueSheetFindAllDTO.getCueSheetDTO();
        List<DailyProgramDTO> dailyProgramDTOList = cueSheetFindAllDTO.getDailyProgramDTO();

        for (CueSheetDTO cueSheet : cueSheetDTOList) {

            String date = cueSheet.getBrdcDt(); //방송일자
            int cueBrdcDt = 0;
            if (date != null && date.trim().isEmpty() == false) { //방송일자 데이터가 있을경우
                //int변환을 위해 -빼기[yyyy-MM-dd -> yyyyMMdd]
                //String getTime = date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10);
                //큐시트 방송일시 비교를 위해 int로 변환
                cueBrdcDt = dateToint(date);
            }

            String time = cueSheet.getBrdcStartTime();
            int cueStartTime = 0;
            if (time != null && time.trim().isEmpty() == false) { //방송시작시간이 있을경우

                //방송시작시작 Int변환을 위해 00:00:00 -> 000000으로 변환
                //String getCueStime = time.substring(0,2)+time.substring(3,5)+time.substring(6,8);
                //큐시트 방송시작시간 비교를 위해 int로 변환
                cueStartTime = timeToInt(time);
            }

            //ConcurrentModificationException에러가 나서 이터레이터로 수정.
            Iterator<DailyProgramDTO> iter = dailyProgramDTOList.listIterator();
            while (iter.hasNext()) {
                DailyProgramDTO dailyProgram = iter.next();

                //일일편성 방송일시 비교를 위해 int로 변환
                int dailyBrdcDt = dateToint(dailyProgram.getBrdcDt());

                //일일편성 방송시작시간 비교를 위해 int로 변환
                int dailyStartTime = timeToInt(dailyProgram.getBrdcStartTime());

                /*//큐시트 방송일시가 일일편성 방송일시보다 크면.
                if (cueBrdcDt > dailyBrdcDt){
                    //프롬프터 큐시트목록 xml변환[ 큐시트 ]
                    prompterProgramDTOList.add(cueToPrompter(cueSheet));
                    break;
                }*/
                //일일편성 방송일시가 큐시트 방송일시보다 크면.
                if (cueBrdcDt < dailyBrdcDt) {
                    parentProgramDTOList.add(dailyToTaker(dailyProgram));
                    iter.remove();
                }
                //큐시트 방송일시와 일일편성 방송일시와 같으면
                if (cueBrdcDt != 0 && dailyBrdcDt != 0 && cueBrdcDt == dailyBrdcDt) {
                    if (cueStartTime > dailyStartTime) {//큐시트 시작시간이 크면 일일편성출력
                        //프롬프터 큐시트목록 xml변환[ 일일편성 ]
                        parentProgramDTOList.add(dailyToTaker(dailyProgram));
                        iter.remove();
                    }
                    /*if (cueStartTime < dailyStartTime){//일일편성 시작시간이 크면 큐시트출력
                        //프롬프터 큐시트목록 xml변환[ 큐시트 ]
                        prompterProgramDTOList.add(cueToPrompter(cueSheet));
                    }*/
                    if (cueStartTime == dailyStartTime) {//같으면 큐시트,일일편성 둘다 출력하되 큐시트 먼저
                        /*//프롬프터 큐시트목록 xml변환[ 큐시트 ]
                        prompterProgramDTOList.add(cueToPrompter(cueSheet));*/
                        //프롬프터 큐시트목록 xml변환[ 일일편성 ]
                        parentProgramDTOList.add(dailyToTaker(dailyProgram));
                        iter.remove();
                    }
                }
            }
            //프롬프터 큐시트목록 xml변환[ 큐시트 ]
            parentProgramDTOList.add(cueToTaker(cueSheet));
        }
        return parentProgramDTOList;
    }

    //큐시트 리스트 테이커 큐시트DTO로 변환
    public ParentProgramDTO cueToTaker(CueSheetDTO cueSheet) {

        //프로그램 아이디가 있으면 넣고 없으면 ""
        String brdcPgmId = "";
        String brdcDivCd = "";
        ProgramDTO programDTO = cueSheet.getProgram();
        if (ObjectUtils.isEmpty(programDTO) == false) {
            brdcPgmId = programDTO.getBrdcPgmId();
            brdcDivCd = programDTO.getBrdcPgmDivCd();
        }

        ParentProgramDTO pcsDto = ParentProgramDTO.builder()
                .cueId(cueSheet.getCueId()) //큐시트 아이디
                .rdStNm(cueSheet.getCueStCdNm())//방송상태
                .chDivCd(cueSheet.getChDivCd()) //채널 구분 코드
                .chDivNm(cueSheet.getChDivCdNm()) // 채널 구분 코드 명
                .brdcDt(cueSheet.getBrdcDt()) //방송일자
                .brdcSeq(null)
                .brdcStartTime(cueSheet.getBrdcStartTime()) //방송시작 시간
                .brdcEndTime(cueSheet.getBrdcEndTime()) //방송종료 시간
                .brdcRunTime(cueSheet.getBrdcRunTime())//방송 길이
                .brdcPgmId(brdcPgmId) // 프로그램 아이디
                .brdcPgmNm(cueSheet.getBrdcPgmNm()) //프로그램 명
                .urgPgmschPgmNm("") // 수정.몬지모름
                .brdcDivCD(brdcDivCd) //방송 구분 코드
                .cmDivCd("") //수정
                .remark(cueSheet.getRemark()) //비고
                .inputr(cueSheet.getInputrId())//입력자
                .inputrNm(cueSheet.getInputrNm())//입렵자명
                .inputDtm(cueSheet.getInputDtm())//입력일시
                .pd1(cueSheet.getPd1Id())//피디1
                .pd2(cueSheet.getPd2Id())//피디2
                .anc1(cueSheet.getAnc1Id())
                .anc2(cueSheet.getAnc2Id())
                .td1(cueSheet.getTd1Id())
                .stdioId(cueSheet.getStdioId())
                .subrmId(cueSheet.getSubrmId())
                .cgId(0L) //수정. 몬지모름
                .cgloc("") //수정. 몬지모름
                .vfId(0L) //수정. 몬지모름
                .vsId(0L) //수정. 몬지모름
                .pd1Nm(cueSheet.getPd1Nm())
                .pd2Nm(cueSheet.getPd2Nm())
                .anc1Nm(cueSheet.getAnc1Nm())
                .anc2Nm(cueSheet.getAnc2Nm())
                .tdNm(cueSheet.getTd1Nm())
                .stdioNm(cueSheet.getStdioNm()) //스튜디오 명
                .subrmNm(cueSheet.getSubrmNm()) //부조명
                .rdEdtYn("") //수정.
                .endpgmYn("") //수정.
                .build();

        return pcsDto;
    }

    //일일편성 리스트를 테이커 큐시트DTO로 변환 [큐시트 아이디, 방송상태가 없음]
    public ParentProgramDTO dailyToTaker(DailyProgramDTO dailyProgram) {

        //프로그램 아이디가 있으면 넣고 없으면 ""
        String brdcPgmId = "";
        ProgramDTO programDTO = dailyProgram.getProgram();
        if (ObjectUtils.isEmpty(programDTO) == false) {
            brdcPgmId = programDTO.getBrdcPgmId();
        }

        //InputDtm 변환해서 넣어야함 String -> Date

        ParentProgramDTO pcsDto = ParentProgramDTO.builder()
                .cueId(null)
                .chDivCd("")
                .chDivNm("") // 수정.
                .brdcDt(dailyProgram.getBrdcDt()) //방송일시
                .brdcStartTime(dailyProgram.getBrdcStartTime()) //방송 시작 시간
                .brdcEndTime(dailyProgram.getBrdcEndClk()) // 방송 종료 시간
                .brdcRunTime(dailyProgram.getBrdcRunTime()) //방송길이
                .brdcPgmId(brdcPgmId) //프로그램 아이디
                .brdcPgmNm(dailyProgram.getBrdcPgmNm())//프로그램 명
                .urgPgmschPgmNm("") // 수정.몬지모름
                .brdcDivCD(dailyProgram.getBrdcDivCd()) //방송구분코드
                .cmDivCd("") //수정
                .remark(dailyProgram.getRmk())//비고
                .inputr(dailyProgram.getInputrId())//입력자
                .inputrNm(dailyProgram.getInputrNm())//입력자 명
                .inputDtm(null) //Bis에서 들어온 데이터가 달라 변환이 안댐.
                .pd1("")
                .pd2("")
                .anc1("")
                .anc2("")
                .td1("")
                .subrmId(dailyProgram.getSubrmId())
                .stdioId(dailyProgram.getStdioId())
                .cgId(0L) //수정. 몬지모름
                .cgloc("") //수정. 몬지모름
                .vfId(0L) //수정. 몬지모름
                .vsId(0L) //수정. 몬지모름
                .pd1Nm("")
                .pd2Nm("")
                .anc1Nm("")
                .anc2Nm("")
                .tdNm("")
                .stdioNm("") //수정. - 스튜디오명 서브쿼리로 넣어야 하나?
                .subrmNm("") //수정. - 부조명 서브쿼리로 넣어야 하나?
                .rdEdtYn("") //수정.
                .endpgmYn("") //수정.
                .build();

        return pcsDto;
    }

    public String takerPgmToXml(List<ParentProgramDTO> parentProgramDTOList) {

        //큐시트목록 xml을 담는 DTO
        TakerProgramDTO takerProgramDTO = new TakerProgramDTO();
        //success="true" msg="ok" 담는DTO
        TakerProgramResultDTO takerProgramResultDTO = new TakerProgramResultDTO();
        //<data totalcount="6" curpage="0" rowcount="0">&&List<cue>
        TakerProgramDataDTO takerProgramDataDTO = new TakerProgramDataDTO();

        takerProgramResultDTO.setSuccess("true");
        takerProgramResultDTO.setMsg("ok");

        //조회된 큐시트 데이터  set
        //ist<ParentProgramDTO> parentProgramDTOList = pageResultDTO.getDtoList();
        takerProgramDataDTO.setParentProgramDTOList(parentProgramDTOList);
        takerProgramDataDTO.setTotalcount(parentProgramDTOList.stream().count());
        //takerProgramDataDTO.setCurpage(pageResultDTO.getPage());
        takerProgramDataDTO.setRowcount(0);


        takerProgramDTO.setResult(takerProgramResultDTO);
        takerProgramDTO.setData(takerProgramDataDTO);

        //DTO TO XML 파싱
        String xml = JAXBXmlHelper.marshal(takerProgramDTO, TakerProgramDTO.class);

        // name.out.println("xml : " + xml);
        return xml;
    }

    //테이커 큐시트 조회
    public TakerCueSheetDataDTO cuefindAll(String rd_id, String play_seq, String cued_seq, String vplay_seq, String vcued_seq,
                                             String del_yn, String ch_div_cd, String usr_id, String token, String usr_ip,
                                             String format, String lang, String os_type) {

        //큐시트 아이티와 삭제여부값 예외 처리[아이디가 String 타입으로 들어오기 때문에 Long값으로 변환.]
        Long cueId = 60L;
        //Long cueId = null;
        /*if (rd_id != null && rd_id.trim().isEmpty() ==false) {
            cueId = Long.parseLong(rd_id);
        }*/
        //큐시트 아이티와 삭제여부값 예외 처리[여부값이 안들어 올시, N값 설정]
        if (del_yn == null || del_yn.trim().isEmpty()) {
            del_yn = "N";
        }
        //테이커큐시트 상세조회
        Optional<CueSheet> cueSheet = cueSheetRepository.findTakerCue(cueId, del_yn);

        TakerCueSheetDataDTO takerCueSheetDataDTO = new TakerCueSheetDataDTO();

        if (cueSheet.isPresent() == false) { //조회된 큐시트가 없으면 return 빈모델리스트 = 에러가 나지 않게 설정
            return takerCueSheetDataDTO;
        }

        CueSheet cueSheetEntity = cueSheet.get();

        //조회된 큐시트 정보로 TakerCueSheetDTO 리스트 빌드
        List<TakerCueSheetDTO> takerCueSheetDTOList = cueSheetToTakerCueSheet(cueSheetEntity);

        //예비 큐시트 아이템 정보를 taker정보로 빌드.
        List<TakerCueSheetSpareDTO> takerCueSheetSpareDTO = findSpareCueSheet(cueId, del_yn, cueSheetEntity);

        takerCueSheetDataDTO.setTakerCueSheetDTO(takerCueSheetDTOList);//기본 큐시트아이템 정보 set
        takerCueSheetDataDTO.setTakerCueSheetSpareDTO(takerCueSheetSpareDTO);//예비 큐시트 아이템 정보 set

        return takerCueSheetDataDTO;
    }

    //테이커 큐시트 예비 큐시트 조회
    public List<TakerCueSheetSpareDTO> findSpareCueSheet(Long cueId, String del_yn, CueSheet cueSheet){

        List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findSpareCue(cueId, del_yn);

        List<TakerCueSheetSpareDTO> takerCueSheetSpareDTOList = new ArrayList<>();

        int rdSeq = 1; //순번값 set

        for (CueSheetItem cueSheetItem : cueSheetItemList) {
            Article article = cueSheetItem.getArticle(); //큐시트 아이템에 기사정보get
            Issue issue = new Issue();

            Long cueItemId = cueSheetItem.getCueItemId();
            //큐시트 아이템 방송아이콘 List 조회
            List<CueSheetItemSymbol> cueSheetItemSymbolList = cueSheetItemSymbolRepository.findSymbol(cueItemId);

            //cmDivCd, cmDivCd 값 구하기 [채널값으로 심볼에 들어가는 NS-1, NS-2, NS-3 값 구하기]
            String returnSymbolId = "";
            String returnSymbolNm = "";
            for (CueSheetItemSymbol cueSheetItemSymbol : cueSheetItemSymbolList){
                Symbol symbol = cueSheetItemSymbol.getSymbol(); //큐시트아이템에 포함된 심볼  get

                if (ObjectUtils.isEmpty(symbol)){  //심볼이 있을경우
                    String symbolId = symbol.getSymbolId(); //심볼아이디
                    String symbolNm = symbol.getSymbolNm(); //심볼 명

                    switch(symbolId){ // VNS1, VNS2, VNS3 채널로 표기된 심볼이 들어가 있을경우 값 셋팅
                        case "VNS1":
                            returnSymbolId = symbolId;
                            returnSymbolNm = symbolNm;
                            break;
                        case "VNS2":
                            returnSymbolId = symbolId;
                            returnSymbolNm = symbolNm;
                            break;
                        case "VNS3":
                            returnSymbolId = symbolId;
                            returnSymbolNm = symbolNm;
                            break;
                    }
                }
            }

            //큐시트 아이템 비디오 정보 get
            List<CueSheetMedia> cueSheetMediaList = cueSheetItem.getCueSheetMedia();

            List<TakerCueSheetVideoDTO> takerCueSheetVideoDTOList = getVideoDTOList(cueSheetMediaList);

            if (ObjectUtils.isEmpty(article)) { //기사가 포함이 안된 큐시트 아이템 일시

                //프로그램 아이디 get null에러 방지
                Program program = cueSheet.getProgram();
                String brdcPgmId = "";
                if (ObjectUtils.isEmpty(program) == false) {
                    brdcPgmId = program.getBrdcPgmId();
                }

                //테이커큐시트 정보 큐시트 엔티티 정보로 빌드
                TakerCueSheetSpareDTO takerCueSheetDTO = TakerCueSheetSpareDTO.builder()
                        .brdcPgmId(brdcPgmId)
                        .rdSeq(rdSeq)
                        .chDivCd(cueSheet.getChDivCd()) // 채널구분코드
                        .cueDivCdNm(cueSheet.getCueDivCdNm()) //채널구분코드 명
                        .rdOrd(cueSheetItem.getCueItemOrd())//순번
                        .rdOrdMrk(cueSheetItem.getCueItemOrdCd())//표시되는 순번
                        .rdDtlDivCd(cueSheetItem.getCueItemDivCd()) //큐시트아이템 구분 코드
                        .mcStCd(cueSheetItem.getBrdcStCd()) //방송상태코드
                        .cmDivCd(returnSymbolId)//심볼 아이디 (채널명) ex VNS1, VNS2, VNS3
                        .rdDtlDivNm(cueSheetItem.getCueItemDivCdNm())//큐시트아이템 구분 코드 명
                        .mcStNm(cueSheetItem.getBrdcStCdNm())//방송상태코드 명
                        .cmDivNm(returnSymbolNm)//심볼 아이디 명 (채널명) ex NS-1, NS-2, NS-3
                        .takerCueSheetVideoDTO(takerCueSheetVideoDTOList)//???
                        .build();

                takerCueSheetSpareDTOList.add(takerCueSheetDTO); //빌드된 큐시트테이커DTO 리턴할 큐시트테이커 리스트에 add

                ++rdSeq;

            } else {
                //이슈아이디 get null에러 방지
                issue = article.getIssue();
                Long issueId = 0L;
                if (ObjectUtils.isEmpty(issue) == false) {
                    issueId = issue.getIssuId();
                }
                //프로그램 아이디 get null에러 방지
                Program program = cueSheet.getProgram();
                String brdcPgmId = "";
                if (ObjectUtils.isEmpty(program) == false) {
                    brdcPgmId = program.getBrdcPgmId();
                }

                //테이커큐시트 정보 큐시트 엔티티 정보로 빌드
                TakerCueSheetSpareDTO takerCueSheetDTO = TakerCueSheetSpareDTO.builder()
                        .brdcPgmId(brdcPgmId)
                        .rdSeq(0)
                        .chDivCd(cueSheet.getChDivCd())// 채널구분코드
                        .cueDivCdNm(cueSheet.getCueDivCdNm())//채널구분코드 명
                        .rdOrd(cueSheetItem.getCueItemOrd())//큐시트 아이템 순번
                        .rdOrdMrk(cueSheetItem.getCueItemOrdCd())//표시되는 순번
                        .rdDtlDivCd(cueSheetItem.getCueItemDivCd()) //큐시트아이템 구분 코드
                        .mcStCd(cueSheetItem.getBrdcStCd()) //방송상태코드
                        .cmDivCd(returnSymbolId)//심볼 아이디 (채널명) ex VNS1, VNS2, VNS3
                        .rdDtlDivNm(cueSheetItem.getCueItemDivCdNm())//큐시트아이템 구분 코드 명
                        .mcStNm(cueSheetItem.getBrdcStCdNm())//방송상태 명
                        .cmDivNm(returnSymbolNm)//심볼 아이디 명 (채널명) ex NS-1, NS-2, NS-3
                        .artclId(article.getArtclId())
                        .artclFrmCd(article.getArtclFrmCd())
                        .artclFrmNm(article.getArtclFrmCdNm())
                        .artclFldCd(article.getArtclFldCd())
                        .artclFldNm(article.getArtclFldCdNm())
                        .artclTitl(article.getArtclTitl())
                        .rptrNm(article.getRptrNm())
                        .deptCd(article.getDeptCd())
                        .deptNm(article.getDeptNm())
                        .artclReqdSec(Optional.ofNullable(article.getArtclReqdSec()).orElse(0))
                        .artclSmryCtt(0)//???
                        .artclDivCd(article.getArtclDivCd())
                        .artclDivNm(article.getArtclDivCdNm())
                        .issuId(issueId)
                        .lckrId(article.getLckrId())
                        .lckrNm(article.getLckrNm())
                        .lckDtm(article.getLckDtm())
                        .apprvDivCd(article.getApprvDivCd())
                        .apprvDivNm(article.getApprvDivCdNm())
                        .apprvDtm(article.getApprvDtm())
                        .apprvrId(article.getApprvrId())
                        .apprvrNm(article.getApprvrNm())
                        .artclOrd(article.getArtclOrd())
                        .brdcCnt(Optional.ofNullable(article.getBrdcCnt()).orElse(0))
                        .orgArtclId(article.getOrgArtclId())
                        .rptPlnId(article.getRptrId())
                        .brdcFnshYn("")//???
                        .urgYn(article.getUrgYn())
                        .frnotiYn(article.getFrnotiYn())
                        .embgYn(article.getEmbgYn())
                        .updtLckYn(article.getLckYn())
                        .internetOnlyYn("")//???
                        .snsYn("")//???
                        .inputrId(article.getInputrId())
                        .inputrNm(article.getInputrNm())
                        .inputDtm(dateToString(article.getInputDtm())) //Date형식의 입력일시를 String으로 변환
                        .takerCueSheetVideoDTO(takerCueSheetVideoDTOList)//???
                        .build();

                takerCueSheetSpareDTOList.add(takerCueSheetDTO); //빌드된 큐시트테이커DTO 리턴할 큐시트테이커 리스트에 add

                ++rdSeq;

            }
        }

        return takerCueSheetSpareDTOList;


    }

    //큐시티 엔티티로 조회된 큐시트 TakerCueSheetDTO형식으로 빌드
    public List<TakerCueSheetDTO> cueSheetToTakerCueSheet(CueSheet cueSheet) {

        //List<CueSheetItem> cueSheetItemList = cueSheet.getCueSheetItem();//큐시트 엔티티에서 큐시트 아이템 리스트 get

        List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findByCueItemList(cueSheet.getCueId());
        List<TakerCueSheetDTO> takerCueSheetDTOList = new ArrayList<>();//테이커 큐시트 DTO를 담아서 리턴할 리스트

        int rdSeq = 1; //순번값 set

        for (CueSheetItem cueSheetItem : cueSheetItemList) {
            Article article = cueSheetItem.getArticle(); //큐시트 아이템에 기사정보get

            Long cueItemId = cueSheetItem.getCueItemId();
            //큐시트 아이템 방송아이콘 List 조회
            List<CueSheetItemSymbol> cueSheetItemSymbolList = cueSheetItemSymbolRepository.findSymbol(cueItemId);

            //cmDivCd, cmDivCd 값 구하기 [채널값으로 심볼에 들어가는 NS-1, NS-2, NS-3 값 구하기]
            String returnSymbolId = "";
            String returnSymbolNm = "";
            for (CueSheetItemSymbol cueSheetItemSymbol : cueSheetItemSymbolList){
                Symbol symbol = cueSheetItemSymbol.getSymbol(); //큐시트아이템에 포함된 심볼  get

                if (ObjectUtils.isEmpty(symbol)){  //심볼이 있을경우
                    String symbolId = symbol.getSymbolId(); //심볼아이디
                    String symbolNm = symbol.getSymbolNm(); //심볼 명

                    switch(symbolId){ // VNS1, VNS2, VNS3 채널로 표기된 심볼이 들어가 있을경우 값 셋팅
                        case "VNS1":
                            returnSymbolId = symbolId;
                            returnSymbolNm = symbolNm;
                            break;
                        case "VNS2":
                            returnSymbolId = symbolId;
                            returnSymbolNm = symbolNm;
                            break;
                        case "VNS3":
                            returnSymbolId = symbolId;
                            returnSymbolNm = symbolNm;
                            break;
                    }
                }
            }

            //큐시트 아이템 비디오 정보 get
            List<CueSheetMedia> cueSheetMediaList = cueSheetItem.getCueSheetMedia();

            List<TakerCueSheetVideoDTO> takerCueSheetVideoDTOList = getVideoDTOList(cueSheetMediaList);

            if (ObjectUtils.isEmpty(article)) { //기사가 포함이 안된 큐시트 아이템 일시


                //테이커큐시트 정보 큐시트 엔티티 정보로 빌드
                TakerCueSheetDTO takerCueSheetDTO = buildTakerCue(cueSheet, cueSheetItem, rdSeq, returnSymbolId
                        , returnSymbolNm, takerCueSheetVideoDTOList);

                takerCueSheetDTOList.add(takerCueSheetDTO); //빌드된 큐시트테이커DTO 리턴할 큐시트테이커 리스트에 add

                rdSeq++; //순서값 +

            } else {

                //테이커큐시트 정보 큐시트 엔티티 정보로 빌드
                TakerCueSheetDTO takerCueSheetDTO = buildTakerCueArticle(cueSheet, cueSheetItem, rdSeq, returnSymbolId
                        ,returnSymbolNm,  takerCueSheetVideoDTOList, article);

                takerCueSheetDTOList.add(takerCueSheetDTO); //빌드된 큐시트테이커DTO 리턴할 큐시트테이커 리스트에 add

                rdSeq++; //순서값 +

            }
        }

        return takerCueSheetDTOList;
    }

    //테이커큐시트 정보 큐시트 엔티티 정보로 빌드( 기사 x)
    public TakerCueSheetDTO buildTakerCue(CueSheet cueSheet, CueSheetItem cueSheetItem, int rdSeq
            , String returnSymbolId, String returnSymbolNm, List<TakerCueSheetVideoDTO> takerCueSheetVideoDTOList){

        //프로그램 아이디 get null에러 방지
        Program program = cueSheet.getProgram();
        String brdcPgmId = "";
        if (ObjectUtils.isEmpty(program) == false) {
            brdcPgmId = program.getBrdcPgmId();
        }

        //테이커큐시트 정보 큐시트 엔티티 정보로 빌드
        TakerCueSheetDTO takerCueSheetDTO = TakerCueSheetDTO.builder()
                .brdcPgmId(brdcPgmId)
                .rdSeq(rdSeq)
                .chDivCd(cueSheet.getChDivCd()) // 채널구분코드
                .cueDivCdNm(cueSheet.getCueDivCdNm()) //채널구분코드 명
                .rdOrd(cueSheetItem.getCueItemOrd())//순번
                .rdOrdMrk(cueSheetItem.getCueItemOrdCd())//표시되는 순번(알파벳? 숫자 같이 들어가 있고 안들어가는 부분이 있음)
                .rdDtlDivCd(cueSheetItem.getCueItemDivCd()) //큐시트아이템 구분 코드
                .mcStCd(cueSheetItem.getBrdcStCd()) //방송상태코드
                .cmDivCd(returnSymbolId)//심볼 아이디 (채널명) ex VNS1, VNS2, VNS3
                .rdDtlDivNm(cueSheetItem.getCueItemDivCdNm())//큐시트아이템 구분 코드 명
                .mcStNm(cueSheetItem.getBrdcStCdNm())//방송상태코드 명
                .cmDivNm(returnSymbolNm)//심볼 아이디 명 (채널명) ex NS-1, NS-2, NS-3
                .takerCueSheetVideoDTO(takerCueSheetVideoDTOList)//???
                .build();

        return takerCueSheetDTO;
    }

    //테이커큐시트 정보 큐시트 엔티티 정보로 빌드( 기사 o)
    public TakerCueSheetDTO buildTakerCueArticle(CueSheet cueSheet, CueSheetItem cueSheetItem, int rdSeq
            , String returnSymbolId, String returnSymbolNm, List<TakerCueSheetVideoDTO> takerCueSheetVideoDTOList, Article article) {

        //이슈아이디 get null에러 방지
        Issue issue = article.getIssue();
        Long issueId = 0L;
        if (ObjectUtils.isEmpty(issue) == false) {
            issueId = issue.getIssuId();
        }
        //프로그램 아이디 get null에러 방지
        Program program = cueSheet.getProgram();
        String brdcPgmId = "";
        if (ObjectUtils.isEmpty(program) == false) {
            brdcPgmId = program.getBrdcPgmId();
        }

        //테이커큐시트 정보 큐시트 엔티티 정보로 빌드
        TakerCueSheetDTO takerCueSheetDTO = TakerCueSheetDTO.builder()
                .brdcPgmId(brdcPgmId)
                .rdSeq(rdSeq)
                .chDivCd(cueSheet.getChDivCd())// 채널구분코드
                .cueDivCdNm(cueSheet.getCueDivCdNm())//채널구분코드 명
                .rdOrd(cueSheetItem.getCueItemOrd())//큐시트 아이템 순번
                .rdOrdMrk(cueSheetItem.getCueItemOrdCd())//표시되는 순번(알파벳? 숫자 같이 들어가 있고 안들어가는 부분이 있음)
                .rdDtlDivCd(cueSheetItem.getCueItemDivCd()) //큐시트아이템 구분 코드
                .mcStCd(cueSheetItem.getBrdcStCd()) //방송상태코드
                .cmDivCd(returnSymbolId)//심볼 아이디 (채널명) ex VNS1, VNS2, VNS3
                .rdDtlDivNm(cueSheetItem.getCueItemDivCdNm())//큐시트아이템 구분 코드 명
                .mcStNm(cueSheetItem.getBrdcStCdNm())//방송상태 명
                .cmDivNm(returnSymbolNm)//심볼 아이디 명 (채널명) ex NS-1, NS-2, NS-3
                .artclId(article.getArtclId())
                .artclFrmCd(article.getArtclFrmCd())
                .artclFrmNm(article.getArtclFrmCdNm())
                .artclFldCd(article.getArtclFldCd())
                .artclFldNm(article.getArtclFldCdNm())
                .artclTitl(article.getArtclTitl())
                .rptrNm(article.getRptrNm())
                .deptCd(article.getDeptCd())
                .deptNm(article.getDeptNm())
                .artclReqdSec(Optional.ofNullable(article.getArtclReqdSec()).orElse(0))
                .artclSmryCtt(0)//???
                .artclDivCd(article.getArtclDivCd())
                .artclDivNm(article.getArtclDivCdNm())
                .issuId(issueId)
                .lckrId(article.getLckrId())
                .lckrNm(article.getLckrNm())
                .lckDtm(article.getLckDtm())
                .apprvDivCd(article.getApprvDivCd())
                .apprvDivNm(article.getApprvDivCdNm())
                .apprvDtm(article.getApprvDtm())
                .apprvrId(article.getApprvrId())
                .apprvrNm(article.getApprvrNm())
                .artclOrd(article.getArtclOrd())
                .brdcCnt(Optional.ofNullable(article.getBrdcCnt()).orElse(0))
                .orgArtclId(article.getOrgArtclId())
                .rptPlnId(article.getRptrId())
                .brdcFnshYn("")//???
                .urgYn(article.getUrgYn())
                .frnotiYn(article.getFrnotiYn())
                .embgYn(article.getEmbgYn())
                .updtLckYn(article.getLckYn())
                .internetOnlyYn("")//???
                .snsYn("")//???
                .inputrId(article.getInputrId())
                .inputrNm(article.getInputrNm())
                .inputDtm(dateToString(article.getInputDtm())) //Date형식의 입력일시를 String으로 변환
                .takerCueSheetVideoDTO(takerCueSheetVideoDTOList)//???
                .build();

        return takerCueSheetDTO;

    }
    //큐시트 아이템 비디오 정보 get
    public List<TakerCueSheetVideoDTO> getVideoDTOList(List<CueSheetMedia> cueSheetMediaList){

        List<TakerCueSheetVideoDTO> returnDTOList = new ArrayList<>(); //리턴할 비디오 DTO 리스트

        //Mam되면 수정
        /*for (CueSheetMedia cueSheetMedia : cueSheetMediaList){//큐시트 아이템에 포함된 큐시트 미디어 정보 get

            //테이커 비디오 정보 빌드
            TakerCueSheetVideoDTO takerCueSheetVideoDTO = TakerCueSheetVideoDTO.builder()
                    .title(cueSheetMedia.getCueMediaTitl()) //미디어 제목
                    .playout_id("") // clip Id
                    .duration(cueSheetMedia.getMediaDurtn()) // 미디어 길이
                    .build();

            returnDTOList.add(takerCueSheetVideoDTO);

        }*/

        TakerCueSheetVideoClipDTO takerCueSheetVideoClipDTO = TakerCueSheetVideoClipDTO.builder()
                .title("media title") //미디어 제목
                .playout_id("11") // clip Id
                .seq("1")
                .duration("01:30") // 미디어 길이
                .build();


        //테이커 비디오 정보 빌드
        TakerCueSheetVideoDTO takerCueSheetVideoDTO = TakerCueSheetVideoDTO.builder()
                .takerCueSheetVideoClipDTO(takerCueSheetVideoClipDTO)
                .build();

        returnDTOList.add(takerCueSheetVideoDTO);

        return returnDTOList;
    }

    //조회된 큐시트 데이터 리스트를 XML DTO에 set 후 XML형식 String데이터로 파싱
    public String takerCueToXml(TakerCueSheetDataDTO takerCueSheetDataDTO) {
        //큐시트목록 xml을 담는 DTO
        TakerCueSheetXML takerCueSheetXML = new TakerCueSheetXML();
        //success="true" msg="ok" 담는DTO
        TakerCueSheetResultDTO takerCueSheetResultDTO = new TakerCueSheetResultDTO();
        //<data totalcount="6" curpage="0" rowcount="0">&&List<cue>
        //TakerCueSheetDataDTO takerCueSheetDataDTO = new TakerCueSheetDataDTO();

        //result 데이터 set
        takerCueSheetResultDTO.setMsg("ok");
        takerCueSheetResultDTO.setSuccess("true");

        //dataDTO 데이터 set
        //takerCueSheetDataDTO.setTakerCueSheetDTO(takerCueSheetDTOList);

        //큐시트 아이템, 예비 큐시트 아이템 조회된 토탈 카운트 get
        List<TakerCueSheetDTO> takerCueSheetDTO = takerCueSheetDataDTO.getTakerCueSheetDTO();
        List<TakerCueSheetSpareDTO> takerCueSheetSpareDTO = takerCueSheetDataDTO.getTakerCueSheetSpareDTO();
        Long totalCount = takerCueSheetDTO.stream().count() + takerCueSheetSpareDTO.stream().count();

        takerCueSheetDataDTO.setTotalcount(totalCount);
        takerCueSheetDataDTO.setCurpage(0);
        takerCueSheetDataDTO.setRowcount(0);

        //xml 변환 DTO에 result, dataDTO set
        takerCueSheetXML.setData(takerCueSheetDataDTO);
        takerCueSheetXML.setResult(takerCueSheetResultDTO);

        //DTO TO XML 파싱
        String xml = JAXBXmlHelper.marshal(takerCueSheetXML, TakerCueSheetXML.class);

        return xml;

    }

    //Date형식을 String으로 파싱
    public String dateToString(Date date) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String stringDate = dateFormat.format(date);

        return stringDate;
    }

    //채널코드 조회
    public TakerCodeDTO codeFindAll(String key, String ch_div_cd, String usr_id, String token, String usr_ip, String format,
                                    String lang, String os_type) {

        String hrnkCd = "channel";

        List<Code> codeList = codeRepository.findTakerCode(hrnkCd); // 상위코드 "channel"값으로 아리랑 채널 조회

        TakerCodeDTO takerCodeDTO = codeEntityToTakerCodeDTO(codeList); //조회된 코드를 TakerCodeDTO로 빌드

        return takerCodeDTO;
    }

    //조회한 코드 데이터 엔티티 리스트 정보를 TakerCodeDTO로 빌드
    public TakerCodeDTO codeEntityToTakerCodeDTO(List<Code> codeList) {

        List<TakerCodeHrnkDTO> takerCodeHrnkDTOList = new ArrayList<>(); //데이터DTO에 set 시켜줄 데이터 리스트

        for (Code code : codeList) { //조회된 코드 엔티티 정보를 [cd, cdNm]  TakerCodeHrnkDTO 데이터에 set

            TakerCodeHrnkDTO takerCodeHrnkDTO = TakerCodeHrnkDTO.builder()
                    .cd(code.getCd())
                    .cdNm(code.getCdNm())
                    .build();
            takerCodeHrnkDTOList.add(takerCodeHrnkDTO); ////데이터DTO에 set 시켜줄 데이터 리스트에 set
        }
        //코드XML DTO에 set 시켜줄 dataDTO 빌드
        TakerCodeDTO takerCodeDTO = TakerCodeDTO.builder()
                .hrnkCd("channel")
                .hrnkCdNm("채널 코드")
                .code(takerCodeHrnkDTOList)
                .build();

        return takerCodeDTO;
    }

    public String codeToTakerCodeXml(TakerCodeDTO takerCodeDTO) {

        //코드목록 XML을 담는 DTO [DTO TO XML]
        TakerCodeXML takerCodeXML = new TakerCodeXML();
        //&List<Code>
        TakerCodeDataDTO takerCodeDataDTO = new TakerCodeDataDTO();
        //success="true" msg="ok" 담는DTO
        TakerCodeResultDTO takerCodeResultDTO = new TakerCodeResultDTO();

        //dataDTO set code데이터
        takerCodeDataDTO.setTakerCodeDTO(takerCodeDTO);

        //result 데이터 set
        takerCodeResultDTO.setMsg("ok");
        takerCodeResultDTO.setSuccess("true");

        //XML 변환할 Code데이터 set
        takerCodeXML.setData(takerCodeDataDTO);
        takerCodeXML.setResult(takerCodeResultDTO);

        //DTO TO XML 파싱
        String xml = JAXBXmlHelper.marshal(takerCodeXML, TakerCodeXML.class);

        return xml;

    }

    //프롬프터 일일편성 목록조회
    public List<PrompterProgramDTO> getMstListService(String pro_id, Date sdate, Date fdate) throws ParseException {

        CueSheetFindAllDTO cueSheetFindAllDTO = cueSheetService.findAll(sdate, fdate, pro_id, "", "");

        List<PrompterProgramDTO> prompterProgramDTOList = toPrompterDailyPgm(cueSheetFindAllDTO);

        return prompterProgramDTOList;
    }

    //String형식의 데이터를 Date타입으로 변환.
   /* public Date stringToDate(String date) throws ParseException {

        Date formatDate = null;

        if (date != null && date.trim().isEmpty() == false) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            formatDate = simpleDateFormat.parse(date);
        }

        return formatDate;
    }*/

    //일일편성 큐시트목록 유니온 목록조회 목록을 프롬프터 형식의 데이터로 변환
    public List<PrompterProgramDTO> toPrompterDailyPgm(CueSheetFindAllDTO cueSheetFindAllDTO) {

        List<PrompterProgramDTO> prompterProgramDTOList = new ArrayList<>(); //리턴시켜줄 프롬프터 프로그램 리스트 생성

        List<CueSheetDTO> cueSheetDTOList = cueSheetFindAllDTO.getCueSheetDTO(); //큐시트목록 조회 리스트
        List<DailyProgramDTO> dailyProgramDTOList = cueSheetFindAllDTO.getDailyProgramDTO(); //알알편성목록 조회 리스트

        for (CueSheetDTO cueSheet : cueSheetDTOList) {

            String date = cueSheet.getBrdcDt(); //방송일자
            int cueBrdcDt = 0;
            if (date != null && date.trim().isEmpty() == false) { //방송일자 데이터가 있을경우
                //int변환을 위해 -빼기[yyyy-MM-dd -> yyyyMMdd]
                //String getTime = date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10);
                //큐시트 방송일시 비교를 위해 int로 변환
                cueBrdcDt = dateToint(date);
            }

            String time = cueSheet.getBrdcStartTime();
            int cueStartTime = 0;
            if (time != null && time.trim().isEmpty() == false) { //방송시작시간이 있을경우

                //방송시작시작 Int변환을 위해 00:00:00 -> 000000으로 변환
                //String getCueStime = time.substring(0,2)+time.substring(3,5)+time.substring(6,8);
                //큐시트 방송시작시간 비교를 위해 int로 변환
                cueStartTime = timeToInt(time);
            }

            //ConcurrentModificationException에러가 나서 이터레이터로 수정.
            Iterator<DailyProgramDTO> iter = dailyProgramDTOList.listIterator();
            while (iter.hasNext()) {
                DailyProgramDTO dailyProgram = iter.next();

                //일일편성 방송일시 비교를 위해 int로 변환
                int dailyBrdcDt = dateToint(dailyProgram.getBrdcDt());

                //일일편성 방송시작시간 비교를 위해 int로 변환
                int dailyStartTime = timeToInt(dailyProgram.getBrdcStartTime());

                /*//큐시트 방송일시가 일일편성 방송일시보다 크면.
                if (cueBrdcDt > dailyBrdcDt){
                    //프롬프터 큐시트목록 xml변환[ 큐시트 ]
                    prompterProgramDTOList.add(cueToPrompter(cueSheet));
                    break;
                }*/
                //일일편성 방송일시가 큐시트 방송일시보다 크면.
                if (cueBrdcDt < dailyBrdcDt) {
                    prompterProgramDTOList.add(dailyToPrompter(dailyProgram));
                    iter.remove();
                }
                //큐시트 방송일시와 일일편성 방송일시와 같으면
                if (cueBrdcDt != 0 && dailyBrdcDt != 0 && cueBrdcDt == dailyBrdcDt) {
                    if (cueStartTime > dailyStartTime) {//큐시트 시작시간이 크면 일일편성출력
                        //프롬프터 큐시트목록 xml변환[ 일일편성 ]
                        prompterProgramDTOList.add(dailyToPrompter(dailyProgram));
                        iter.remove();
                    }
                    /*if (cueStartTime < dailyStartTime){//일일편성 시작시간이 크면 큐시트출력
                        //프롬프터 큐시트목록 xml변환[ 큐시트 ]
                        prompterProgramDTOList.add(cueToPrompter(cueSheet));
                    }*/
                    if (cueStartTime == dailyStartTime) {//같으면 큐시트,일일편성 둘다 출력하되 큐시트 먼저
                        /*//프롬프터 큐시트목록 xml변환[ 큐시트 ]
                        prompterProgramDTOList.add(cueToPrompter(cueSheet));*/
                        //프롬프터 큐시트목록 xml변환[ 일일편성 ]
                        prompterProgramDTOList.add(dailyToPrompter(dailyProgram));
                        iter.remove();
                    }
                }
            }
            //프롬프터 큐시트목록 xml변환[ 큐시트 ]
            prompterProgramDTOList.add(cueToPrompter(cueSheet));
        }
        return prompterProgramDTOList;
    }

    //프롬프터 큐시트목록 xml변환[ 일일편성 ]
    public PrompterProgramDTO dailyToPrompter(DailyProgramDTO dailyProgram) {

        ProgramDTO programDTO = dailyProgram.getProgram();//프로그램 get
        String brdcPgmId = "";//set해줄 프로그램 아이디
        if (ObjectUtils.isEmpty(programDTO) == false) { //프로그램이 있으면 프로그램아이디 출력하여 set
            brdcPgmId = programDTO.getBrdcPgmId();
        }
        PrompterProgramDTO program = PrompterProgramDTO.builder()
                .brdcPgmId(brdcPgmId)
                .proNm(dailyProgram.getBrdcPgmNm())
                .chDivCd("")
                .onAirDate(dailyProgram.getBrdcDt())
                .startTime(dailyProgram.getBrdcStartTime())
                .endTime(dailyProgram.getBrdcEndClk())
                .aricleCount(0)
                .brdcStCd("")
                .rdEditYn("")
                .build();
        return program;
    }

    //프롬프터 큐시트목록 xml변환[ 큐시트 ]
    public PrompterProgramDTO cueToPrompter(CueSheetDTO cueSheet) {

        ProgramDTO programDTO = cueSheet.getProgram();//프로그램 get
        String brdcPgmId = "";//set해줄 프로그램 아이디
        if (ObjectUtils.isEmpty(programDTO) == false) { //프로그램이 있으면 프로그램아이디 출력하여 set
            brdcPgmId = programDTO.getBrdcPgmId();
        }

                //기사수 get
        List<CueSheetItemDTO> cueSheetItemDTOList = cueSheet.getCueSheetItem();
        int articleCount = 0;
        for (CueSheetItemDTO cueSheetItemDTO : cueSheetItemDTOList) {
            ArticleCueItemDTO article = cueSheetItemDTO.getArticle();
            if (ObjectUtils.isEmpty(article)) {//기사가 포함되지 않았으면 contiue;
                continue;
            }
            ++articleCount; //기사가 포함되어있으면 +1
        }



        PrompterProgramDTO program = PrompterProgramDTO.builder()
                .csId(cueSheet.getCueId())
                .chDivCd(cueSheet.getChDivCd())
                .brdcPgmId(brdcPgmId)
                .proNm(cueSheet.getBrdcPgmNm())
                .onAirDate(cueSheet.getBrdcDt())
                .startTime(cueSheet.getBrdcStartTime())
                .endTime(cueSheet.getBrdcEndTime())
                .aricleCount(articleCount)
                .brdcStCd(cueSheet.getCueStCd())
                .rdEditYn("")
                .build();
        return program;
    }

    //time to int
    public int timeToInt(String time) {

        String formatTime = time.substring(0, 2) + time.substring(3, 5) + time.substring(6, 8);

        int formatDailyStartTime = 0;
        if (formatTime != null & formatTime.trim().isEmpty() == false) {
            formatDailyStartTime = Integer.parseInt(formatTime);
        }

        return formatDailyStartTime;
    }

    //date to int
    public int dateToint(String date) {

        String formatDate = date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10);

        int returnInt = 0;
        if (formatDate != null & formatDate.trim().isEmpty() == false) {
            returnInt = Integer.parseInt(formatDate);
        }

        return returnInt;
    }

    //프로그램 프롬프터 Xml형식으로 변환
    public String prompterProgramToXml(List<PrompterProgramDTO> prompterProgramDTOList) {

        //프롬프터 xml형식으로 변환할
        PrompterProgramXML prompterProgramXML = new PrompterProgramXML();

        //Lsit<prompterProgram>
        PrompterProgramDataDTO prompterProgramDataDTO = new PrompterProgramDataDTO();

        //success="true" msg="ok" 담는DTO
        PrompterProgramResultDTO prompterProgramResultDTO = new PrompterProgramResultDTO();

        //dataDTO set code데이터
        prompterProgramDataDTO.setPrompterProgramDTO(prompterProgramDTOList);

        //result 데이터 set
        prompterProgramDataDTO.setTotalcount(prompterProgramDTOList.stream().count());
        prompterProgramResultDTO.setMsg("ok");
        prompterProgramResultDTO.setSuccess("true");

        //XML 변환할 Code데이터 set
        prompterProgramXML.setData(prompterProgramDataDTO);
        prompterProgramXML.setResult(prompterProgramResultDTO);

       /* prompterProgramXML.setPrompterProgramDTO(prompterProgramDTOList);*/

        //DTO TO XML 파싱
        String xml = JAXBXmlHelper.marshal(prompterProgramXML, PrompterProgramXML.class);

        return xml;
    }

    //프롬프터 큐시트 상세 조회 -> PrompterCueSheetDTO리스트로 변환
    public List<PrompterCueSheetDTO> getCuesheetService(Long cs_id) {


        CueSheetDTO cueSheetDTO = cueSheetService.find(cs_id); //프롬프트로 보내줄 큐시트를 조회[단건 : 조건 큐시트 아이디]

        List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetDTO.getCueSheetItem(); //조회된 큐시트상세 정보에서 큐시트 아이템get*/

        List<CueSheetItem> cueSheetItemList = cueSheetItemMapper.toEntityList(cueSheetItemDTOList);

        // 조회된 큐시트 정보를 List<PrompterCueSheetDTO>빌드 [기사정보가 있는 큐시트아이템 정보를 프롬프트DTO로 빌드 후 리턴]
        List<PrompterCueSheetDTO> prompterCueSheetDTOList = cueToPrompterCue(cueSheetItemList);

        return prompterCueSheetDTOList;

    }

    public String prompterCueSheetXml(List<PrompterCueSheetDTO> prompterCueSheetDTOList) {

        //프롬프터 큐시트 xml형식으로 변환할 DTO
        PrompterCueSheetXML prompterCueSheetXML = new PrompterCueSheetXML();

        //set Lsit<PrompterCueSheetDTO>
        PrompterCueSheetDataDTO prompterCueSheetDataDTO = new PrompterCueSheetDataDTO();

        //success="true" msg="ok" 담는DTO
        PrompterCueSheetResultDTO prompterCueSheetResultDTO = new PrompterCueSheetResultDTO();

        //dataDTO set code데이터
        prompterCueSheetDataDTO.setCueSheetDTO(prompterCueSheetDTOList);

        //result 데이터 set
        prompterCueSheetDataDTO.setTotalcount(prompterCueSheetDTOList.stream().count());
        prompterCueSheetResultDTO.setMsg("ok");
        prompterCueSheetResultDTO.setSuccess("true");

        //XML 변환할 데이터 set
        prompterCueSheetXML.setData(prompterCueSheetDataDTO);
        prompterCueSheetXML.setResult(prompterCueSheetResultDTO);

        /*prompterCueSheetXML.setCueSheetDTO(prompterCueSheetDTOList);*/

        //DTO TO XML 파싱
        String xml = JAXBXmlHelper.marshal(prompterCueSheetXML, PrompterCueSheetXML.class);

        return xml;
    }

    // 조회된 큐시트 정보를 List<PrompterCueSheetDTO>빌드 [기사정보가 있는 큐시트아이템 정보를 프롬프트DTO로 빌드 후 리턴]
    public List<PrompterCueSheetDTO> cueToPrompterCue(List<CueSheetItem> cueSheetItemList) {

        List<PrompterCueSheetDTO> prompterCueSheetDTOList = new ArrayList<>();

        Integer newsAcumTime = 0;
        int ord = 1;

        for (CueSheetItem cueSheetItem : cueSheetItemList) { //큐시트 아이템 PrompterCueSheetDTO리스트로 변환[기사(article)이 있는 아이템만 변환]

            Article article = cueSheetItem.getArticle(); //큐시스트 아이템에서 기사 get
            CueSheet cueSheet = cueSheetItem.getCueSheet();
            Long cueId = cueSheet.getCueId();

            if (ObjectUtils.isEmpty(article)) { //기사 정보가 없으면 contiue [프롬프트에서 기사정보만 쓰임]
                
                //조회된 cueSheetItem정보로 PrompterCueSheetDTO생성
                PrompterCueSheetDTO prompterCueSheetDTO = PrompterCueSheetDTO.builder()
                        .cueId(cueSheetItem.getCueItemId())
                        .rdSeq("")
                        .rdOrd(cueSheetItem.getCueItemOrd())
                        .openYn("")
                        .artclTitl(cueSheetItem.getCueItemTitl()) //국문제목
                        .artclTitlEn(cueSheetItem.getCueItemTitlEn()) // 영문제목
                        .artclCtt(cueSheetItem.getCueItemCtt())
                        .newsAcumTime(newsAcumTime) //누적시간
                        .build();

                //빌드된 PrompterCueSheetDTO를 PrompterCueSheetDTO List에 add
                prompterCueSheetDTOList.add(prompterCueSheetDTO);
                
            }else { //기사 아이템인 경우

                Integer articleCttTime = article.getArtclCttTime(); // 기사 소요시간
                Integer ancCttTime = article.getAncMentCttTime(); // 앵커 소요시간

                if (articleCttTime != null && ancCttTime != null) { //기사,앵커 소요시간이 둘다 Null이 아닌경우
                    // 기사 소요시간 + 앵커 소요시간 + 누적시간 = 누적시간
                    newsAcumTime = articleCttTime + ancCttTime + newsAcumTime;
                } else if (articleCttTime == null && ancCttTime != null) { // 앵커시간이 Null인경우
                    newsAcumTime = ancCttTime + newsAcumTime;
                } else if (articleCttTime != null && ancCttTime == null) { //기사시간이 Null인경우
                    newsAcumTime = articleCttTime + newsAcumTime;
                }


                //조회된 cueSheet정보의 기사정보로 PrompterCueSheetDTO생성
                PrompterCueSheetDTO prompterCueSheetDTO = PrompterCueSheetDTO.builder()
                        .cueId(cueSheetItem.getCueItemId())
                        .rdSeq("")
                        .chDivCd(article.getChDivCd())
                        .rdOrd(cueSheetItem.getCueItemOrd()) //순번
                        .rdOrdMrk(ord)
                        .prdDivCd(article.getPrdDivCd())
                        .openYn("")
                        .artclId(article.getArtclId())
                        .artclFldCd(article.getArtclFldCd())
                        .artclFldNm(article.getArtclFldCdNm())
                        .artclFrmCd(article.getArtclFrmCd())
                        .artclFrmNm(article.getArtclFrmCdNm())
                        .artclTitl(article.getArtclTitl()) //국문제목
                        .artclTitlEn(article.getArtclTitlEn()) // 영문제목
                        .artclCtt(article.getArtclCtt())
                        .ancCtt(article.getAncMentCtt())//앵커맨트
                        .rptrId(article.getRptrId()) //기자 아이디
                        .rptrNm(article.getRptrNm()) //기자 명
                        .deptCd(article.getDeptCd())
                        .artclReqdSec(articleCttTime) //기사 소요시간
                        .ancReqdSec(ancCttTime) //앵커기사 소요시간
                        .newsAcumTime(newsAcumTime) //누적시간
                        .build();


                //빌드된 PrompterCueSheetDTO를 PrompterCueSheetDTO List에 add
                prompterCueSheetDTOList.add(prompterCueSheetDTO);
            }
            ++ord;
        }

        //PrompterCueSheetDTO List return
        return prompterCueSheetDTOList;
    }

    //프롬프트로 보내줄 큐시트를 조회[단건 : 조건 큐시트 아이디]
    /*public CueSheet findCueSheet(Long cs_id) {

        Optional<CueSheet> cueSheet = cueSheetRepository.findByCue(cs_id);

        if (cueSheet.isPresent() == false) {
            throw new ResourceNotFoundException("큐시트를 찾을 수 없습니다. 큐시트 아이디 : " + cs_id);
        }

        return cueSheet.get();
    }*/
}

    /*public BooleanBuilder getSearchCue(String rd_id, String play_seq, String cued_seq, String vplay_seq, String vcued_seq,
                                       String del_yn, String ch_div_cd, String usr_id, String token, String usr_ip,
                                       String format, String lang, String os_type){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QCueSheet qCueSheet = QCueSheet.cueSheet;

        //프로그램 아이디가 검색조건으로 들어온 경우.
        //String 타입으로 들어온 프로그램 아이디를 Long으로 변환후 검색조건 추가.
        if (rd_id != null && rd_id.trim().isEmpty() == false){
            Long programId = Long.parseLong(rd_id.trim());
            booleanBuilder.and(qCueSheet.program.brdcPgmId.eq(programId));
        }
        //삭제 여부값이 검색조건으로 들어온 경우.
        if (del_yn != null && del_yn.trim().isEmpty() == false){
            booleanBuilder.and(qCueSheet.delYn.eq(del_yn));
        }else {
            booleanBuilder.and(qCueSheet.delYn.eq("N"));
        }
        //채널 구분 코드가 검색조건으로 들어온 경우.
        if (ch_div_cd != null && ch_div_cd.trim().isEmpty() ==false){
            booleanBuilder.and(qCueSheet.chDivCd.eq(ch_div_cd));
        }
        //사용자 아이디가 검색조건으로 들어온 경우.
        if (usr_id != null && usr_id.trim().isEmpty() == false){
            booleanBuilder.and(qCueSheet.inputrId.eq(usr_id));
        }

        return booleanBuilder;

    }*/

        /*BooleanBuilder booleanBuilder = getSearch( sdate,  edate);

        List<CueSheet> cueSheets = (List<CueSheet>) cueSheetRepository.findAll(booleanBuilder);

        AnsToTaker taker = new AnsToTaker();
        taker.SetCuesheet(cueSheets);
        List<ParentProgramDTO> cueSheetDTOList = taker.ToSTringXML();
        System.out.println(" xml : " + cueSheetDTOList );

        TakerCueSheetDataDTO takerCueSheetDTO = new TakerCueSheetDataDTO();
        takerCueSheetDTO.setParentProgramDTOList(cueSheetDTOList);

        String xml = JAXBXmlHelper.marshal(takerCueSheetDTO, TakerCueSheetDataDTO.class);

        System.out.println(" xml : " + xml );

        return xml;*/
/*
        //조회된 일일편성 엔티티 프롬프터DTO로 변환
        public List<PrompterProgramDTO> conversionDailyPgm(List<DailyProgramDTO> dailyProgramDTOList){

            List<PrompterProgramDTO> prompterProgramDTOList = new ArrayList<>(); //리턴시켜줄 프롬프터 프로그램 리스트 생성

            for (DailyProgramDTO dailyProgramDTO : dailyProgramDTOList){

                ProgramDTO programDTO = dailyProgramDTO.getProgram();
                String brdcPgmId = "";

                if (ObjectUtils.isEmpty(programDTO) == false){
                    brdcPgmId = programDTO.getBrdcPgmId();
                }

                PrompterProgramDTO program = PrompterProgramDTO.builder()
                        .brdcPgmId(brdcPgmId)
                        .proNm(dailyProgramDTO.getBrdcPgmNm())
                        .onAirDate(dailyProgramDTO.getBrdcDt())
                        .startTime(dailyProgramDTO.getBrdcStartTime())
                        .endTime(dailyProgramDTO.getBrdcEndClk())
                        .displaySeq(null)
                        .csId("")
                        .csSpendTime("")
                        .nodstatusNm("")
                        .build();

                prompterProgramDTOList.add(program);
            }

            return prompterProgramDTOList;

        }*/

            /*for (DailyProgramDTO dailyProgram : dailyProgramDTOList){

                    //일일편성 방송일시 비교를 위해 int로 변환
                    int dailyBrdcDt = dateToint(dailyProgram.getBrdcDt());

                    //일일편성 방송시작시간 비교를 위해 int로 변환
                    int dailyStartTime = timeToInt(dailyProgram.getBrdcStartTime());

                    //큐시트 방송일시가 일일편성 방송일시보다 크면.
                    if (cueBrdcDt > dailyBrdcDt){
                    //프롬프터 큐시트목록 xml변환[ 큐시트 ]
                    prompterProgramDTOList.add(cueToPrompter(cueSheet));
                    }
                    //일일편성 방송일시가 큐시트 방송일시보다 크면.
                    if (cueBrdcDt < dailyBrdcDt){
        prompterProgramDTOList.add(dailyToPrompter(dailyProgram));
        }
        //큐시트 방송일시와 일일편성 방송일시와 같으면
        if (cueBrdcDt != 0 && dailyBrdcDt != 0 && cueBrdcDt == dailyBrdcDt){
        if (cueStartTime > dailyStartTime){//큐시트 시작시간이 크면 일일편성출력
        //프롬프터 큐시트목록 xml변환[ 일일편성 ]
        prompterProgramDTOList.add(dailyToPrompter(dailyProgram));
        }
        if (cueStartTime < dailyStartTime){//일일편성 시작시간이 크면 큐시트출력
        //프롬프터 큐시트목록 xml변환[ 큐시트 ]
        prompterProgramDTOList.add(cueToPrompter(cueSheet));
        }
        if (cueStartTime == dailyStartTime){//같으면 큐시트,일일편성 둘다 출력하되 큐시트 먼저
        //프롬프터 큐시트목록 xml변환[ 큐시트 ]
        prompterProgramDTOList.add(cueToPrompter(cueSheet));
        //프롬프터 큐시트목록 xml변환[ 일일편성 ]
        prompterProgramDTOList.add(dailyToPrompter(dailyProgram));
        }

        }

        }*/