package com.gemiso.zodiac.app.appInterface;

import com.gemiso.zodiac.app.anchorCap.AnchorCap;
import com.gemiso.zodiac.app.appInterface.codeDTO.*;
import com.gemiso.zodiac.app.appInterface.prompterCueDTO.*;
import com.gemiso.zodiac.app.appInterface.prompterProgramDTO.PrompterProgramDTO;
import com.gemiso.zodiac.app.appInterface.prompterProgramDTO.PrompterProgramDataDTO;
import com.gemiso.zodiac.app.appInterface.prompterProgramDTO.PrompterProgramResultDTO;
import com.gemiso.zodiac.app.appInterface.prompterProgramDTO.PrompterProgramXML;
import com.gemiso.zodiac.app.appInterface.takerCueFindAllDTO.*;
import com.gemiso.zodiac.app.appInterface.takerCueRefreshDTO.TakerCueRefreshDataDTO;
import com.gemiso.zodiac.app.appInterface.takerCueRefreshDTO.TakerCueRefreshXML;
import com.gemiso.zodiac.app.appInterface.takerCueRefreshDTO.TakerSpareCueRefreshDataDTO;
import com.gemiso.zodiac.app.appInterface.takerCueRefreshDTO.TakerSpareCueRefreshXML;
import com.gemiso.zodiac.app.appInterface.takerProgramDTO.ParentProgramDTO;
import com.gemiso.zodiac.app.appInterface.takerProgramDTO.TakerProgramDTO;
import com.gemiso.zodiac.app.appInterface.takerProgramDTO.TakerProgramDataDTO;
import com.gemiso.zodiac.app.appInterface.takerProgramDTO.TakerProgramResultDTO;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.dto.ArticleCueItemDTO;
import com.gemiso.zodiac.app.articleCap.ArticleCap;
import com.gemiso.zodiac.app.articleMedia.ArticleMedia;
import com.gemiso.zodiac.app.articleMedia.ArticleMediaRepository;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaDTO;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaMapper;
import com.gemiso.zodiac.app.code.Code;
import com.gemiso.zodiac.app.code.CodeRepository;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.cueSheet.CueSheetRepository;
import com.gemiso.zodiac.app.cueSheet.CueSheetService;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetDTO;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetFindAllDTO;
import com.gemiso.zodiac.app.cueSheet.mapper.CueSheetMapper;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItemRepository;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItemService;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemDTO;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemMapper;
import com.gemiso.zodiac.app.cueSheetItemSymbol.CueSheetItemSymbol;
import com.gemiso.zodiac.app.cueSheetItemSymbol.CueSheetItemSymbolRepository;
import com.gemiso.zodiac.app.cueSheetMedia.CueSheetMedia;
import com.gemiso.zodiac.app.cueSheetTemplate.CueSheetTemplate;
import com.gemiso.zodiac.app.dailyProgram.dto.DailyProgramDTO;
import com.gemiso.zodiac.app.issue.Issue;
import com.gemiso.zodiac.app.program.Program;
import com.gemiso.zodiac.app.program.dto.ProgramDTO;
import com.gemiso.zodiac.app.symbol.Symbol;
import com.gemiso.zodiac.core.helper.DateChangeHelper;
import com.gemiso.zodiac.core.helper.JAXBXmlHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.xml.bind.annotation.XmlElement;
import java.text.ParseException;
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
    private final ArticleMediaRepository articleMediaRepository;

    private final ArticleMediaMapper articleMediaMapper;
    private final CueSheetMapper cueSheetMapper;

    private final CueSheetService cueSheetService;
    private final CueSheetItemService cueSheetItemService;

    private final DateChangeHelper dateChangeHelper;


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

        if (CollectionUtils.isEmpty(dailyProgramDTOList)) {

            for (CueSheetDTO cueSheet : cueSheetDTOList) {

                //프롬프터 큐시트목록 xml변환[ 큐시트 ]
                parentProgramDTOList.add(cueToTaker(cueSheet));

            }
        } else {

            for (DailyProgramDTO dailyProgramDTO : dailyProgramDTOList) {

                //일일편성 방송일시 비교를 위해 int로 변환
                int dailyBrdcDt = dateToint(dailyProgramDTO.getBrdcDt());

                //일일편성 방송시작시간 비교를 위해 int로 변환
                int dailyStartTime = timeToInt(dailyProgramDTO.getBrdcStartTime());

                Iterator<CueSheetDTO> iter = cueSheetDTOList.listIterator();
                while (iter.hasNext()) {

                    CueSheetDTO cueSheet = iter.next();

                    String date = cueSheet.getBrdcDt(); //방송일자
                    int cueBrdcDt = 0;
                    if (date != null && date.trim().isEmpty() == false) { //방송일자 데이터가 있을경우
                        cueBrdcDt = dateToint(date);
                    }
                    String time = cueSheet.getBrdcStartTime(); //방송 시작 시간
                    int cueStartTime = 0;
                    if (time != null && time.trim().isEmpty() == false) { //방송시작시간이 있을경우
                        cueStartTime = timeToInt(time);
                    }

                    //큐시트 방송일시가 일일편성 방송일시보다 크면.
                    if (cueBrdcDt < dailyBrdcDt) {

                        if (parentProgramDTOList.contains(cueToTaker(cueSheet)) == false) { //최초한번만
                            //프롬프터 큐시트목록 xml변환[ 큐시트 ]
                            parentProgramDTOList.add(cueToTaker(cueSheet));
                        }
                    } else if (cueBrdcDt == dailyBrdcDt) {//큐시트 방송일시가 일일편성 방송일시와 같은경우.

                        if (cueStartTime < dailyStartTime) {//큐시트 시작시간이 크면 일일편성출력
                            if (parentProgramDTOList.contains(cueToTaker(cueSheet)) == false) { //최초한번만
                                //프롬프터 큐시트목록 xml변환[ 큐시트 ]
                                parentProgramDTOList.add(cueToTaker(cueSheet));
                            }
                        }

                    }
                }

                //프롬프터 큐시트목록 xml변환[ 일일편성 ]
                parentProgramDTOList.add(dailyToTaker(dailyProgramDTO));

            }

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
                .rdId(cueSheet.getCueId()) //큐시트 아이디
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
                .cueId(cueSheet.getCueId())
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
                .rdId(null)
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
                .cueId(null)
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

    public String takerPgmToXmlOne(ParentProgramDTO parentProgramDTO) {

        List<ParentProgramDTO> parentProgramDTOList = new ArrayList<>();
        parentProgramDTOList.add(parentProgramDTO);

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

    //큐시트 조회
    public CueSheet findCueSheet(Long cueId, String del_yn) {

        //조회된 큐시트값없을경우 에러를 안내기 위해 빈 모델 리턴
        CueSheet cueSheet = new CueSheet();

        //테이커큐시트 상세조회
        Optional<CueSheet> cueSheetEntity = cueSheetRepository.findTakerCue(cueId, del_yn);

        if (cueSheetEntity.isPresent() == false) { //조회된 큐시트가 없으면 return 빈모델리스트 = 에러가 나지 않게 설정
            return cueSheet;
        }

        return cueSheetEntity.get();
    }

    //테이커 큐시트 조회
    public TakerCueSheetDataDTO cuefindAll(String rd_id, String play_seq, String cued_seq, String vplay_seq, String vcued_seq,
                                           String del_yn, String ch_div_cd, String usr_id, String token, String usr_ip,
                                           String format, String lang, String os_type) {

        TakerCueSheetDataDTO takerCueSheetDataDTO = new TakerCueSheetDataDTO();

        //큐시트 아이티와 삭제여부값 예외 처리[아이디가 String 타입으로 들어오기 때문에 Long값으로 변환.]
        //Long cueId = 60L;
        Long cueId = null;
        if (rd_id != null && rd_id.trim().isEmpty() == false) {
            cueId = Long.parseLong(rd_id);
        }
        //큐시트 아이티와 삭제여부값 예외 처리[여부값이 안들어 올시, N값 설정]
        if (del_yn == null || del_yn.trim().isEmpty()) {
            del_yn = "N";
        }

        CueSheet cueSheetEntity = findCueSheet(cueId, del_yn);

        //조회된 큐시트 정보로 TakerCueSheetDTO 리스트 빌드
        List<TakerCueSheetDTO> takerCueSheetDTOList = cueSheetToTakerCueSheet(cueSheetEntity);

        //예비 큐시트 아이템 정보를 taker정보로 빌드.
        List<TakerCueSheetSpareDTO> takerCueSheetSpareDTO = findSpareCueSheet(cueId, del_yn, cueSheetEntity);

        takerCueSheetDataDTO.setTakerCueSheetDTO(takerCueSheetDTOList);//기본 큐시트아이템 정보 set
        takerCueSheetDataDTO.setTakerCueSheetSpareDTO(takerCueSheetSpareDTO);//예비 큐시트 아이템 정보 set

        return takerCueSheetDataDTO;
    }

    //테이커 큐시트 예비 큐시트 조회
    public List<TakerCueSheetSpareDTO> findSpareCueSheet(Long cueId, String del_yn, CueSheet cueSheet) {

        List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findSpareCue(cueId, del_yn);

        List<TakerCueSheetSpareDTO> takerCueSheetSpareDTOList = new ArrayList<>();

        int rdSeq = 1; //순번값 set

        for (CueSheetItem cueSheetItem : cueSheetItemList) {
            Article article = cueSheetItem.getArticle(); //큐시트 아이템에 기사정보get
            Issue issue = new Issue();

            Long cueItemId = cueSheetItem.getCueItemId();
            //큐시트 아이템 방송아이콘 List 조회
            List<CueSheetItemSymbol> cueSheetItemSymbolList = cueSheetItemSymbolRepository.findSymbol(cueItemId);

            //값을 셋팅하여 xml노드를 만들어줄 비디오 테이커 노드 리스트
            TakerCueSheetVideoSymbolDTO takerCueSheetVideoSymbolDTO = new TakerCueSheetVideoSymbolDTO();
            //값을 셋팅하여 xml노드를 만들어줄 오디오 테이커 노드 리스트
            TakerCueSheetAudioSymbolDTO takerCueSheetAudioSymbolDTO = new TakerCueSheetAudioSymbolDTO();

            List<TakerCueSheetSymbolListDTO> videoSymbolList = new ArrayList<>();
            List<TakerCueSheetSymbolListDTO> audioSymbolList = new ArrayList<>();


            for (CueSheetItemSymbol cueSheetItemSymbol : cueSheetItemSymbolList) {
                Symbol symbol = cueSheetItemSymbol.getSymbol();

                if (ObjectUtils.isEmpty(symbol)) {
                    continue;
                }

                String typeCode = symbol.getTypCd();

                if ("audio_icons".equals(typeCode)) {

                    String symbolId = symbol.getSymbolId();
                    TakerCueSheetSymbolListDTO takerCueSheetSymbolListDTO = TakerCueSheetSymbolListDTO.builder()
                            .symbolId(symbolId).build();

                    audioSymbolList.add(takerCueSheetSymbolListDTO);

                } else if ("video_icons".equals(typeCode)) {

                    String symbolId = symbol.getSymbolId();
                    TakerCueSheetSymbolListDTO takerCueSheetSymbolListDTO = TakerCueSheetSymbolListDTO.builder()
                            .symbolId(symbolId).build();

                    videoSymbolList.add(takerCueSheetSymbolListDTO);
                }
            }
            //비디오 심볼정보 노드리스트에 추가
            takerCueSheetVideoSymbolDTO.setTakerCueSheetSymbolListDTOList(videoSymbolList);
            //오디오 심볼정보 노드리스트에 추가
            takerCueSheetAudioSymbolDTO.setTakerCueSheetSymbolListDTOList(audioSymbolList);


            //cmDivCd, cmDivCd 값 구하기 [채널값으로 심볼에 들어가는 NS-1, NS-2, NS-3 값 구하기]
            String returnSymbolId = "";
            String returnSymbolNm = "";
            for (CueSheetItemSymbol cueSheetItemSymbol : cueSheetItemSymbolList) {
                Symbol symbol = cueSheetItemSymbol.getSymbol(); //큐시트아이템에 포함된 심볼  get

                if (ObjectUtils.isEmpty(symbol)) {  //심볼이 있을경우
                    String symbolId = symbol.getSymbolId(); //심볼아이디
                    String symbolNm = symbol.getSymbolNm(); //심볼 명

                    switch (symbolId) { // VNS1, VNS2, VNS3 채널로 표기된 심볼이 들어가 있을경우 값 셋팅
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

            if (ObjectUtils.isEmpty(article)) { //기사가 포함이 안된 큐시트 아이템 일시

                //큐시트 아이템 비디오 정보 get
                List<CueSheetMedia> cueSheetMediaList = cueSheetItem.getCueSheetMedia();
                //큐시트 아이템 비디오 정보를 큐시트 테이커 비디오 DTO로 set
                TakerCueSheetVideoDTO takerCueSheetVideoDTOList = getVideoDTOList(cueSheetMediaList);

                //프로그램 아이디 get null에러 방지
                Program program = cueSheet.getProgram();
                String brdcPgmId = "";
                if (ObjectUtils.isEmpty(program) == false) {
                    brdcPgmId = program.getBrdcPgmId();
                }

                //템플릿이 있을경우 템플릿아이디 추가
                CueSheetTemplate cueSheetTemplate = cueSheetItem.getCueSheetTemplate();
                Long cueTmpltId = null;
                if (ObjectUtils.isEmpty(cueSheetTemplate) == false) {
                    cueTmpltId = cueSheetTemplate.getCueTmpltId();
                }

                //테이커큐시트 정보 큐시트 엔티티 정보로 빌드
                TakerCueSheetSpareDTO takerCueSheetDTO = TakerCueSheetSpareDTO.builder()
                        .rdId(cueSheetItem.getCueItemId())
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
                        .takerCueSheetVideoSymbolDTO(takerCueSheetVideoSymbolDTO)
                        .takerCueSheetAudioSymbolDTO(takerCueSheetAudioSymbolDTO)
                        .cueId(cueSheet.getCueId()) //큐시트 아이디 Topic에서 사용
                        .cueTmpltId(cueTmpltId)
                        .cueItemId(cueSheetItem.getCueItemId())
                        .build();

                takerCueSheetSpareDTOList.add(takerCueSheetDTO); //빌드된 큐시트테이커DTO 리턴할 큐시트테이커 리스트에 add

                ++rdSeq;

            } else {

                //기사에서 비디오 정보 get
                List<ArticleMedia> cueSheetMediaArticleList = article.getArticleMedia();
                //기사 비디오 정보를 큐시트 테이커 비디오 DTO로 set
                TakerCueSheetVideoDTO takerArticleVideoDTOList = getArticleVideoDTOList(cueSheetMediaArticleList);


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
                        .rdId(cueSheetItem.getCueItemId())
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
                        .artclFrmCd(article.getArtclTypDtlCd())
                        .artclFrmNm(article.getArtclTypDtlCdNm())
                        .artclFldCd(article.getArtclFldCd())
                        .artclFldNm(article.getArtclFldCdNm())
                        .artclTitl(article.getArtclTitl())
                        .artclTitlEn(article.getArtclTitlEn())
                        .rptrNm(article.getRptrNm())
                        .deptCd(article.getDeptCd())
                        .deptNm(article.getDeptNm())
                        .artclReqdSec(article.getArtclCttTime())//기사 소요시간 초
                        .ancReqdSec(article.getAncMentCttTime())//앵커 소요시간 초
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
                        .inputDtm(dateChangeHelper.dateToStringNormal(article.getInputDtm())) //Date(yyyy-MM-dd HH:mm:ss)형식의 입력일시를 String으로 변환
                        .takerCueSheetVideoDTO(takerArticleVideoDTOList)//???
                        .takerCueSheetVideoSymbolDTO(takerCueSheetVideoSymbolDTO)
                        .takerCueSheetAudioSymbolDTO(takerCueSheetAudioSymbolDTO)
                        .cueId(cueSheet.getCueId())
                        .cueItemId(cueSheetItem.getCueItemId())
                        .cueTmpltId(null)
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

            //값을 셋팅하여 xml노드를 만들어줄 비디오 테이커 노드 리스트
            TakerCueSheetVideoSymbolDTO takerCueSheetVideoSymbolDTO = new TakerCueSheetVideoSymbolDTO();
            //값을 셋팅하여 xml노드를 만들어줄 오디오 테이커 노드 리스트
            TakerCueSheetAudioSymbolDTO takerCueSheetAudioSymbolDTO = new TakerCueSheetAudioSymbolDTO();

            List<TakerCueSheetSymbolListDTO> videoSymbolList = new ArrayList<>();
            List<TakerCueSheetSymbolListDTO> audioSymbolList = new ArrayList<>();
            for (CueSheetItemSymbol cueSheetItemSymbol : cueSheetItemSymbolList) {
                Symbol symbol = cueSheetItemSymbol.getSymbol();

                if (ObjectUtils.isEmpty(symbol)) {
                    continue;
                }

                String typeCode = symbol.getTypCd();

                if ("audio_icons".equals(typeCode)) {

                    String symbolId = symbol.getSymbolId();
                    TakerCueSheetSymbolListDTO takerCueSheetSymbolListDTO = TakerCueSheetSymbolListDTO.builder()
                            .symbolId(symbolId).build();

                    audioSymbolList.add(takerCueSheetSymbolListDTO);

                } else if ("video_icons".equals(typeCode)) {

                    String symbolId = symbol.getSymbolId();
                    TakerCueSheetSymbolListDTO takerCueSheetSymbolListDTO = TakerCueSheetSymbolListDTO.builder()
                            .symbolId(symbolId).build();

                    videoSymbolList.add(takerCueSheetSymbolListDTO);
                }
            }

            //비디오 심볼정보 노드리스트에 추가
            takerCueSheetVideoSymbolDTO.setTakerCueSheetSymbolListDTOList(videoSymbolList);
            //오디오 심볼정보 노드리스트에 추가
            takerCueSheetAudioSymbolDTO.setTakerCueSheetSymbolListDTOList(audioSymbolList);

            //cmDivCd, cmDivCd 값 구하기 [채널값으로 심볼에 들어가는 NS-1, NS-2, NS-3 값 구하기]
            String returnSymbolId = "";
            String returnSymbolNm = "";
            for (CueSheetItemSymbol cueSheetItemSymbol : cueSheetItemSymbolList) {
                Symbol symbol = cueSheetItemSymbol.getSymbol(); //큐시트아이템에 포함된 심볼  get

                if (ObjectUtils.isEmpty(symbol) == false) {  //심볼이 있을경우
                    String symbolId = symbol.getSymbolId(); //심볼아이디
                    String symbolNm = symbol.getSymbolNm(); //심볼 명

                    switch (symbolId) { // VNS1, VNS2, VNS3 채널로 표기된 심볼이 들어가 있을경우 값 셋팅
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


            if (ObjectUtils.isEmpty(article)) { //기사가 포함이 안된 큐시트 아이템 일시

                //큐시트 아이템 비디오 정보 get
                List<CueSheetMedia> cueSheetMediaList = cueSheetItem.getCueSheetMedia();
                //큐시트 아이템 비디오 정보를 큐시트 테이커 비디오 DTO로 set
                TakerCueSheetVideoDTO takerCueSheetVideoDTOList = getVideoDTOList(cueSheetMediaList);


                //테이커큐시트 정보 큐시트 엔티티 정보로 빌드
                TakerCueSheetDTO takerCueSheetDTO = buildTakerCue(cueSheet, cueSheetItem, rdSeq, returnSymbolId
                        , returnSymbolNm, takerCueSheetVideoDTOList, takerCueSheetVideoSymbolDTO, takerCueSheetAudioSymbolDTO);

                takerCueSheetDTOList.add(takerCueSheetDTO); //빌드된 큐시트테이커DTO 리턴할 큐시트테이커 리스트에 add

                rdSeq++; //순서값 +

            } else { //기사가 포함된 큐시트 아이템 일시

                //기사에서 비디오 정보 get
                List<ArticleMedia> cueSheetMediaArticleList = article.getArticleMedia();
                //기사 비디오 정보를 큐시트 테이커 비디오 DTO로 set
                TakerCueSheetVideoDTO takerArticleVideoDTOList = getArticleVideoDTOList(cueSheetMediaArticleList);

                //테이커큐시트 정보 큐시트 엔티티 정보로 빌드
                TakerCueSheetDTO takerCueSheetDTO = buildTakerCueArticle(cueSheet, cueSheetItem, rdSeq, returnSymbolId
                        , returnSymbolNm, takerArticleVideoDTOList, article, takerCueSheetVideoSymbolDTO, takerCueSheetAudioSymbolDTO);

                takerCueSheetDTOList.add(takerCueSheetDTO); //빌드된 큐시트테이커DTO 리턴할 큐시트테이커 리스트에 add

                rdSeq++; //순서값 +

            }
        }

        return takerCueSheetDTOList;
    }

    //테이커큐시트 정보 큐시트 엔티티 정보로 빌드( 기사 x)
    public TakerCueSheetDTO buildTakerCue(CueSheet cueSheet, CueSheetItem cueSheetItem, int rdSeq
            , String returnSymbolId, String returnSymbolNm, TakerCueSheetVideoDTO takerCueSheetVideoDTO
            , TakerCueSheetVideoSymbolDTO takerCueSheetVideoSymbolDTO, TakerCueSheetAudioSymbolDTO takerCueSheetAudioSymbolDTO) {

        //프로그램 아이디 get null에러 방지
        Program program = cueSheet.getProgram();
        String brdcPgmId = "";
        if (ObjectUtils.isEmpty(program) == false) {
            brdcPgmId = program.getBrdcPgmId();
        }

        //템플릿이 있을경우 템플릿아이디 추가
        CueSheetTemplate cueSheetTemplate = cueSheetItem.getCueSheetTemplate();
        Long cueTmpltId = null;
        if (ObjectUtils.isEmpty(cueSheetTemplate) == false) {
            cueTmpltId = cueSheetTemplate.getCueTmpltId();
        }

        //테이커큐시트 정보 큐시트 엔티티 정보로 빌드
        TakerCueSheetDTO takerCueSheetDTO = TakerCueSheetDTO.builder()
                .rdId(cueSheetItem.getCueItemId())
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
                .artclTitl(cueSheetItem.getCueItemTitl()) //큐시트 아이템 제목
                .artclTitlEn(cueSheetItem.getCueItemTitlEn()) //큐시트 아이템 영어 제목
                .takerCueSheetVideoDTO(takerCueSheetVideoDTO)//???
                .takerCueSheetVideoSymbolDTO(takerCueSheetVideoSymbolDTO)
                .takerCueSheetAudioSymbolDTO(takerCueSheetAudioSymbolDTO)
                .cueId(cueSheet.getCueId()) //큐시트 아이디 Topic에서 사용
                .cueTmpltId(cueTmpltId)
                .cueItemId(cueSheetItem.getCueItemId())
                //.artclTitl()
                //.artclTitlEn()
                .build();

        return takerCueSheetDTO;
    }

    //테이커큐시트 정보 큐시트 엔티티 정보로 빌드( 기사 o)
    public TakerCueSheetDTO buildTakerCueArticle(CueSheet cueSheet, CueSheetItem cueSheetItem, int rdSeq
            , String returnSymbolId, String returnSymbolNm, TakerCueSheetVideoDTO takerCueSheetVideoDTO, Article article
            , TakerCueSheetVideoSymbolDTO takerCueSheetVideoSymbolDTO, TakerCueSheetAudioSymbolDTO takerCueSheetAudioSymbolDTO) {

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
                .rdId(cueSheetItem.getCueItemId())
                .rdSeq(rdSeq)
                .chDivCd(cueSheet.getChDivCd())// 채널구분코드
                .cueDivCdNm(cueSheet.getCueDivCdNm())//채널구분코드 명
                .rdOrd(cueSheetItem.getCueItemOrd())//큐시트 아이템 순번
                .rdOrdMrk(cueSheetItem.getCueItemOrdCd())//표시되는 순번(알파벳? 숫자 같이 들어가 있고 안들어가는 부분이 있음)
                .rdDtlDivCd(cueSheetItem.getCueItemDivCd()) //큐시트아이템 구분 코드
                .mcStCd(cueSheetItem.getBrdcStCd()) //방송상태코드
                .cmDivCd(returnSymbolId)//심볼 아이디 (채널명) ex VNS1, VNS2, VNS3
                .cmDivNm(returnSymbolNm)//심볼 아이디 명 (채널명) ex NS-1, NS-2, NS-3
                .rdDtlDivNm(cueSheetItem.getCueItemDivCdNm())//큐시트아이템 구분 코드 명
                .mcStNm(cueSheetItem.getBrdcStCdNm())//방송상태 명
                .artclId(article.getArtclId())
                .artclFrmCd(article.getArtclTypDtlCd())
                .artclFrmNm(article.getArtclTypDtlCdNm())
                .artclFldCd(article.getArtclFldCd())
                .artclFldNm(article.getArtclFldCdNm())
                .artclTitl(article.getArtclTitl())
                .artclTitlEn(article.getArtclTitlEn())
                .rptrNm(article.getRptrNm())
                .deptCd(article.getDeptCd())
                .deptNm(article.getDeptNm())
                .artclReqdSec(article.getArtclCttTime())//기사 소요시간 초
                .ancReqdSec(article.getAncMentCttTime())//앵커 소요시간 초
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
                .inputDtm(dateChangeHelper.dateToStringNormal(article.getInputDtm())) //Date(yyyy-MM-dd HH:mm:ss)형식의 입력일시를 String으로 변환
                //.newsAcumTime(newsAcumTime)//누적시간
                .takerCueSheetVideoDTO(takerCueSheetVideoDTO)//???
                .takerCueSheetVideoSymbolDTO(takerCueSheetVideoSymbolDTO)
                .takerCueSheetAudioSymbolDTO(takerCueSheetAudioSymbolDTO)
                .cueId(cueSheet.getCueId())
                .cueTmpltId(null)
                .cueItemId(cueSheetItem.getCueItemId())
                .build();


        return takerCueSheetDTO;

    }

    //큐시트 아이템 비디오 정보 get
    public TakerCueSheetVideoDTO getVideoDTOList(List<CueSheetMedia> cueSheetMediaList) {

        TakerCueSheetVideoDTO returnDTO = new TakerCueSheetVideoDTO(); //리턴할 비디오 DTO 리스트
        List<TakerCueSheetVideoClipDTO> takerCueSheetVideoDTOList = new ArrayList<>();

        //Mam되면 수정
        for (CueSheetMedia cueSheetMedia : cueSheetMediaList) {//큐시트 아이템에 포함된 큐시트 미디어 정보 get

            //테이커 비디오 정보 빌드
            TakerCueSheetVideoClipDTO takerCueSheetVideoDTO = TakerCueSheetVideoClipDTO.builder()
                    .title(cueSheetMedia.getCueMediaTitl()) //미디어 제목
                    .playout_id("") // clip Id
                    .duration(cueSheetMedia.getMediaDurtn()) // 미디어 길이
                    .build();

            takerCueSheetVideoDTOList.add(takerCueSheetVideoDTO);

        }
        returnDTO.setTakerCueSheetVideoClipDTO(takerCueSheetVideoDTOList);


       /* TakerCueSheetVideoClipDTO takerCueSheetVideoClipDTO = TakerCueSheetVideoClipDTO.builder()
                .title("media title") //미디어 제목
                .playout_id("11") // clip Id
                .seq("1")
                .duration("01:30") // 미디어 길이
                .build();


        //테이커 비디오 정보 빌드
        TakerCueSheetVideoDTO takerCueSheetVideoDTO = TakerCueSheetVideoDTO.builder()
                .takerCueSheetVideoClipDTO(takerCueSheetVideoClipDTO)
                .build();*/

        //returnDTOList.add(takerCueSheetVideoDTO);

        return returnDTO;
    }

    //큐시트 기사 비디오 정보 get
    public TakerCueSheetVideoDTO getArticleVideoDTOList(List<ArticleMedia> cueSheetMediaArticleList) {

        TakerCueSheetVideoDTO returnDTO = new TakerCueSheetVideoDTO(); //리턴할 비디오 DTO 리스트
        List<TakerCueSheetVideoClipDTO> takerCueSheetVideoDTOList = new ArrayList<>();

        Integer seq = 1;
        //Mam되면 수정
        for (ArticleMedia articleMedia : cueSheetMediaArticleList) {//큐시트 아이템에 포함된 큐시트 미디어 정보 get

            //테이커 비디오 정보 빌드
            TakerCueSheetVideoClipDTO takerCueSheetVideoDTO = TakerCueSheetVideoClipDTO.builder()
                    .title(articleMedia.getArtclMediaTitl()) //미디어 제목
                    .playout_id(articleMedia.getVideoId()) // clip Id
                    .duration(articleMedia.getMediaDurtn()) // 미디어 길이
                    .seq(seq)
                    .build();

            takerCueSheetVideoDTOList.add(takerCueSheetVideoDTO);

            seq++;

        }
        returnDTO.setTakerCueSheetVideoClipDTO(takerCueSheetVideoDTOList);


       /* TakerCueSheetVideoClipDTO takerCueSheetVideoClipDTO = TakerCueSheetVideoClipDTO.builder()
                .title("media title") //미디어 제목
                .playout_id("11") // clip Id
                .seq("1")
                .duration("01:30") // 미디어 길이
                .build();


        //테이커 비디오 정보 빌드
        TakerCueSheetVideoDTO takerCueSheetVideoDTO = TakerCueSheetVideoDTO.builder()
                .takerCueSheetVideoClipDTO(takerCueSheetVideoClipDTO)
                .build();*/

        //returnDTOList.add(takerCueSheetVideoDTO);

        return returnDTO;
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

        //ContentsDTO 데이터 set
        //takerCueSheetDataDTO.setTakerCueSheetDTO(takerCueSheetDTOList);

        //큐시트 아이템, 예비 큐시트 아이템 조회된 토탈 카운트 get
        List<TakerCueSheetDTO> takerCueSheetDTO = takerCueSheetDataDTO.getTakerCueSheetDTO();
        List<TakerCueSheetSpareDTO> takerCueSheetSpareDTO = takerCueSheetDataDTO.getTakerCueSheetSpareDTO();
        Long totalCount = takerCueSheetDTO.stream().count() + takerCueSheetSpareDTO.stream().count();

        takerCueSheetDataDTO.setTotalcount(totalCount);
        takerCueSheetDataDTO.setCurpage(0);
        takerCueSheetDataDTO.setRowcount(0);

        //xml 변환 DTO에 result, ContentsDTO set
        takerCueSheetXML.setData(takerCueSheetDataDTO);
        takerCueSheetXML.setResult(takerCueSheetResultDTO);

        //DTO TO XML 파싱
        String xml = JAXBXmlHelper.marshal(takerCueSheetXML, TakerCueSheetXML.class);

        return xml;

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
        //코드XML DTO에 set 시켜줄 ContentsDTO 빌드
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

        //ContentsDTO set code데이터
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

    //일일편성 큐시트목록 유니온 목록조회 목록을 프롬프터 형식의 데이터로 변환
    public List<PrompterProgramDTO> toPrompterDailyPgm(CueSheetFindAllDTO cueSheetFindAllDTO) {

        List<PrompterProgramDTO> prompterProgramDTOList = new ArrayList<>(); //리턴시켜줄 프롬프터 프로그램 리스트 생성

        List<CueSheetDTO> cueSheetDTOList = cueSheetFindAllDTO.getCueSheetDTO();
        List<DailyProgramDTO> dailyProgramDTOList = cueSheetFindAllDTO.getDailyProgramDTO();

        if (CollectionUtils.isEmpty(dailyProgramDTOList)) {

            for (CueSheetDTO cueSheet : cueSheetDTOList) {

                //프롬프터 큐시트목록 xml변환[ 큐시트 ]
                prompterProgramDTOList.add(cueToPrompter(cueSheet));

            }
        } else {

            for (DailyProgramDTO dailyProgramDTO : dailyProgramDTOList) {

                //일일편성 방송일시 비교를 위해 int로 변환
                int dailyBrdcDt = dateToint(dailyProgramDTO.getBrdcDt());

                //일일편성 방송시작시간 비교를 위해 int로 변환
                int dailyStartTime = timeToInt(dailyProgramDTO.getBrdcStartTime());

                Iterator<CueSheetDTO> iter = cueSheetDTOList.listIterator();
                while (iter.hasNext()) {

                    CueSheetDTO cueSheet = iter.next();

                    String date = cueSheet.getBrdcDt(); //방송일자
                    int cueBrdcDt = 0;
                    if (date != null && date.trim().isEmpty() == false) { //방송일자 데이터가 있을경우
                        cueBrdcDt = dateToint(date);
                    }
                    String time = cueSheet.getBrdcStartTime(); //방송 시작 시간
                    int cueStartTime = 0;
                    if (time != null && time.trim().isEmpty() == false) { //방송시작시간이 있을경우
                        cueStartTime = timeToInt(time);
                    }

                    //큐시트 방송일시가 일일편성 방송일시보다 크면.
                    if (cueBrdcDt < dailyBrdcDt) {

                        if (prompterProgramDTOList.contains(cueToPrompter(cueSheet)) == false) { //최초한번만
                            //프롬프터 큐시트목록 xml변환[ 큐시트 ]
                            prompterProgramDTOList.add(cueToPrompter(cueSheet));
                        }
                    } else if (cueBrdcDt == dailyBrdcDt) {//큐시트 방송일시가 일일편성 방송일시와 같은경우.

                        if (cueStartTime < dailyStartTime) {//큐시트 시작시간이 크면 일일편성출력
                            if (prompterProgramDTOList.contains(cueToPrompter(cueSheet)) == false) { //최초한번만
                                //프롬프터 큐시트목록 xml변환[ 큐시트 ]
                                prompterProgramDTOList.add(cueToPrompter(cueSheet));
                            }
                        }

                    }
                }

                //프롬프터 큐시트목록 xml변환[ 일일편성 ]
                prompterProgramDTOList.add(dailyToPrompter(dailyProgramDTO));

            }

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
                .rdId(cueSheet.getCueId())
                .chDivCd(cueSheet.getChDivCd())
                .brdcPgmId(brdcPgmId)
                .proNm(cueSheet.getBrdcPgmNm())
                .onAirDate(cueSheet.getBrdcDt())
                .startTime(cueSheet.getBrdcStartTime())
                .endTime(cueSheet.getBrdcEndTime())
                .aricleCount(articleCount)
                .brdcStCd(cueSheet.getCueStCd())
                .rdEditYn("")
                .cueId(cueSheet.getCueId())
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

        //Lsit<prompterProgramDTO>
        PrompterProgramDataDTO prompterProgramDataDTO = new PrompterProgramDataDTO();

        //success="true" msg="ok" 담는DTO
        PrompterProgramResultDTO prompterProgramResultDTO = new PrompterProgramResultDTO();

        //ContentsDTO set code데이터
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
    public PrompterCueSheetDataDTO getCuesheetService(Long cs_id) {

        //set Lsit<PrompterCueRefreshDTO>
        PrompterCueSheetDataDTO prompterCueSheetDataDTO = new PrompterCueSheetDataDTO();

        //CueSheetDTO cueSheetDTO = cueSheetService.find(cs_id); //프롬프트로 보내줄 큐시트를 조회[단건 : 조건 큐시트 아이디]
        CueSheet cueSheet = findCueSheet(cs_id, "N");

        //List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetDTO.getCueSheetItem(); //조회된 큐시트상세 정보에서 큐시트 아이템get*/

        //List<CueSheetItem> cueSheetItemList = cueSheetItemMapper.toEntityList(cueSheetItemDTOList);

        //List<CueSheetItem> cueSheetItemList = cueSheet.getCueSheetItem();

        // 조회된 큐시트 정보를 List<PrompterCueRefreshDTO>빌드 [기사정보가 있는 큐시트아이템 정보를 프롬프트DTO로 빌드 후 리턴]
        List<PrompterCueSheetDTO> prompterCueSheetDTOList = cueToPrompterCue(cueSheet);

        //큐시트 예비조회 및 DTO빌드
        List<PrompterSpareCueSheetDTO> prompterSpareCueSheetDTOList = cueToPrompterSpareCue(cueSheet);

        //큐시트 아이템 DTO 빌드 set
        prompterCueSheetDataDTO.setCueSheetDTO(prompterCueSheetDTOList);
        //예비 큐시트 아이템 DTO 빌드 set
        prompterCueSheetDataDTO.setPrompterSpareCueSheetDTOS(prompterSpareCueSheetDTOList);

        return prompterCueSheetDataDTO;

    }

    public String prompterCueSheetXml(PrompterCueSheetDataDTO prompterCueSheetDataDTO) {

        //프롬프터 큐시트 xml형식으로 변환할 DTO
        PrompterCueSheetXML prompterCueSheetXML = new PrompterCueSheetXML();

        //success="true" msg="ok" 담는DTO
        PrompterCueSheetResultDTO prompterCueSheetResultDTO = new PrompterCueSheetResultDTO();

        //초회된 큐시트 아이템 총 갯수
        List<PrompterCueSheetDTO> cueSheetDTO = prompterCueSheetDataDTO.getCueSheetDTO();
        List<PrompterSpareCueSheetDTO> SpareCueSheetDTOS = prompterCueSheetDataDTO.getPrompterSpareCueSheetDTOS();
        Long totalCount = cueSheetDTO.stream().count() + SpareCueSheetDTOS.stream().count();

        //result 데이터 set
        prompterCueSheetDataDTO.setTotalcount(totalCount);
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

    // 조회된 큐시트 정보를 List<PrompterCueRefreshDTO>빌드 [기사정보가 있는 큐시트아이템 정보를 프롬프트DTO로 빌드 후 리턴]
    public List<PrompterCueSheetDTO> cueToPrompterCue(CueSheet cueSheet) {

        Long cueId = cueSheet.getCueId();
        List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findByCueItemList(cueId);

        List<PrompterCueSheetDTO> prompterCueSheetDTOList = new ArrayList<>();

        //Integer newsAcumTime = 0; //누적시간
        int seq = 1;

        for (CueSheetItem cueSheetItem : cueSheetItemList) { //큐시트 아이템 PrompterCueSheetDTO리스트로 변환[기사(article)이 있는 아이템만 변환]

            Article article = cueSheetItem.getArticle(); //큐시스트 아이템에서 기사 get

            if (ObjectUtils.isEmpty(article)) { //기사 정보가 없으면 contiue [프롬프트에서 기사정보만 쓰임]

                //템플릿이 있을경우 템플릿아이디 추가
                CueSheetTemplate cueSheetTemplate = cueSheetItem.getCueSheetTemplate();
                Long cueTmpltId = null;
                if (ObjectUtils.isEmpty(cueSheetTemplate) == false) {
                    cueTmpltId = cueSheetTemplate.getCueTmpltId();
                }


                //조회된 cueSheetItem정보로 PrompterCueSheetDTO생성
                PrompterCueSheetDTO prompterCueSheetDTO = PrompterCueSheetDTO.builder()
                        .rdId(cueSheetItem.getCueItemId())
                        .rdSeq(seq)
                        .rdOrdMrk(cueSheetItem.getCueItemOrdCd())
                        .rdOrd(cueSheetItem.getCueItemOrd())
                        .openYn("")
                        .artclTitl(cueSheetItem.getCueItemTitl()) //국문제목
                        .artclTitlEn(cueSheetItem.getCueItemTitlEn()) // 영문제목
                        .artclCtt(cueSheetItem.getCueItemCtt())
                        //.newsAcumTime(newsAcumTime) //누적시간
                        .cueId(cueSheet.getCueId()) //Topic 사용 큐시트 아이디
                        .cueItemId(cueSheetItem.getCueItemId()) //Topic 사용 큐시트 아이템 아이디
                        .cueTmpltId(cueTmpltId)
                        .build();

                //빌드된 PrompterCueSheetDTO를 PrompterCueRefreshDTO List에 add
                prompterCueSheetDTOList.add(prompterCueSheetDTO);

                seq++;
            } else { //기사 아이템인 경우

                //Integer articleCttTime = article.getArtclCttTime(); // 기사 소요시간
                //Integer ancCttTime = article.getAncMentCttTime(); // 앵커 소요시간

                //String articleTypDtlCd = article.getArtclTypDtlCd(); // 기상 유형 상세 코드

                //기사&앵커자막 조회데이터 get
                List<ArticleCap> articleCapList = article.getArticleCap();
                List<AnchorCap> anchorCapList = article.getAnchorCap();
                //자박정보 set
                PrompterArticleCaps prompterArticleCap = getPrompterArticleCap(articleCapList);
                PrompterAnchorCaps prompterAnchorCap = getPrompterAnchorCap(anchorCapList);

                //기상 유형 상세 코드 가 apk,pk인 경우 기사 + 앵커 소요시간 add
                //if ("apk".equals(articleTypDtlCd) || "pk".equals(articleTypDtlCd)) {
                // 기사 소요시간 + 앵커 소요시간 + 누적시간 = 누적시간
                //    newsAcumTime += articleCttTime + ancCttTime;

                //기상 유형 상세 코드 가 telphone,smartphone,news_studio,broll,mng 인 경우 기사소요시간만 add
                //} else { // 앵커시간이 Null인경우
                //    newsAcumTime += articleCttTime;
                //}


                //조회된 cueSheet정보의 기사정보로 PrompterCueSheetDTO생성
                PrompterCueSheetDTO prompterCueSheetDTO = PrompterCueSheetDTO.builder()
                        .rdId(cueSheetItem.getCueItemId())
                        .rdSeq(seq)
                        .chDivCd(article.getChDivCd())
                        .rdOrd(cueSheetItem.getCueItemOrd()) //순번
                        .rdOrdMrk(cueSheetItem.getCueItemOrdCd())
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
                        .artclReqdSec(article.getArtclCttTime()) //기사 소요시간
                        .ancReqdSec(article.getAncMentCttTime()) //앵커기사 소요시간
                        //.newsAcumTime(newsAcumTime) //누적시간
                        .anchorCaps(prompterAnchorCap)//앵커자막
                        .articleCaps(prompterArticleCap)//기사자막
                        .cueId(cueSheet.getCueId())
                        .cueTmpltId(null)
                        .cueItemId(cueSheetItem.getCueItemId())
                        .build();


                //빌드된 PrompterCueSheetDTO를 PrompterCueRefreshDTO List에 add
                prompterCueSheetDTOList.add(prompterCueSheetDTO);

                seq++;
            }
        }

        //PrompterCueRefreshDTO List return
        return prompterCueSheetDTOList;
    }

    // 조회된 큐시트 정보를 List<PrompterCueRefreshDTO>빌드 [기사정보가 있는 큐시트아이템 정보를 프롬프트DTO로 빌드 후 리턴]
    public List<PrompterSpareCueSheetDTO> cueToPrompterSpareCue(CueSheet cueSheet) {

        Long cueId = cueSheet.getCueId();
        List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findSpareCue(cueId, "N");

        List<PrompterSpareCueSheetDTO> prompterSpareCueSheetDTOList = new ArrayList<>();

        //Integer newsAcumTime = 0; //누적시간
        int seq = 1;

        for (CueSheetItem cueSheetItem : cueSheetItemList) { //큐시트 아이템 PrompterCueSheetDTO리스트로 변환[기사(article)이 있는 아이템만 변환]

            Article article = cueSheetItem.getArticle(); //큐시스트 아이템에서 기사 get

            if (ObjectUtils.isEmpty(article)) { //기사 정보가 없으면 contiue [프롬프트에서 기사정보만 쓰임]

                //템플릿이 있을경우 템플릿아이디 추가
                CueSheetTemplate cueSheetTemplate = cueSheetItem.getCueSheetTemplate();
                Long cueTmpltId = null;
                if (ObjectUtils.isEmpty(cueSheetTemplate) == false) {
                    cueTmpltId = cueSheetTemplate.getCueTmpltId();
                }

                //조회된 cueSheetItem정보로 PrompterCueSheetDTO생성
                PrompterSpareCueSheetDTO prompterSpareCueSheetDTO = PrompterSpareCueSheetDTO.builder()
                        .rdId(cueSheetItem.getCueItemId())
                        .rdSeq(seq)
                        .rdOrdMrk(cueSheetItem.getCueItemOrdCd())
                        .rdOrd(cueSheetItem.getCueItemOrd())
                        .openYn("")
                        .artclTitl(cueSheetItem.getCueItemTitl()) //국문제목
                        .artclTitlEn(cueSheetItem.getCueItemTitlEn()) // 영문제목
                        .artclCtt(cueSheetItem.getCueItemCtt())
                        //.newsAcumTime(newsAcumTime) //누적시간
                        .cueId(cueSheet.getCueId())
                        .cueTmpltId(cueTmpltId)
                        .cueItemId(cueSheetItem.getCueItemId())
                        .build();

                //빌드된 PrompterCueSheetDTO를 PrompterCueRefreshDTO List에 add
                prompterSpareCueSheetDTOList.add(prompterSpareCueSheetDTO);

                seq++;
            } else { //기사 아이템인 경우

                //기사&앵커자막 조회데이터 get
                List<ArticleCap> articleCapList = article.getArticleCap();
                List<AnchorCap> anchorCapList = article.getAnchorCap();
                //자박정보 set
                PrompterArticleCaps prompterArticleCap = getPrompterArticleCap(articleCapList);
                PrompterAnchorCaps prompterAnchorCap = getPrompterAnchorCap(anchorCapList);


                //조회된 cueSheet정보의 기사정보로 PrompterCueSheetDTO생성
                PrompterSpareCueSheetDTO prompterSpareCueSheetDTO = PrompterSpareCueSheetDTO.builder()
                        .rdId(cueSheetItem.getCueItemId())
                        .rdSeq(seq)
                        .chDivCd(article.getChDivCd())
                        .rdOrd(cueSheetItem.getCueItemOrd()) //순번
                        .rdOrdMrk(cueSheetItem.getCueItemOrdCd())
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
                        .artclReqdSec(article.getArtclCttTime()) //기사 소요시간
                        .ancReqdSec(article.getAncMentCttTime()) //앵커기사 소요시간
                        //.newsAcumTime(newsAcumTime) //누적시간
                        .anchorCaps(prompterAnchorCap)//앵커자막
                        .articleCaps(prompterArticleCap)//기사자막
                        .cueId(cueSheet.getCueId())
                        .cueTmpltId(null)
                        .cueItemId(cueSheetItem.getCueItemId())
                        .build();


                //빌드된 PrompterCueSheetDTO를 PrompterCueRefreshDTO List에 add
                prompterSpareCueSheetDTOList.add(prompterSpareCueSheetDTO);

                seq++;
            }
        }

        //PrompterCueRefreshDTO List return
        return prompterSpareCueSheetDTOList;
    }

    //프롬프터 기사 자막정보 빌드후 리턴 
    public PrompterArticleCaps getPrompterArticleCap(List<ArticleCap> articleCapList) {

        PrompterArticleCaps prompterArticleCaps = new PrompterArticleCaps();
        List<PrompterArticleCapDTO> prompterArticleCapDTOList = new ArrayList<>();

        //들어온 리스트가 비어있으면 빈 어레이 리스트 리턴.
        if (CollectionUtils.isEmpty(articleCapList)) {
            return prompterArticleCaps;
        }

        //기사자막&방송아이콘 정보로 프롬프터에 사용되는 자막정보 빌드
        for (ArticleCap articleCap : articleCapList) {

            Symbol symbol = articleCap.getSymbol();

            PrompterArticleCapDTO prompterArticleCapDTO = PrompterArticleCapDTO.builder()
                    .artclCapId(articleCap.getArtclCapId())
                    .symbolId(symbol.getSymbolId())
                    .capCtt(articleCap.getCapCtt())
                    .lnNo(articleCap.getLnNo())
                    .lnOrd(articleCap.getLnOrd())
                    .build();

            prompterArticleCapDTOList.add(prompterArticleCapDTO);
        }
        prompterArticleCaps.setArticleCapList(prompterArticleCapDTOList);

        return prompterArticleCaps;

    }

    //프롬프터 앵커 자막정보 빌드후 리턴
    public PrompterAnchorCaps getPrompterAnchorCap(List<AnchorCap> anchorCapList) {

        PrompterAnchorCaps prompterAnchorCaps = new PrompterAnchorCaps();
        List<PrompterAnchorCapDTO> prompterAnchorCapDTOList = new ArrayList<>();

        //들어온 리스트가 비어있으면 빈 어레이 리스트 리턴.
        if (CollectionUtils.isEmpty(anchorCapList)) {
            return prompterAnchorCaps;
        }

        //앵커자막&방송아이콘 정보로 프롬프터에 사용되는 자막정보 빌드
        for (AnchorCap anchorCap : anchorCapList) {

            Symbol symbol = anchorCap.getSymbol();

            PrompterAnchorCapDTO prompterAnchorCapDTO = PrompterAnchorCapDTO.builder()
                    .artclCapId(anchorCap.getAnchorCapId())
                    .symbolId(symbol.getSymbolId())
                    .capCtt(anchorCap.getCapCtt())
                    .lnNo(anchorCap.getLnNo())
                    .lnOrd(anchorCap.getLnOrd())
                    .build();

            prompterAnchorCapDTOList.add(prompterAnchorCapDTO);
        }
        prompterAnchorCaps.setAnchorCapList(prompterAnchorCapDTOList);

        return prompterAnchorCaps;
    }


    //테이커 큐시트 아이템 Refresh
    public TakerCueRefreshDataDTO takerCueItemRefresh(Long rd_id, Integer rd_seq) {

        TakerCueRefreshDataDTO takerCueRefreshDataDTO = new TakerCueRefreshDataDTO();//리턴DTO

        //조회조건으로 들어온 큐시트 아이템 아이디로 큐시트 엔티티 조회
        //CueSheet cueSheet = findCueSheet(rd_id);

        //조회조건으로 들어온 큐시트 아이템 아이디로 큐시트 아이템 엔티티 조회
        CueSheetItem cueSheetItem = cueSheetItemService.cueItemFindOrFail(rd_id);

        //조회된 큐시트 아이템에서 큐시트 정보 get
        CueSheet cueSheet = cueSheetItem.getCueSheet();

        Article article = cueSheetItem.getArticle(); //큐시트 아이템에 기사정보get

        Long cueItemId = cueSheetItem.getCueItemId();
        //큐시트 아이템 방송아이콘 List 조회
        List<CueSheetItemSymbol> cueSheetItemSymbolList = cueSheetItemSymbolRepository.findSymbol(cueItemId);

        //값을 셋팅하여 xml노드를 만들어줄 비디오 테이커 노드 리스트
        TakerCueSheetVideoSymbolDTO takerCueSheetVideoSymbolDTO = new TakerCueSheetVideoSymbolDTO();
        //값을 셋팅하여 xml노드를 만들어줄 오디오 테이커 노드 리스트
        TakerCueSheetAudioSymbolDTO takerCueSheetAudioSymbolDTO = new TakerCueSheetAudioSymbolDTO();

        List<TakerCueSheetSymbolListDTO> videoSymbolList = new ArrayList<>();
        List<TakerCueSheetSymbolListDTO> audioSymbolList = new ArrayList<>();
        for (CueSheetItemSymbol cueSheetItemSymbol : cueSheetItemSymbolList) {
            Symbol symbol = cueSheetItemSymbol.getSymbol();

            if (ObjectUtils.isEmpty(symbol)) {
                continue;
            }

            String typeCode = symbol.getTypCd();

            if ("audio_icons".equals(typeCode)) {

                String symbolId = symbol.getSymbolId();
                TakerCueSheetSymbolListDTO takerCueSheetSymbolListDTO = TakerCueSheetSymbolListDTO.builder()
                        .symbolId(symbolId).build();

                audioSymbolList.add(takerCueSheetSymbolListDTO);

            } else if ("video_icons".equals(typeCode)) {

                String symbolId = symbol.getSymbolId();
                TakerCueSheetSymbolListDTO takerCueSheetSymbolListDTO = TakerCueSheetSymbolListDTO.builder()
                        .symbolId(symbolId).build();

                videoSymbolList.add(takerCueSheetSymbolListDTO);
            }
        }

        //비디오 심볼정보 노드리스트에 추가
        takerCueSheetVideoSymbolDTO.setTakerCueSheetSymbolListDTOList(videoSymbolList);
        //오디오 심볼정보 노드리스트에 추가
        takerCueSheetAudioSymbolDTO.setTakerCueSheetSymbolListDTOList(audioSymbolList);

        //cmDivCd, cmDivCd 값 구하기 [채널값으로 심볼에 들어가는 NS-1, NS-2, NS-3 값 구하기]
        String returnSymbolId = "";
        String returnSymbolNm = "";

        for (CueSheetItemSymbol cueSheetItemSymbol : cueSheetItemSymbolList) {
            Symbol symbol = cueSheetItemSymbol.getSymbol(); //큐시트아이템에 포함된 심볼  get

            if (ObjectUtils.isEmpty(symbol) == false) {  //심볼이 있을경우
                String symbolId = symbol.getSymbolId(); //심볼아이디
                String symbolNm = symbol.getSymbolNm(); //심볼 명

                switch (symbolId) { // VNS1, VNS2, VNS3 채널로 표기된 심볼이 들어가 있을경우 값 셋팅
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

        TakerCueSheetDTO takerCueSheetDTO = new TakerCueSheetDTO(); //데이터 DTO에 담을 DTO

        if (ObjectUtils.isEmpty(article)) { //기사가 포함이 안된 큐시트 아이템 일시

            //큐시트 아이템 비디오 정보 get
            List<CueSheetMedia> cueSheetMediaList = cueSheetItem.getCueSheetMedia();
            //큐시트 아이템 비디오 정보를 큐시트 테이커 비디오 DTO로 set
            TakerCueSheetVideoDTO takerCueSheetVideoDTOList = getVideoDTOList(cueSheetMediaList);

            //테이커큐시트 정보 큐시트 엔티티 정보로 빌드
            takerCueSheetDTO = buildTakerCue(cueSheet, cueSheetItem, rd_seq, returnSymbolId
                    , returnSymbolNm, takerCueSheetVideoDTOList, takerCueSheetVideoSymbolDTO, takerCueSheetAudioSymbolDTO);

            takerCueRefreshDataDTO.setTakerCueSheetDTO(takerCueSheetDTO);


        } else { //기사가 포함된 큐시트 아이템 일시

            //기사에서 비디오 정보 get
            List<ArticleMedia> cueSheetMediaArticleList = article.getArticleMedia();
            //기사 비디오 정보를 큐시트 테이커 비디오 DTO로 set
            TakerCueSheetVideoDTO takerArticleVideoDTOList = getArticleVideoDTOList(cueSheetMediaArticleList);


            //테이커큐시트 정보 큐시트 엔티티 정보로 빌드
            takerCueSheetDTO = buildTakerCueArticle(cueSheet, cueSheetItem, rd_seq, returnSymbolId
                    , returnSymbolNm, takerArticleVideoDTOList, article, takerCueSheetVideoSymbolDTO, takerCueSheetAudioSymbolDTO);

            takerCueRefreshDataDTO.setTakerCueSheetDTO(takerCueSheetDTO);

        }

        return takerCueRefreshDataDTO;

    }

    //테이커 큐시트 스페어 아이템 Refresh
    public TakerSpareCueRefreshDataDTO takerSpareCueItemRefresh(Long rd_id, Integer rd_seq) {

        TakerSpareCueRefreshDataDTO takerSpareCueRefreshDataDTO = new TakerSpareCueRefreshDataDTO();//리턴DTO

        //조회조건으로 들어온 큐시트 아이템 아이디로 큐시트 엔티티 조회
        //CueSheet cueSheet = findCueSheet(rd_id);

        //조회조건으로 들어온 큐시트 아이템 아이디로 큐시트 아이템 엔티티 조회
        CueSheetItem cueSheetItem = cueSheetItemService.cueItemFindOrFail(rd_id);

        //조회된 큐시트 아이템에서 큐시트 정보 get
        CueSheet cueSheet = cueSheetItem.getCueSheet();

        Article article = cueSheetItem.getArticle(); //큐시트 아이템에 기사정보get

        Long cueItemId = cueSheetItem.getCueItemId();
        //큐시트 아이템 방송아이콘 List 조회
        List<CueSheetItemSymbol> cueSheetItemSymbolList = cueSheetItemSymbolRepository.findSymbol(cueItemId);

        //값을 셋팅하여 xml노드를 만들어줄 비디오 테이커 노드 리스트
        TakerCueSheetVideoSymbolDTO takerCueSheetVideoSymbolDTO = new TakerCueSheetVideoSymbolDTO();
        //값을 셋팅하여 xml노드를 만들어줄 오디오 테이커 노드 리스트
        TakerCueSheetAudioSymbolDTO takerCueSheetAudioSymbolDTO = new TakerCueSheetAudioSymbolDTO();

        List<TakerCueSheetSymbolListDTO> videoSymbolList = new ArrayList<>();
        List<TakerCueSheetSymbolListDTO> audioSymbolList = new ArrayList<>();
        for (CueSheetItemSymbol cueSheetItemSymbol : cueSheetItemSymbolList) {
            Symbol symbol = cueSheetItemSymbol.getSymbol();

            if (ObjectUtils.isEmpty(symbol)) {
                continue;
            }

            String typeCode = symbol.getTypCd();

            if ("audio_icons".equals(typeCode)) {

                String symbolId = symbol.getSymbolId();
                TakerCueSheetSymbolListDTO takerCueSheetSymbolListDTO = TakerCueSheetSymbolListDTO.builder()
                        .symbolId(symbolId).build();

                audioSymbolList.add(takerCueSheetSymbolListDTO);

            } else if ("video_icons".equals(typeCode)) {

                String symbolId = symbol.getSymbolId();
                TakerCueSheetSymbolListDTO takerCueSheetSymbolListDTO = TakerCueSheetSymbolListDTO.builder()
                        .symbolId(symbolId).build();

                videoSymbolList.add(takerCueSheetSymbolListDTO);
            }
        }

        //비디오 심볼정보 노드리스트에 추가
        takerCueSheetVideoSymbolDTO.setTakerCueSheetSymbolListDTOList(videoSymbolList);
        //오디오 심볼정보 노드리스트에 추가
        takerCueSheetAudioSymbolDTO.setTakerCueSheetSymbolListDTOList(audioSymbolList);

        //cmDivCd, cmDivCd 값 구하기 [채널값으로 심볼에 들어가는 NS-1, NS-2, NS-3 값 구하기]
        String returnSymbolId = "";
        String returnSymbolNm = "";

        for (CueSheetItemSymbol cueSheetItemSymbol : cueSheetItemSymbolList) {
            Symbol symbol = cueSheetItemSymbol.getSymbol(); //큐시트아이템에 포함된 심볼  get

            if (ObjectUtils.isEmpty(symbol) == false) {  //심볼이 있을경우
                String symbolId = symbol.getSymbolId(); //심볼아이디
                String symbolNm = symbol.getSymbolNm(); //심볼 명

                switch (symbolId) { // VNS1, VNS2, VNS3 채널로 표기된 심볼이 들어가 있을경우 값 셋팅
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

        TakerCueSheetDTO takerCueSheetDTO = new TakerCueSheetDTO(); //데이터 DTO에 담을 DTO

        if (ObjectUtils.isEmpty(article)) { //기사가 포함이 안된 큐시트 아이템 일시

            //큐시트 아이템 비디오 정보 get
            List<CueSheetMedia> cueSheetMediaList = cueSheetItem.getCueSheetMedia();
            //큐시트 아이템 비디오 정보를 큐시트 테이커 비디오 DTO로 set
            TakerCueSheetVideoDTO takerCueSheetVideoDTOList = getVideoDTOList(cueSheetMediaList);

            //테이커큐시트 정보 큐시트 엔티티 정보로 빌드
            takerCueSheetDTO = buildTakerCue(cueSheet, cueSheetItem, rd_seq, returnSymbolId
                    , returnSymbolNm, takerCueSheetVideoDTOList, takerCueSheetVideoSymbolDTO, takerCueSheetAudioSymbolDTO);

            takerSpareCueRefreshDataDTO.setTakerCueSheetDTO(takerCueSheetDTO);


        } else { //기사가 포함된 큐시트 아이템 일시

            //기사에서 비디오 정보 get
            List<ArticleMedia> cueSheetMediaArticleList = article.getArticleMedia();
            //기사 비디오 정보를 큐시트 테이커 비디오 DTO로 set
            TakerCueSheetVideoDTO takerArticleVideoDTOList = getArticleVideoDTOList(cueSheetMediaArticleList);


            //테이커큐시트 정보 큐시트 엔티티 정보로 빌드
            takerCueSheetDTO = buildTakerCueArticle(cueSheet, cueSheetItem, rd_seq, returnSymbolId
                    , returnSymbolNm, takerArticleVideoDTOList, article, takerCueSheetVideoSymbolDTO, takerCueSheetAudioSymbolDTO);

            takerSpareCueRefreshDataDTO.setTakerCueSheetDTO(takerCueSheetDTO);

        }

        return takerSpareCueRefreshDataDTO;

    }

    //테이커 큐시트 리플레시 DTO TO XML
    public String takerCueRefresh(TakerCueRefreshDataDTO takerCueSheetDTO) {
        //큐시트목록 xml을 담는 DTO
        TakerCueRefreshXML takerCueSheetXML = new TakerCueRefreshXML();
        //success="true" msg="ok" 담는DTO
        TakerCueSheetResultDTO takerCueSheetResultDTO = new TakerCueSheetResultDTO();
        //<data totalcount="6" curpage="0" rowcount="0">&&List<cue>
        //TakerCueSheetDataDTO takerCueSheetDataDTO = new TakerCueSheetDataDTO();

        //result 데이터 set
        takerCueSheetResultDTO.setMsg("ok");
        takerCueSheetResultDTO.setSuccess("true");

        //ContentsDTO 데이터 set
        //takerCueSheetDataDTO.setTakerCueSheetDTO(takerCueSheetDTOList);

        takerCueSheetDTO.setTotalcount(1L);
        takerCueSheetDTO.setCurpage(0);
        takerCueSheetDTO.setRowcount(0);

        //xml 변환 DTO에 result, ContentsDTO set
        takerCueSheetXML.setData(takerCueSheetDTO);
        takerCueSheetXML.setResult(takerCueSheetResultDTO);

        //DTO TO XML 파싱
        String xml = JAXBXmlHelper.marshal(takerCueSheetXML, TakerCueRefreshXML.class);

        return xml;
    }

    //테이커 큐시트 리플레시 DTO TO XML
    public String takerSpareCueRefresh(TakerSpareCueRefreshDataDTO takerSpareCueSheetDTO) {
        //큐시트목록 xml을 담는 DTO
        TakerSpareCueRefreshXML takerSpareCueRefreshXML = new TakerSpareCueRefreshXML();
        //success="true" msg="ok" 담는DTO
        TakerCueSheetResultDTO takerCueSheetResultDTO = new TakerCueSheetResultDTO();
        //<data totalcount="6" curpage="0" rowcount="0">&&List<cue>
        //TakerCueSheetDataDTO takerCueSheetDataDTO = new TakerCueSheetDataDTO();

        //result 데이터 set
        takerCueSheetResultDTO.setMsg("ok");
        takerCueSheetResultDTO.setSuccess("true");

        //ContentsDTO 데이터 set
        //takerCueSheetDataDTO.setTakerCueSheetDTO(takerCueSheetDTOList);

        takerSpareCueSheetDTO.setTotalcount(1L);
        takerSpareCueSheetDTO.setCurpage(0);
        takerSpareCueSheetDTO.setRowcount(0);

        //xml 변환 DTO에 result, ContentsDTO set
        takerSpareCueRefreshXML.setData(takerSpareCueSheetDTO);
        takerSpareCueRefreshXML.setResult(takerCueSheetResultDTO);

        //DTO TO XML 파싱
        String xml = JAXBXmlHelper.marshal(takerSpareCueRefreshXML, TakerSpareCueRefreshXML.class);

        return xml;
    }

    //전송상태 업데이트
    public void stateChange(Integer contentId, String mediaTypCd, Integer trnasfVal){

        //콘텐츠아이디 + 비디오아이디 로 기사 미디어 검색.
        List<ArticleMedia> articleMediaList = articleMediaRepository.findArticleMediaListByContentId(contentId);

        //검색된 기사 미디어 값 업데이트
        for (ArticleMedia articleMedia : articleMediaList){

            ArticleMediaDTO articleMediaDTO = articleMediaMapper.toDto(articleMedia);
            articleMediaDTO.setMediaTypCd(mediaTypCd); //변경코드 업데이트
            articleMediaDTO.setMediaMtchDtm(new Date());
            articleMediaDTO.setTrnasfVal(trnasfVal);
            //articleMediaDTO.setTrnsfFileNm(trnsfFileNm); //전송파일명 업데이트

            articleMediaMapper.updateFromDto(articleMediaDTO, articleMedia);

            articleMediaRepository.save(articleMedia);

        }
    }

    //큐시트 방송상태 업데이트[taker]
    public ParentProgramDTO cueStCdUpdate(Long cueId, String cueStCd){

        CueSheet cueSheet = findCueSheet(cueId, "N");

        CueSheetDTO cueSheetDTO = cueSheetMapper.toDto(cueSheet);
        if ("on_air".equals(cueStCd)) {
            cueSheetDTO.setCueStCd(cueStCd);

            cueSheetMapper.updateFromDto(cueSheetDTO, cueSheet);
            cueSheetRepository.save(cueSheet);
        }


        String rdId = String.valueOf(cueId);

        ParentProgramDTO parentProgramDTO = cueToTaker(cueSheetDTO);


       /* TakerCueSheetDataDTO takerCueSheetDataDTO = cuefindAll(rdId, null, null, null, null, "N", null
                , null, null, null, null, null, null);


        return takerCueSheetDataDTO;*/

        return parentProgramDTO;
    }
}