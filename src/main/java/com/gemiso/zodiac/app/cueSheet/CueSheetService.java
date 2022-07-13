package com.gemiso.zodiac.app.cueSheet;

import com.gemiso.zodiac.app.articleTag.ArticleTag;
import com.gemiso.zodiac.app.articleTag.ArticleTagRepository;
import com.gemiso.zodiac.app.anchorCap.AnchorCap;
import com.gemiso.zodiac.app.anchorCap.AnchorCapRepository;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.ArticleRepository;
import com.gemiso.zodiac.app.article.ArticleService;
import com.gemiso.zodiac.app.article.dto.ArticleCueItemDTO;
import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import com.gemiso.zodiac.app.article.mapper.ArticleSimpleMapper;
import com.gemiso.zodiac.app.articleCap.ArticleCap;
import com.gemiso.zodiac.app.articleCap.ArticleCapRepository;
import com.gemiso.zodiac.app.articleMedia.ArticleMedia;
import com.gemiso.zodiac.app.articleMedia.ArticleMediaRepository;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaDTO;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaSimpleDTO;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaMapper;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaSimpleMapper;
import com.gemiso.zodiac.app.baseProgram.BaseProgram;
import com.gemiso.zodiac.app.baseProgram.dto.BaseProgramSimpleDTO;
import com.gemiso.zodiac.app.capTemplate.CapTemplate;
import com.gemiso.zodiac.app.cueCapTmplt.CueCapTmplt;
import com.gemiso.zodiac.app.cueCapTmplt.CueCapTmpltRepository;
import com.gemiso.zodiac.app.cueSheet.dto.*;
import com.gemiso.zodiac.app.cueSheet.mapper.CueSheetCreateMapper;
import com.gemiso.zodiac.app.cueSheet.mapper.CueSheetMapper;
import com.gemiso.zodiac.app.cueSheet.mapper.CueSheetOrderLockMapper;
import com.gemiso.zodiac.app.cueSheet.mapper.CueSheetUpdateMapper;
import com.gemiso.zodiac.app.cueSheetHist.CueSheetHist;
import com.gemiso.zodiac.app.cueSheetHist.CueSheetHistRepository;
import com.gemiso.zodiac.app.cueSheetHist.dto.CueSheetHistCreateDTO;
import com.gemiso.zodiac.app.cueSheetHist.mapper.CueSheetHistCreateMapper;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItemRepository;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemCreateDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSimpleDTO;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemCreateMapper;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemMapper;
import com.gemiso.zodiac.app.cueSheetItemCap.CueSheetItemCap;
import com.gemiso.zodiac.app.cueSheetItemCap.CueSheetItemCapRepotitory;
import com.gemiso.zodiac.app.cueSheetItemCap.dto.CueSheetItemCapCreateDTO;
import com.gemiso.zodiac.app.cueSheetItemCap.dto.CueSheetItemCapDTO;
import com.gemiso.zodiac.app.cueSheetItemCap.mapper.CueSheetItemCapCreateMapper;
import com.gemiso.zodiac.app.cueSheetItemCap.mapper.CueSheetItemCapMapper;
import com.gemiso.zodiac.app.cueSheetItemSymbol.CueSheetItemSymbol;
import com.gemiso.zodiac.app.cueSheetItemSymbol.CueSheetItemSymbolRepository;
import com.gemiso.zodiac.app.cueSheetItemSymbol.dto.CueSheetItemSymbolCreateDTO;
import com.gemiso.zodiac.app.cueSheetItemSymbol.dto.CueSheetItemSymbolDTO;
import com.gemiso.zodiac.app.cueSheetItemSymbol.mapper.CueSheetItemSymbolCreateMapper;
import com.gemiso.zodiac.app.cueSheetItemSymbol.mapper.CueSheetItemSymbolMapper;
import com.gemiso.zodiac.app.cueSheetMedia.CueSheetMedia;
import com.gemiso.zodiac.app.cueSheetMedia.CueSheetMediaRepository;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaCreateDTO;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaDTO;
import com.gemiso.zodiac.app.cueSheetMedia.mapper.CueSheetMediaCreateMapper;
import com.gemiso.zodiac.app.cueSheetMedia.mapper.CueSheetMediaMapper;
import com.gemiso.zodiac.app.dailyProgram.DailyProgramService;
import com.gemiso.zodiac.app.dailyProgram.dto.DailyProgramDTO;
import com.gemiso.zodiac.app.elasticsearch.ElasticSearchArticleService;
import com.gemiso.zodiac.app.facilityManage.FacilityManage;
import com.gemiso.zodiac.app.facilityManage.FacilityManageService;
import com.gemiso.zodiac.app.lbox.LboxService;
import com.gemiso.zodiac.app.program.Program;
import com.gemiso.zodiac.app.program.ProgramService;
import com.gemiso.zodiac.app.program.dto.ProgramDTO;
import com.gemiso.zodiac.app.program.dto.ProgramSimpleDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import com.gemiso.zodiac.app.tag.Tag;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.app.user.UserService;
import com.gemiso.zodiac.core.enumeration.ActionEnum;
import com.gemiso.zodiac.core.helper.DateChangeHelper;
import com.gemiso.zodiac.core.helper.JAXBXmlHelper;
import com.gemiso.zodiac.core.topic.CueSheetTopicService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CueSheetService {

    @Value("${files.url-key}")
    private String fileUrl;

    private final CueSheetRepository cueSheetRepository;
    private final CueSheetItemRepository cueSheetItemRepository;
    private final CueSheetHistRepository cueSheetHistRepository;
    private final CueSheetItemSymbolRepository cueSheetItemSymbolRepository;
    private final CueSheetMediaRepository cueSheetMediaRepository;
    private final ArticleRepository articleRepository;
    private final ArticleCapRepository articleCapRepository;
    private final AnchorCapRepository anchorCapRepository;
    private final ArticleMediaRepository articleMediaRepository;
    private final CueSheetItemCapRepotitory cueSheetItemCapRepotitory;
    private final CueCapTmpltRepository cueCapTmpltRepository;
    private final ArticleTagRepository articleTagRepository;

    private final CueSheetMapper cueSheetMapper;
    private final CueSheetCreateMapper cueSheetCreateMapper;
    private final CueSheetUpdateMapper cueSheetUpdateMapper;
    private final CueSheetOrderLockMapper cueSheetOrderLockMapper;
    private final CueSheetItemCreateMapper cueSheetItemCreateMapper;
    private final CueSheetItemMapper cueSheetItemMapper;
    private final CueSheetHistCreateMapper cueSheetHistCreateMapper;
    private final CueSheetItemSymbolMapper cueSheetItemSymbolMapper;
    private final CueSheetItemSymbolCreateMapper cueSheetItemSymbolCreateMapper;
    private final ArticleSimpleMapper articleSimpleMapper;
    private final CueSheetItemCapCreateMapper cueSheetItemCapCreateMapper;
    private final ArticleMediaSimpleMapper articleMediaSimpleMapper;
    private final CueSheetMediaCreateMapper cueSheetMediaCreateMapper;
    private final ArticleMediaMapper articleMediaMapper;
    private final CueSheetMediaMapper cueSheetMediaMapper;
    private final CueSheetItemCapMapper cueSheetItemCapMapper;

    private final ProgramService programService;
    //private final UserAuthService userAuthService;
    private final ArticleService articleService;
    private final UserService userService;
    private final ElasticSearchArticleService elasticSearchArticleService;

    private final DailyProgramService dailyProgramService;

    private final DateChangeHelper dateChangeHelper;

    //private final MarshallingJsonHelper marshallingJsonHelper;

    private final CueSheetTopicService cueSheetTopicService;
    //private final TopicSendService topicSendService;
    private final FacilityManageService facilityManageService;
    private final LboxService lboxService;


    //큐시트 목록조회 + 유니온 일일편성 [큐시트 인터페이스+큐시트 아이템추가 목록]
    public CueSheetFindAllDTO findAll(Date sdate, Date edate, String brdcPgmId, String brdcPgmNm, Long deptCd, String searchWord) {

        //BooleanBuilder booleanBuilder = getSearch( sdate,  edate,  brdcPgmId,  brdcPgmNm, deptCd, searchWord);

        //order by 정령조건 생성[ ASC 방송일시, DESC 방송시작시간]
       /* List<Sort.Order> orders = new ArrayList<>();
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "brdcDt");
        orders.add(order1);
        Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "brdcStartTime");
        orders.add(order2);*/

        List<CueSheet> cueSheets = cueSheetRepository.findByCueSheetList(sdate, edate, brdcPgmId, brdcPgmNm, deptCd, searchWord);

        List<CueSheetDTO> cueSheetDTOList = cueSheetMapper.toDtoList(cueSheets);

        //List<CueSheetDTO> newCueSheetDTOList = checkDelItem(cueSheetDTOList); //삭제된 기사아이템 제거.

        CueSheetFindAllDTO cueSheetFindAllDTO = unionPgmInterface(cueSheetDTOList, sdate, edate, brdcPgmId,
                brdcPgmNm, searchWord); //큐시트 유니온 방송프로그램.

        //큐시트에 포함된 기사수를 계산한다[ 큐시트 목록조회시 화면에 필요 ]
        cueSheetFindAllDTO = getArticleCount(cueSheetFindAllDTO);

        //큐시트에 포함된 기사수를 계산한다[ 큐시트 목록조회시 화면에 필요 ]
        //cueSheetFindAllDTO = createArticleCount(cueSheetFindAllDTO);

        return cueSheetFindAllDTO;


    }

    //큐시트 목록조회 + 유니온 일일편성 [큐시트 인터페이스+큐시트 아이템추가 목록]
    public List<CueSheetDTO> takerFindAll(Date sdate, Date edate, String brdcPgmId, String brdcPgmNm, Long deptCd, String searchWord) {

        BooleanBuilder booleanBuilder = getSearch(sdate, edate, brdcPgmId, brdcPgmNm, deptCd, searchWord);

        //order by 정령조건 생성[ ASC 방송일시, DESC 방송시작시간]
        List<Sort.Order> orders = new ArrayList<>();
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "brdcDt");
        orders.add(order1);
        Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "brdcStartTime");
        orders.add(order2);

        List<CueSheet> cueSheets = (List<CueSheet>) cueSheetRepository.findAll(booleanBuilder, Sort.by(orders));

        List<CueSheetDTO> cueSheetDTOList = cueSheetMapper.toDtoList(cueSheets);

        //List<CueSheetDTO> newCueSheetDTOList = checkDelItem(cueSheetDTOList); //삭제된 기사아이템 제거.

        /*CueSheetFindAllDTO cueSheetFindAllDTO = unionPgmInterface(cueSheetDTOList, sdate, edate, brdcPgmId,
                brdcPgmNm, searchWord); //큐시트 유니온 방송프로그램.*/

        //큐시트에 포함된 기사수를 계산한다[ 큐시트 목록조회시 화면에 필요 ]
        /*cueSheetFindAllDTO = getArticleCount(cueSheetFindAllDTO);*/

        //큐시트에 포함된 기사수를 계산한다[ 큐시트 목록조회시 화면에 필요 ]
        cueSheetDTOList = getTakerArticleCount(cueSheetDTOList);

        return cueSheetDTOList;


    }

    //큐시트에 큐시트아이템 정보 추가
    public List<CueSheetDTO> addCueItem(List<CueSheetDTO> cueSheetDTOList) {

        List<CueSheetDTO> returnCueSheet = new ArrayList<>();

        for (CueSheetDTO cueSheetDTO : cueSheetDTOList) {

            Long cueId = cueSheetDTO.getCueId();

            List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findByCueItemList(cueId);
            List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemMapper.toDtoList(cueSheetItemList);
            cueSheetDTO.setCueSheetItem(cueSheetItemDTOList);

            returnCueSheet.add(cueSheetDTO);
        }

        return returnCueSheet;
    }


    //큐시트 목록조회 + 유니온 일일편성 [큐시트 인터페이스+큐시트 아이템추가 목록]
    public CueSheetFindAllDTO psTakerFindAll(Date sdate, Date edate, String brdcPgmId, String brdcPgmNm, String searchWord) {

        BooleanBuilder booleanBuilder = getSearch(sdate, edate, brdcPgmId, brdcPgmNm, null, searchWord);

        //order by 정령조건 생성[ ASC 방송일시, DESC 방송시작시간]
        List<Sort.Order> orders = new ArrayList<>();
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "brdcDt");
        orders.add(order1);
        Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "brdcStartTime");
        orders.add(order2);

        List<CueSheet> cueSheets = (List<CueSheet>) cueSheetRepository.findAll(booleanBuilder, Sort.by(orders));

        List<CueSheetDTO> cueSheetDTOList = cueSheetMapper.toDtoList(cueSheets);

        //List<CueSheetDTO> newCueSheetDTOList = checkDelItem(cueSheetDTOList); //삭제된 기사아이템 제거.
        List<CueSheetDTO> newCueSheetDTOList = addCueItem(cueSheetDTOList); //큐시트에 큐시트아이템정보 추가

        CueSheetFindAllDTO cueSheetFindAllDTO = unionPgmInterface(newCueSheetDTOList, sdate, edate, brdcPgmId,
                brdcPgmNm, searchWord); //큐시트 유니온 방송프로그램.

        //큐시트에 포함된 기사수를 계산한다[ 큐시트 목록조회시 화면에 필요 ]
        cueSheetFindAllDTO = getArticleCount(cueSheetFindAllDTO);

        //큐시트에 포함된 기사수를 계산한다[ 큐시트 목록조회시 화면에 필요 ]
        //cueSheetFindAllDTO = createArticleCount(cueSheetFindAllDTO);

        return cueSheetFindAllDTO;


    }

    //큐시트 아이템에 포함된 기사수 생성[ 테이커 , 프롬프터]
    public List<CueSheetDTO> getTakerArticleCount(List<CueSheetDTO> cueSheetDTOList) {

        List<CueSheetDTO> returncueSheetDTOList = new ArrayList<>();

        for (CueSheetDTO cueSheetDTO : cueSheetDTOList) {

            Long cueId = cueSheetDTO.getCueId();

            int count = articleRepository.findArticleCount(cueId);
            cueSheetDTO.setArticleCount(count);

            returncueSheetDTOList.add(cueSheetDTO);
        }

        return returncueSheetDTOList;
    }

    //큐시트 아이템에 포함된 기사수 생성
    public CueSheetFindAllDTO getArticleCount(CueSheetFindAllDTO cueSheetFindAllDTO) {

        List<CueSheetDTO> cueSheetDTOs = cueSheetFindAllDTO.getCueSheetDTO();
        List<CueSheetDTO> cueSheetDTOList = new ArrayList<>();

        for (CueSheetDTO cueSheetDTO : cueSheetDTOs) {

            Long cueId = cueSheetDTO.getCueId();

            int count = articleRepository.findArticleCount(cueId);
            cueSheetDTO.setArticleCount(count);

            cueSheetDTOList.add(cueSheetDTO);
        }

        cueSheetFindAllDTO.setCueSheetDTO(cueSheetDTOList);

        return cueSheetFindAllDTO;
    }

    //큐시트에 포함된 기사수를 계산한다[ 큐시트 목록조회시 화면에 필요 ]
    public CueSheetFindAllDTO createArticleCount(CueSheetFindAllDTO cueSheetFindAllDTO) {

        List<CueSheetDTO> returnCueSheetDTOList = new ArrayList<>();//새로등록할 큐시트 리스트
        List<CueSheetDTO> cueSheetDTOList = cueSheetFindAllDTO.getCueSheetDTO();//조회된 큐시트 리스트get

        for (CueSheetDTO cueSheetDTO : cueSheetDTOList) { //큐트리스트 기사수 체크

            List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetDTO.getCueSheetItem();//큐시트에 포함된 큐시트 아이템 리스트get
            Integer articleCount = 0;//기사수를 count해서 set해줄 객체 생성

            for (CueSheetItemDTO cueSheetItemDTO : cueSheetItemDTOList) {//큐시트아이템 리스트 체크
                ArticleCueItemDTO articleDTO = cueSheetItemDTO.getArticle();//큐시트아이템에 포함된 기사가 get

                if (ObjectUtils.isEmpty(articleDTO)) {// 큐시트 아이템에 기사가 포함되어 있지 않으면 contiue
                    continue;
                }
                articleCount++; //큐시트에 기사가 포함되어 있으면 count ++
            }
            cueSheetDTO.setArticleCount(articleCount); //체크된 count수 set
            returnCueSheetDTOList.add(cueSheetDTO); //기사수가 포함된 큐시트 리턴할 큐시트 리스트에 add
        }

        cueSheetFindAllDTO.setCueSheetDTO(returnCueSheetDTOList);//큐시트 목록 기사수 포함된 큐시트리스르로 다시 set

        return cueSheetFindAllDTO;
    }

    //삭제된 기사아이템 제거.[큐시트 인터페이스+큐시트 아이템추가 목록]
    public List<CueSheetDTO> checkDelItem(List<CueSheetDTO> cueSheetDTOList) {

        List<CueSheetDTO> newCueSheetDTOList = new ArrayList<>();

        //삭제된 큐시트 아이템 제거.
        for (CueSheetDTO cueSheetDTO : cueSheetDTOList) {
            List<CueSheetItemDTO> orgCueSheetItemDTOList = cueSheetDTO.getCueSheetItem();
            List<CueSheetItemDTO> newCueSheetItemDTOList = new ArrayList<>();
            for (Iterator<CueSheetItemDTO> itr = orgCueSheetItemDTOList.iterator(); itr.hasNext(); ) {

                CueSheetItemDTO cueSheetItemDTO = itr.next();

                String delYn = cueSheetItemDTO.getDelYn();//큐시트 아이템의 삭제여부 값
                ArticleCueItemDTO articleCueItemDTO = cueSheetItemDTO.getArticle();//큐시트 아이템의 기사의 삭제여부 값

                if (ObjectUtils.isEmpty(articleCueItemDTO) == false) { //큐시트 아이템에 기사가 포함된 경우.
                    String articleDelYn = articleCueItemDTO.getDelYn();//큐시트 아이템에 포함된 기사의 삭제 여부값
                    if ("Y".equals(articleDelYn)) { //기사 삭제 값이 Y인경우 조회된 큐시트아이템 삭제
                        continue;
                    }
                }
                if ("Y".equals(delYn)) { //조회된 큐시트 아이템 삭제여부값이 Y인 경우/.
                    continue;
                }

                newCueSheetItemDTOList.add(cueSheetItemDTO);

            }

            cueSheetDTO.setCueSheetItem(newCueSheetItemDTOList);
            newCueSheetDTOList.add(cueSheetDTO);
        }

        return newCueSheetDTOList;
    }

    // 큐시트목록, 일일편성목록 유니온.[큐시트목록조회 인터페이스 +큐시트 아이템추가 목록]
    public CueSheetFindAllDTO unionPgmInterface(List<CueSheetDTO> cueSheetDTOList, Date sdate, Date edate, String brdcPgmId, String brdcPgmNm, String searchWord) {

        //일일편성 목록조회[큐시트에 들어온 조회조건과 같이 조회한다.]
        List<DailyProgramDTO> dailyProgramList = dailyProgramService.findAll(sdate, edate, brdcPgmId, brdcPgmNm, null, null, null, searchWord);

        //조회된 큐시트 와 조회된 일일편성 유니온.
        for (CueSheetDTO cueSheetDTO : cueSheetDTOList) {

            //프로그램 get null포인트 에러 발생 방지 optional get
            Optional<ProgramDTO> programDTO = Optional.ofNullable(cueSheetDTO.getProgram());
            String cueBrdcPgmId = "";

            if (programDTO.isPresent()) { //프로그램이 있을경우 프로그램 아이디 get
                ProgramDTO program = programDTO.get();
                cueBrdcPgmId = program.getBrdcPgmId();
            }
            String cueBrdcDt = cueSheetDTO.getBrdcDt(); // 큐시트 방송일자 get
            String cueBrdcStartTime = cueSheetDTO.getBrdcStartTime(); //큐시트 방송시간 get

            //ConcurrentModificationException에러가 나서 이터레이터로 수정.
            Iterator<DailyProgramDTO> iter = dailyProgramList.listIterator();
            while (iter.hasNext()) {
                DailyProgramDTO dailyProgramDTO = iter.next();

                ProgramDTO dailyProgram = dailyProgramDTO.getProgram(); //일일편성 프로그램get
                String dailyBrdcPgmId = "";

                if (ObjectUtils.isEmpty(dailyProgramDTO) == false) { //일일편성에 프로그램이 있을경우 프로그램 아이디 get
                    dailyBrdcPgmId = dailyProgram.getBrdcPgmId();
                }
                String dailyBrdcDt = dailyProgramDTO.getBrdcDt(); //일일편성 방송일자 get
                String dailyBrdcStartTime = dailyProgramDTO.getBrdcStartTime(); //일일편성 방송시작시간 get

                if (cueBrdcPgmId.trim().isEmpty() == false && dailyBrdcPgmId.trim().isEmpty() == false
                        && dailyBrdcPgmId.equals(cueBrdcPgmId)) { //큐시트&일일편성 방송프로그램 아이디가 있고 같으면

                    if (dailyBrdcDt.equals(cueBrdcDt) && dailyBrdcStartTime.equals(cueBrdcStartTime)) { //방송일자, 방송시작시간이 같으면 삭제.
                        iter.remove(); //생성된 큐시트와 일일편성이 같으면 일일편성 리스트에서 삭제
                    }

                }
            }

        }
        //조회된 큐시트 + 방송프로그램
        CueSheetFindAllDTO cueSheetFindAllDTO = new CueSheetFindAllDTO();
        cueSheetFindAllDTO.setCueSheetDTO(cueSheetDTOList);
        cueSheetFindAllDTO.setDailyProgramDTO(dailyProgramList);

        return cueSheetFindAllDTO;
    }

    //큐시트 목록조회 - 큐시트 리스트 + 일일편성 리스트 유니온
    public CueSheetFindAllDTO unionPgm(List<CueSheetDTO> cueSheetDTOList, Date sdate, Date edate, String brdcPgmId, String brdcPgmNm, String searchWord) {

        //일일편성 목록조회[큐시트에 들어온 조회조건과 같이 조회한다.]
        List<DailyProgramDTO> dailyProgramList = dailyProgramService.findAll(sdate, edate, brdcPgmId, brdcPgmNm, null, null, null, searchWord);

        //조회된 큐시트 와 조회된 일일편성 유니온.
        for (CueSheetDTO cueSheetDTO : cueSheetDTOList) {

            //프로그램 get null포인트 에러 발생 방지 optional get
            Optional<ProgramDTO> programDTO = Optional.ofNullable(cueSheetDTO.getProgram());
            String cueBrdcPgmId = "";

            if (programDTO.isPresent()) { //프로그램이 있을경우 프로그램 아이디 get
                ProgramDTO program = programDTO.get();
                cueBrdcPgmId = program.getBrdcPgmId();
            }
            String cueBrdcDt = cueSheetDTO.getBrdcDt(); // 큐시트 방송일자 get
            String cueBrdcStartTime = cueSheetDTO.getBrdcStartTime(); //큐시트 방송시간 get

            //ConcurrentModificationException에러가 나서 이터레이터로 수정.
            Iterator<DailyProgramDTO> iter = dailyProgramList.listIterator();
            while (iter.hasNext()) {
                DailyProgramDTO dailyProgramDTO = iter.next();

                ProgramDTO dailyProgram = dailyProgramDTO.getProgram(); //일일편성 프로그램get
                String dailyBrdcPgmId = "";

                if (ObjectUtils.isEmpty(dailyProgramDTO) == false) { //일일편성에 프로그램이 있을경우 프로그램 아이디 get
                    dailyBrdcPgmId = dailyProgram.getBrdcPgmId();
                }
                String dailyBrdcDt = dailyProgramDTO.getBrdcDt(); //일일편성 방송일자 get
                String dailyBrdcStartTime = dailyProgramDTO.getBrdcStartTime(); //일일편성 방송시작시간 get

                if (cueBrdcPgmId.trim().isEmpty() == false && dailyBrdcPgmId.trim().isEmpty() == false
                        && dailyBrdcPgmId.equals(cueBrdcPgmId)) { //큐시트&일일편성 방송프로그램 아이디가 있고 같으면

                    if (dailyBrdcDt.equals(cueBrdcDt) && dailyBrdcStartTime.equals(cueBrdcStartTime)) { //방송일자, 방송시작시간이 같으면 삭제.
                        iter.remove(); //생성된 큐시트와 일일편성이 같으면 일일편성 리스트에서 삭제
                    }
                }
            }
        }
        //조회된 큐시트 + 방송프로그램
        CueSheetFindAllDTO cueSheetFindAllDTO = new CueSheetFindAllDTO();
        cueSheetFindAllDTO.setCueSheetDTO(cueSheetDTOList);
        cueSheetFindAllDTO.setDailyProgramDTO(dailyProgramList);

        return cueSheetFindAllDTO;

    }

    //큐시트 상세조회
    public CueSheetDTO find(Long cueId) {

        CueSheet cueSheet = cueSheetFindOrFail(cueId);

        CueSheetDTO cueSheetDTO = cueSheetMapper.toDto(cueSheet);

        //큐시트 아이템
        //List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findByCueItemList(cueId);
        //List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemMapper.toDtoList(cueSheetItemList);
        //cueSheetDTO.setCueSheetItem(cueSheetItemDTOList);

        //기사 미디어 있을경우 set
        //List<CueSheetItem> cueSheetItemList = cueSheetItemFindAll(cueId);
        //List<CueSheetItem> setCueSheetItemList = new ArrayList<>();

        //List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemMapper.toDtoList(cueSheetItemList);

        //cueSheetItemDTOList = setSymbol(cueSheetItemDTOList);


        return cueSheetDTO;

    }

    //큐시트 상세조회
    public CueSheetDTO psFind(Long cueId) {

        CueSheet cueSheet = cueSheetFindOrFail(cueId);

        CueSheetDTO cueSheetDTO = cueSheetMapper.toDto(cueSheet);

        List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findByCueItemList(cueId);
        List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemMapper.toDtoList(cueSheetItemList);

        List<CueSheetItemDTO> restoreCueSheetItemDTOList = new ArrayList<>();


        for (CueSheetItemDTO cueSheetItemDTO : cueSheetItemDTOList) {
            ArticleCueItemDTO article = cueSheetItemDTO.getArticle();
            if (ObjectUtils.isEmpty(article) == false) {

                Long artclId = article.getArtclId();
                List<ArticleMedia> articleMedia = articleMediaRepository.findArticleMediaList(artclId);
                List<ArticleMediaSimpleDTO> articleMediaSimpleDTOS = articleMediaSimpleMapper.toDtoList(articleMedia);
                article.setArticleMedia(articleMediaSimpleDTOS);
            }
            cueSheetItemDTO.setArticle(article);
            restoreCueSheetItemDTOList.add(cueSheetItemDTO);
        }

        cueSheetDTO.setCueSheetItem(restoreCueSheetItemDTOList);
        //기사 미디어 있을경우 set
        //List<CueSheetItem> cueSheetItemList = cueSheetItemFindAll(cueId);
        //List<CueSheetItem> setCueSheetItemList = new ArrayList<>();

        //List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemMapper.toDtoList(cueSheetItemList);

        //cueSheetItemDTOList = setSymbol(cueSheetItemDTOList);

        //cueSheetDTO.setCueSheetItem(cueSheetItemDTOList);

        return cueSheetDTO;

    }

    //큐시트아이템 목록조회(검색조건 :  큐시트 아이디)
   /* public List<CueSheetItem> cueSheetItemFindAll(Long cueId) {

        BooleanBuilder booleanBuilder = getSearchCueItem(cueId);

        List<CueSheetItem> cueSheetItemList = (List<CueSheetItem>) cueSheetItemRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "cueItemOrd"));

        return cueSheetItemList;
    }*/

    //방송아이콘 url 추가
    public List<CueSheetItemDTO> setSymbol(List<CueSheetItemDTO> cueSheetItemDTOList) {

        for (CueSheetItemDTO cueSheetItemDTO : cueSheetItemDTOList) { //조회된 아이템에 List

            Long cueItemId = cueSheetItemDTO.getCueItemId(); //아이템 아이디 get

            //아이템 아이디로 방송아이콘 맵핑테이블 조회
            List<CueSheetItemSymbol> cueSheetItemSymbolList = cueSheetItemSymbolRepository.findSymbol(cueItemId);

            if (ObjectUtils.isEmpty(cueSheetItemSymbolList) == false) { //조회된 방송아콘 맵핑테이블 List가 있으면 url추가후 큐시트 아이템DTO에 추가

                List<CueSheetItemSymbolDTO> cueSheetItemSymbolDTO = cueSheetItemSymbolMapper.toDtoList(cueSheetItemSymbolList);

                for (CueSheetItemSymbolDTO getCueSheetItemSymbol : cueSheetItemSymbolDTO) {

                    SymbolDTO symbolDTO = getCueSheetItemSymbol.getSymbol();

                    String fileLoc = symbolDTO.getAttachFile().getFileLoc();//파일로그 get
                    String url = fileUrl + fileLoc; //url + 파일로그

                    symbolDTO.setUrl(url);//방송아이콘이 저장된 url set

                    getCueSheetItemSymbol.setSymbol(symbolDTO);//url 추가 articleDTO set
                }


                cueSheetItemDTO.setCueSheetItemSymbol(cueSheetItemSymbolDTO); //아이템에 set방송아이콘List

            }
        }

        return cueSheetItemDTOList;

    }

    //큐시트아이템 목록조회 조건검색빌드
   /* public BooleanBuilder getSearchCueItem(Long cueId) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        //dsl q쿼리 생성
        QCueSheetItem qCueSheetItem = QCueSheetItem.cueSheetItem;

        //삭제여부
        booleanBuilder.and(qCueSheetItem.delYn.eq("N"));
        //예비큐시트 여부
        //booleanBuilder.and(qCueSheetItem.spareYn.eq("N"));

        //큐시트아이디로 검색조건 설정.
        if (ObjectUtils.isEmpty(cueId) == false) {
            booleanBuilder.and(qCueSheetItem.cueSheet.cueId.eq(cueId));
        }

        return booleanBuilder;
    }*/


    //큐시트 등록
    public Long create(CueSheetCreateDTO cueSheetCreateDTO, String userId) throws Exception {

        cueSheetCreateDTO.setInputrId(userId);
        cueSheetCreateDTO.setCueOderVer(0);
        cueSheetCreateDTO.setCueVer(0);

        CueSheet cueSheet = cueSheetCreateMapper.toEntity(cueSheetCreateDTO);

        cueSheetRepository.save(cueSheet);

        Long cueId = cueSheet.getCueId();//생성된 큐시트 아이디.

        cueSheetHistCreate(cueId, cueSheet, userId);

        //큐시트 생성시 빈 스페어 큐시트 아이템 생성
        createSpareCueSheetItem(cueId, userId);

        //큐시트 토픽 메세지 전송
        cueSheetTopicService.sendCueTopic(cueSheet, "CueSheet Create", cueSheetCreateDTO);

        return cueId;
    }

    //큐시트 생성시 빈 스페어 큐시트 아이템 생성
    public void createSpareCueSheetItem(Long cueId, String userId) {

        CueSheet cueSheet = CueSheet.builder().cueId(cueId).build();

        CueSheetItem cueSheetItem = CueSheetItem.builder()
                .spareYn("Y")
                .cueItemDivCd("cue_item")
                .inputrId(userId)
                .cueSheet(cueSheet)
                .build();

        cueSheetItemRepository.save(cueSheetItem);

    }

    //큐시트 수정
    public void update(CueSheetUpdateDTO cueSheetUpdateDTO, Long cueId, String userId) throws Exception {

        //수정! 잠금여부? 잠금사용자, 잠금일시 도 수정할 때 정보를 넣어주나?

        CueSheet cueSheet = cueSheetFindOrFail(cueId);

        Long subrmId = cueSheet.getSubrmId();
        Long updateSubrmId = cueSheetUpdateDTO.getSubrmId();

        //프로그램 정보가 새로 들어왔을 시 프로그램 정보 업데이트를 위해 엔티티에서 프로그램 null값으로 셋팅
        ProgramSimpleDTO program = cueSheetUpdateDTO.getProgram();
        Program orgProgram = cueSheet.getProgram();

        //프로그램 정보 update면 기존프로그램 조회된 원본 엔티티에서 제거
        if (ObjectUtils.isEmpty(program) == false) {
            String brdcPgmId = program.getBrdcPgmId();
            String orgBrdcPgmId = orgProgram.getBrdcPgmId();
            if (orgBrdcPgmId.equals(Optional.ofNullable(brdcPgmId).orElse("")) == false) {

                //프로그램 정보 업데이트
                Program updateProgram =  programService.programFindOrFail(brdcPgmId);
                cueSheet.setProgram(updateProgram);

                List<Article> articleList = articleRepository.findArticleCue(cueId);

                for (Article article : articleList){

                    article.setBrdcPgmId(brdcPgmId);

                    articleRepository.save(article);
                    
                    //큐시트 프로그램 명 셋팅
                    CueSheet aritlcCueSheet = article.getCueSheet();
                    cueSheet.setBrdcPgmNm(updateProgram.getBrdcPgmNm());
                    article.setCueSheet(aritlcCueSheet);

                    elasticSearchArticleService.elasticPush(article);
                }
            }
        }

        BaseProgramSimpleDTO baseProgram = cueSheetUpdateDTO.getBaseProgram();

        if (ObjectUtils.isEmpty(baseProgram) == false) {

            Long basePgmschId = baseProgram.getBasePgmschId();

            BaseProgram updateBaseProgram = BaseProgram.builder().basePgmschId(basePgmschId).build();

            cueSheet.setBaseProgram(updateBaseProgram);

        }


        cueSheetUpdateDTO.setCueVer(cueSheet.getCueVer() + 1); //버전정보 + 1

        cueSheetUpdateMapper.updateFromDto(cueSheetUpdateDTO, cueSheet);

        cueSheetRepository.save(cueSheet);

        
        //부조 정보가 바뀌면 엘라스틱서치 인덱싱 업데이트
        if (subrmId.equals(Optional.ofNullable(updateSubrmId).orElse(0L)) ==false){


            List<Article> articleList = articleRepository.findArticleCue(cueId);

            for (Article article : articleList){

                elasticSearchArticleService.elasticPush(article);
            }

        }

        cueSheetHistUpdate(cueId, cueSheet, userId);

        //수정! 버전정보 안들어가나요?
        //큐시트 토픽 메세지 전송
        //sendCueTopic(cueSheet, "CuSheet Update", cueSheetUpdateDTO);
        cueSheetTopicService.sendCueTopic(cueSheet, "CuSheet Update", cueSheetUpdateDTO);
    }

    //큐시트 삭제
    public void delete(Long cueId, String userId) throws Exception {

        CueSheet cueSheet = cueSheetFindOrFail(cueId);

        CueSheetDTO cueSheetDTO = cueSheetMapper.toDto(cueSheet);

        cueSheetDTO.setDelrId(userId);
        cueSheetDTO.setDelDtm(new Date());
        cueSheetDTO.setDelYn("Y");

        cueSheetMapper.updateFromDto(cueSheetDTO, cueSheet);

        cueSheetRepository.save(cueSheet);

        cueSheetHistDelete(cueId, cueSheet, userId);

        //큐시트에 포함된 아이템 기사 삭제
        cueItemAnsArticleDelete(cueId, userId);

        //sendCueTopic(cueSheet, "CuSheet Delete", cueSheetDTO);
        cueSheetTopicService.sendCueTopic(cueSheet, "CuSheet Delete", cueSheetDTO);
    }

    //큐시트가 삭제될때 큐시트 아이템 및 기사도 삭제처리
    public void cueItemAnsArticleDelete(Long cueId, String userId) throws ParseException {

        List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findCueItemIdListAll(cueId);

        for (CueSheetItem cueSheetItem : cueSheetItemList) {

            CueSheetItemDTO cueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItem);

            cueSheetItemDTO.setDelrId(userId);
            cueSheetItemDTO.setDelYn("Y");
            cueSheetItemDTO.setDelDtm(new Date());

            cueSheetItemMapper.updateFromDto(cueSheetItemDTO, cueSheetItem);
            cueSheetItemRepository.save(cueSheetItem);

            //큐시트 아이템 삭제시 포함된 기사(복사된 기사)도 삭제
            Article getArticle = cueSheetItem.getArticle();

            if (ObjectUtils.isEmpty(getArticle) == false) {
                Long artclId = getArticle.getArtclId();

                //articleService.delete(artclId, userId);

                Article article = articleService.articleFindOrFail(artclId);

                //Article chkArticle = articleService.articleFindOrFailCueItem(artclId);

                if (ObjectUtils.isEmpty(article) == false) {

                    article.setDelrId(userId);
                    article.setDelYn("Y");
                    article.setDelDtm(new Date());

                    articleRepository.save(article);

                    //기사가 삭제될때 포함된 미디어정보도 같이 삭제처리 ( delYn = "Y")
                    Set<ArticleMedia> articleMedia = article.getArticleMedia();
                    deleteArticleMedia(articleMedia, userId);

                    //엘라스틱서치 등록
                    elasticSearchArticleService.elasticPush(article);
                }
            }


            //삭제되 아이템이 포함된 미디어정보 삭제 플레그 처리 ( delYn = "Y")
            Set<CueSheetMedia> cueSheetMedia = cueSheetItem.getCueSheetMedia();
            deleteMedia(cueSheetMedia, userId);
            //삭제되 아이템이 포함된 자막 삭제 플레그 처리 ( delYn = "Y")
            Set<CueSheetItemCap> cueSheetItemCap = cueSheetItem.getCueSheetItemCap();
            deleteItemCap(cueSheetItemCap, userId);

        }


    }

    //삭제되 아이템이 포함된 자막 삭제 플레그 처리 ( delYn = "Y")
    public void deleteItemCap(Set<CueSheetItemCap> cueSheetItemCapSet, String userId) {

        for (CueSheetItemCap cueSheetItemCap : cueSheetItemCapSet) {

            CueSheetItemCapDTO cueSheetItemCapDTO = cueSheetItemCapMapper.toDto(cueSheetItemCap);
            cueSheetItemCapDTO.setDelYn("Y");
            cueSheetItemCapDTO.setDelrId(userId);
            cueSheetItemCapDTO.setDelDtm(new Date());

            cueSheetItemCapMapper.updateFromDto(cueSheetItemCapDTO, cueSheetItemCap);

            cueSheetItemCapRepotitory.save(cueSheetItemCap);
        }

    }

    //기사가 삭제될때 포함된 미디어정보도 같이 삭제처리 ( delYn = "Y")
    public void deleteArticleMedia(Set<ArticleMedia> articleMediaSet, String userId) {

        for (ArticleMedia articleMedia : articleMediaSet) {

            ArticleMediaDTO articleMediaDTO = articleMediaMapper.toDto(articleMedia);
            articleMediaDTO.setDelYn("Y");
            articleMediaDTO.setDelrId(userId);
            articleMediaDTO.setDelDtm(new Date());

            articleMediaMapper.updateFromDto(articleMediaDTO, articleMedia);

            articleMediaRepository.save(articleMedia);

        }
    }

    //삭제되 아이템이 포함된 미디어정보 삭제 플레그 처리 ( delYn = "Y")
    public void deleteMedia(Set<CueSheetMedia> cueSheetMediaSet, String userId) {

        for (CueSheetMedia cueSheetMedia : cueSheetMediaSet) {

            CueSheetMediaDTO cueSheetMediaDTO = cueSheetMediaMapper.toDto(cueSheetMedia);
            cueSheetMediaDTO.setDelYn("Y");
            cueSheetMediaDTO.setDelrId(userId);
            cueSheetMediaDTO.setDelDtm(new Date());

            cueSheetMediaMapper.updateFromDto(cueSheetMediaDTO, cueSheetMedia);

            cueSheetMediaRepository.save(cueSheetMedia);

        }

    }

    //이미등록된 큐시트가 있는지 체크
    public int getCueSheetCount(CueSheetCreateDTO cueSheetCreateDTO) {

        int cueCnt = 0;

        String brdcDt = cueSheetCreateDTO.getBrdcDt();

        String brdcStartTime = cueSheetCreateDTO.getBrdcStartTime();

        String brdcEndTime = cueSheetCreateDTO.getBrdcEndTime();

        ProgramSimpleDTO program = cueSheetCreateDTO.getProgram();

        if (ObjectUtils.isEmpty(program) == false) {

            String brdcPgmId = program.getBrdcPgmId();

            cueCnt = cueSheetRepository.findCueProgram(brdcDt, brdcPgmId, brdcStartTime/*, brdcEndTime*/);

        }

        return cueCnt;

    }

    //이미등록된 큐시트가 있는지 체크
    public int getCueSheetCount2(String brdcDt, String brdcPgmId) {

        int cueCnt = 0;

        if (brdcPgmId != null && brdcPgmId.trim().isEmpty() == false) {

            cueCnt = cueSheetRepository.findCueProgram2(brdcDt, brdcPgmId);

        }

        return cueCnt;

    }

    //큐시트 이력 등록
    public void cueSheetHistCreate(Long cueId, CueSheet cueSheet, String userId) {

        //List<CueSheetItem> cueSheetItemList = cueSheet.getCueSheetItem(); //큐시트 아이템 엔티티 조회

        //조회된 큐시트 아이템 엔티티 DTO변환[ 엔티티를 바로 써버리면 필요없는 데이터가 다 보여지기 때문.]
        //List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemMapper.toDtoList(cueSheetItemList);
        /*String cueSheetItem = String.valueOf(cueSheetItemList);*/

        //큐시트 아이템 리스트를 String 으로 변환 리스트 정보를 text로 저장하기 위함.
        //String cueSheetItem = null;
        //if (CollectionUtils.isEmpty(cueSheetItemDTOList) == false) {
        //    cueSheetItem = cueSheetItemDTOList.stream()
        //            .map(n -> String.valueOf(n))
        //            .collect(Collectors.joining());
        // }

        //큐시트 이력에 넣어줄 큐시트 아이디 빌드
        CueSheetSimpleDTO cueSheetSimpleDTO = CueSheetSimpleDTO.builder().cueId(cueId).build();

        CueSheetHistCreateDTO cueSheetHistBuild = CueSheetHistCreateDTO.builder()
                .cueVer(cueSheet.getCueVer())
                .cueAction(ActionEnum.CREATE.getAction(ActionEnum.CREATE))
                .inputrId(userId)
                .cueSheet(cueSheetSimpleDTO)
                //.cueItemData(cueSheetItem)
                .build();

        CueSheetHist cueSheetHist = cueSheetHistCreateMapper.toEntity(cueSheetHistBuild);

        cueSheetHistRepository.save(cueSheetHist);

    }

    //큐시트 이력 수정
    public void cueSheetHistUpdate(Long cueId, CueSheet cueSheet, String userId) {

        //List<CueSheetItem> cueSheetItemList = cueSheet.getCueSheetItem(); //큐시트 아이템 엔티티 조회

        //조회된 큐시트 아이템 엔티티 DTO변환[ 엔티티를 바로 써버리면 필요없는 데이터가 다 보여지기 때문.]
        //List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemMapper.toDtoList(cueSheetItemList);
        /*String cueSheetItem = String.valueOf(cueSheetItemList);*/

        //큐시트 아이템 리스트를 String 으로 변환 리스트 정보를 text로 저장하기 위함.
        //String cueSheetItem = cueSheetItemDTOList.stream()
        //        .map(n -> String.valueOf(n))
        //        .collect(Collectors.joining());

        //큐시트 이력에 넣어줄 큐시트 아이디 빌드
        CueSheetSimpleDTO cueSheetSimpleDTO = CueSheetSimpleDTO.builder().cueId(cueId).build();

        //큐시트아이템 이력 버전 카운트 조회
        int getCueVer = cueSheetHistRepository.findCueVer(cueId);

        //큐시트 아이템 이력 articleDTO 빌드
        CueSheetHistCreateDTO cueSheetHistBuild = CueSheetHistCreateDTO.builder()
                .cueVer(cueSheet.getCueVer())
                .cueAction(ActionEnum.UPDATE.getAction(ActionEnum.UPDATE))
                //.cueItemData(cueSheetItem)
                .inputrId(userId)
                .cueSheet(cueSheetSimpleDTO)
                .build();

        //빌드된 큐시트아이템 이력 DTO를 엔티티 변환
        CueSheetHist cueSheetHist = cueSheetHistCreateMapper.toEntity(cueSheetHistBuild);

        //큐시트 아이템 등록
        cueSheetHistRepository.save(cueSheetHist);

    }

    //큐시트 이력 삭제
    public void cueSheetHistDelete(Long cueId, CueSheet cueSheet, String userId) {

        //List<CueSheetItem> cueSheetItemList = cueSheet.getCueSheetItem(); //큐시트 아이템 엔티티 조회

        //조회된 큐시트 아이템 엔티티 DTO변환[ 엔티티를 바로 써버리면 필요없는 데이터가 다 보여지기 때문.]
        //List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemMapper.toDtoList(cueSheetItemList);
        /*String cueSheetItem = String.valueOf(cueSheetItemList);*/

        //큐시트 아이템 리스트를 String 으로 변환 리스트 정보를 text로 저장하기 위함.
        //String cueSheetItem = cueSheetItemDTOList.stream()
        //        .map(n -> String.valueOf(n))
        //        .collect(Collectors.joining());

        //큐시트 이력에 넣어줄 큐시트 아이디 빌드
        CueSheetSimpleDTO cueSheetSimpleDTO = CueSheetSimpleDTO.builder().cueId(cueId).build();

        //큐시트아이템 이력 버전 카운트 조회
        int getCueVer = cueSheetHistRepository.findCueVer(cueId);

        //큐시트 아이템 이력 articleDTO 빌드
        CueSheetHistCreateDTO cueSheetHistBuild = CueSheetHistCreateDTO.builder()
                .cueVer(cueSheet.getCueVer())
                .cueAction(ActionEnum.DELETE.getAction(ActionEnum.DELETE))
                //.cueItemData(cueSheetItem)
                .inputrId(userId)
                .cueSheet(cueSheetSimpleDTO)
                .build();

        //빌드된 큐시트아이템 이력 DTO를 엔티티 변환
        CueSheetHist cueSheetHist = cueSheetHistCreateMapper.toEntity(cueSheetHistBuild);

        //큐시트 아이템 등록
        cueSheetHistRepository.save(cueSheetHist);

    }

    //큐시트 조회 및 존재여부 확인 [단건조회]
    public CueSheet cueSheetFindOrFail(Long cueId) {

        Optional<CueSheet> cueSheet = cueSheetRepository.findByCue(cueId);

        if (cueSheet.isPresent() == false) {
            throw new ResourceNotFoundException("큐시트를 찾을 수 없습니다. 큐시트 아이디 : " + cueId);
        }

        return cueSheet.get();

    }

    //큐시트 목록조회 조회조건 빌드
    private BooleanBuilder getSearch(Date sdate, Date edate, String brdcPgmId, String brdcPgmNm, Long deptCd, String searchWord) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QCueSheet qCueSheet = QCueSheet.cueSheet;

        booleanBuilder.and(qCueSheet.delYn.eq("N"));

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            String StringSdate = dateChangeHelper.dateToStringNoTime(sdate);//Date To String( yyyy-MM-dd )
            String stringEdate = dateChangeHelper.dateToStringNoTime(edate);//Date To String( yyyy-MM-dd )
            booleanBuilder.and(qCueSheet.brdcDt.between(StringSdate, stringEdate));
        }
        if (brdcPgmId != null && brdcPgmId.trim().isEmpty() == false) {
            booleanBuilder.and(qCueSheet.program.brdcPgmId.eq(brdcPgmId));
        }
        if (brdcPgmNm != null && brdcPgmNm.trim().isEmpty() == false) {
            booleanBuilder.and(qCueSheet.brdcPgmNm.contains(brdcPgmNm));
        }
        if (searchWord != null && searchWord.trim().isEmpty() == false) {
            booleanBuilder.and(qCueSheet.brdcPgmNm.contains(searchWord).or(qCueSheet.anc1Nm.contains(searchWord))
                    .or(qCueSheet.pd2Nm.contains(searchWord)));
        }
        if (ObjectUtils.isEmpty(deptCd) == false) {
            booleanBuilder.and(qCueSheet.deptCd.eq(deptCd));
        }

        return booleanBuilder;
    }

    //큐시트 락
    public CueSheetOrderLockResponsDTO cueSheetOrderLock(CueSheetOrderLockDTO cueSheetOrderLockDTO, Long cueId, String userId) throws Exception {

        Optional<CueSheet> cueSheetEntity = cueSheetRepository.findCueLock(cueId);

        if (cueSheetEntity.isPresent() == false) {
            throw new ResourceNotFoundException("큐시트를 찾을 수 없습니다. 큐시트 아이디 : " + cueId);
        }

        CueSheet cueSheet = cueSheetEntity.get();

        String lckYn = cueSheetOrderLockDTO.getLckYn();

        String lockYn = cueSheet.getLckYn();
        String lckrId = cueSheet.getLckrId();

        if ("Y".equals(lockYn) && userId.equals(lckrId) == false) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); // 잠금 사용자가 다르다.
        }

        if (lckYn != null && lckYn.equals("Y")) { //락 여부값이 Y 가 아닐경우 lck값 초기화
            //String userId = userAuthService.authUser.getUserId();
            cueSheetOrderLockDTO.setLckrId(userId);
            cueSheetOrderLockDTO.setLckDtm(new Date());
            cueSheetOrderLockDTO.setLckYn("Y");
            //cueSheetOrderLockDTO.setCueStCd("cuesheet_ready");
        } else {
            cueSheet.setLckrId(null);
            cueSheet.setLckDtm(null);
            cueSheet.setDelYn("N");
            //cueSheet.setCueStCd(null);
            //cueSheetOrderLockDTO.setLckYn("N");
        }

        cueSheetOrderLockMapper.updateFromDto(cueSheetOrderLockDTO, cueSheet);

        cueSheetRepository.save(cueSheet);

        String updateLockYn = cueSheet.getLckYn();

        //sendCueTopic(cueSheet, "CueSheet Lock Update - "+updateLockYn, null);
        cueSheetTopicService.sendCueTopic(cueSheet, "CueSheet Lock Update - " + updateLockYn, null);

        CueSheetOrderLockResponsDTO cueSheetDTO = new CueSheetOrderLockResponsDTO();
        cueSheetDTO.setCueId(cueId);
        cueSheetDTO.setLckYn(updateLockYn);
        cueSheetDTO.setLckDtm(cueSheet.getLckDtm());
        cueSheetDTO.setLckrId(cueSheet.getLckrId());
        //cueSheetDTO.setLckrNm(cueSheet.getLckrNm());

        return cueSheetDTO;
    }

    //큐시트 잠금 해제
    public void cueSheetUnLock(Long cueId, String userId) throws Exception {

        CueSheet cueSheet = cueSheetFindOrFail(cueId);

        //큐시트 lock정보 초기화
        cueSheet.setLckrId(null);
        cueSheet.setLckDtm(null);
        cueSheet.setLckYn("N");
        //cueSheet.setCueStCd(null);

        cueSheetRepository.save(cueSheet);

        User user = userService.userFindOrFail(userId);

        String userNm = user.getUserNm();

        cueSheetTopicService.sendUnLockTopic("CueSheet Unlock", cueId, userId, userNm);

    }

    //큐시트 복사
    public Long copy(Long cueId, String brdcPgmId, String brdcDt, String userId) throws Exception {

        CueSheet getCueSheet = cueSheetFindOrFail(cueId);//원본 큐시트 get

        CueSheetCreateDTO cueSheetCreateDTO = cueSheetCreateMapper.toDto(getCueSheet);//원본 큐시트의 데이터를 createDTO에set

        cueSheetCreateDTO.setInputrId(userId);
        cueSheetCreateDTO.setCueVer(0); //큐시트 버전 리셋
        cueSheetCreateDTO.setCueOderVer(0); //큐시트 오더버전 리셋
        getCueSheet.setInputDtm(null); // 원본 큐시트 입력시간 초기화

        if (brdcDt != null && brdcDt.trim().isEmpty() == false) { //방송일자를 변경해 큐시트를 복사할 경우
            cueSheetCreateDTO.setBrdcDt(brdcDt); //방송일자 set
        }
        if (brdcPgmId != null && brdcPgmId.trim().isEmpty() == false) { //방송프로그램을 변경해 큐시트를 복사할 경우
            ProgramSimpleDTO programSimpleDTO = ProgramSimpleDTO.builder()
                    .brdcPgmId(brdcPgmId).build(); //프로그램 아이디 빌드
            cueSheetCreateDTO.setProgram(programSimpleDTO);//프로그램 set

            ProgramDTO programDTO = programService.find(brdcPgmId);//방송프로그램 명 바꿔주기 위해 프로그램 get

            cueSheetCreateDTO.setBrdcPgmNm(programDTO.getBrdcPgmNm());//방송프로그램이 바뀔시 큐시트에 입력된 방송프로그램 명도 바꿔준다.
        }

        CueSheet cueSheet = cueSheetCreateMapper.toEntity(cueSheetCreateDTO);
        cueSheetRepository.save(cueSheet);


        Long newCueId = cueSheet.getCueId(); //복사된 큐시트 아이디 get

        copyCueItem(cueId, newCueId, userId, getCueSheet);//복사된 큐시트의 아이템 리스트 복사

        //큐시트 생성시 빈 스페어 큐시트 아이템 생성
        createSpareCueSheetItem(newCueId, userId);

        return newCueId;

    }

    //큐시트 복사[ 큐시트 아이템만 ]
    public Long cueItemcopy(Long cueId, Long newCueId, String userId) throws Exception {

        CueSheet getCueSheet = cueSheetFindOrFail(cueId);//원본 큐시트 get
        CueSheet newCueSheet = cueSheetFindOrFail(newCueId);//새로운 큐시트 get

        //Long newCueId = cueSheet.getCueId(); //복사된 큐시트 아이디 get

        copyCueItem(cueId, newCueId, userId, getCueSheet);//복사된 큐시트의 아이템 리스트 복사


        return newCueId;

    }

    //큐시트 아이템 복사.
    public void copyCueItem(Long orgCueId, Long newCueId, String userId, CueSheet CueSheet) throws Exception { //복사된 큐시트의 아이템 리스트 복사

        List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findByCueItemList(orgCueId); //원본 큐시트에 입력된 아이템 get

        Optional<Integer> cueItemMaxOrder = cueSheetItemRepository.findCueItemMaxOrder(newCueId);

        Integer ord = 0;
        if (cueItemMaxOrder.isPresent()){
            ord = cueItemMaxOrder.get();
        }

        List<CueSheetItemCreateDTO> cueSheetCreateDTOS = cueSheetItemCreateMapper.toDtoList(cueSheetItemList); //큐시트아이템 리스트 get

        CueSheetSimpleDTO cueSheetDTO = CueSheetSimpleDTO.builder().cueId(newCueId).build(); //새로 등록해줄 큐시

        for (CueSheetItemCreateDTO cueItemDTO : cueSheetCreateDTOS) {

            //큐시트 아이템에 기사get
            ArticleSimpleDTO article = cueItemDTO.getArticle();

            if (ObjectUtils.isEmpty(article) == false) { //기사 아이템일시

                Long artclId = article.getArtclId(); //기사아이디 get

                //기사 copy
                Article copyArticle = copyArticle(artclId, newCueId, userId);

                //복사된기사 ArticleSimpleDTO로 변환하여 저장
                ArticleSimpleDTO articleSimpleDTO = articleSimpleMapper.toDto(copyArticle);

                cueItemDTO.setArticle(articleSimpleDTO);
            }

            cueItemDTO.setCueSheet(cueSheetDTO); //복사된 큐시트 아이디 set

            Integer setDisplayCd = 0;
            if (ord == 0){
                cueItemDTO.setCueItemOrd(ord);
                setDisplayCd = ord+1;
            }else {
                cueItemDTO.setCueItemOrd(ord + 1);
                setDisplayCd = ord+2;
            }

            String displayCd = Integer.toString(setDisplayCd);

            cueItemDTO.setCueItemOrdCd(displayCd);
            CueSheetItem cueSheetItem = cueSheetItemCreateMapper.toEntity(cueItemDTO);
            cueSheetItemRepository.save(cueSheetItem);

            Long cueItemId = cueSheetItem.getCueItemId();

            //큐시트 아이템 자막 리스트 get
            List<CueSheetItemCapCreateDTO> cueSheetItemCapDTOList = cueItemDTO.getCueSheetItemCap();
            //큐시트 아이템 자막 리스트가 있으면 등록
            if (CollectionUtils.isEmpty(cueSheetItemCapDTOList) == false) {
                createCap(cueSheetItemCapDTOList, cueItemId, userId);//큐시트 아이템 자막 리스트 등록
            }

            //큐시트 아이템 방송아이콘 List 등록
            List<CueSheetItemSymbolCreateDTO> cueSheetItemSymbol = cueItemDTO.getCueSheetItemSymbol();
            createItemSymbol(cueSheetItemSymbol, cueItemId);

            //큐시트 미디어 등록
            List<CueSheetMediaCreateDTO> cueSheetMedia = cueItemDTO.getCueSheetMedia();
            createMedia(cueSheetMedia, cueItemId, userId, CueSheet);

            ++ord;
        }

    }

    //큐시트 미디어 등록[ 큐시트 복사 ]
    public void createMedia(List<CueSheetMediaCreateDTO> cueSheetMedia, Long cueItemId, String userId, CueSheet cueSheet) throws Exception {

        CueSheetItemSimpleDTO cueSheetItemSimpleDTO = CueSheetItemSimpleDTO.builder().cueItemId(cueItemId).build();

        Long subrmId = cueSheet.getSubrmId();
        FacilityManage facilityManage = facilityManageService.findFacility(subrmId);
        String subrmNm = facilityManage.getFcltyDivCd();

        for (CueSheetMediaCreateDTO cueSheetMediaDTO : cueSheetMedia) {

            String mediaTypCd = cueSheetMediaDTO.getMediaTypCd();

            if ("media_typ_001".equals(mediaTypCd)) {

                cueSheetMediaDTO.setCueSheetItem(cueSheetItemSimpleDTO);
                cueSheetMediaDTO.setInputrId(userId);

                CueSheetMedia cueSheetMediaEntity = cueSheetMediaCreateMapper.toEntity(cueSheetMediaDTO);

                cueSheetMediaRepository.save(cueSheetMediaEntity);

                Long mediaId = cueSheetMediaEntity.getCueMediaId();
                Integer contentId = cueSheetMediaEntity.getContId();

                //추후에 T는 클라우드 콘피그 교체
                lboxService.cueMediaTransfer(mediaId, contentId, subrmNm, false, false, "T");

            }

        }
    }

    //큐시트 아이템 방송아이콘 List 등록
    public void createItemSymbol(List<CueSheetItemSymbolCreateDTO> cueSheetItemSymbol, Long cueItemId) {

        //방송아이콩에 넣어줄 큐시트 아이템 아이디 빌드
        CueSheetItemSimpleDTO cueSheetItemSimpleDTO = CueSheetItemSimpleDTO.builder().cueItemId(cueItemId).build();

        //방송아이콘  복사
        for (CueSheetItemSymbolCreateDTO symbol : cueSheetItemSymbol) {

            symbol.setCueSheetItem(cueSheetItemSimpleDTO);

            CueSheetItemSymbol copySymbol = cueSheetItemSymbolCreateMapper.toEntity(symbol);

            cueSheetItemSymbolRepository.save(copySymbol);

        }

    }

    //큐시트 아이템 자막 등록
    public void createCap(List<CueSheetItemCapCreateDTO> cueSheetItemCapDTOList, Long cueItemId, String userId) {

        //큐시트 아이템 아이디 빌드
        CueSheetItemSimpleDTO cueSheetItem = CueSheetItemSimpleDTO.builder().cueItemId(cueItemId).build();

        //큐시트 아이템 자막 리스트 등록
        for (CueSheetItemCapCreateDTO capCreateDTO : cueSheetItemCapDTOList) {

            capCreateDTO.setCueSheetItem(cueSheetItem); //큐시트 아이템 아이디 set
            capCreateDTO.setInputrId(userId);//등록자 set

            CueSheetItemCap cueSheetItemCap = cueSheetItemCapCreateMapper.toEntity(capCreateDTO);

            cueSheetItemCapRepotitory.save(cueSheetItemCap);//등록
        }
    }

    //큐시트 토픽 메세지 전송
   /* public void sendCueTopic(CueSheet cueSheet, String eventId, Object object) throws JsonProcessingException {

        Long cueId = cueSheet.getCueId();

        //토픽메세지 ArticleTopicDTO Json으로 변환후 send
        CueSheetTakerTopicDTO cueSheetTakerTopicDTO = new CueSheetTakerTopicDTO();
        //모델부분은 안넣어줘도 될꺼같음.
        cueSheetTakerTopicDTO.setEventId(eventId);
        cueSheetTakerTopicDTO.setCueId(cueId);
        cueSheetTakerTopicDTO.setCueVer(cueSheet.getCueVer());
        //takerCueSheetTopicDTO.setCueSheet(object);
        String json = marshallingJsonHelper.MarshallingJson(cueSheetTakerTopicDTO);


        //interface에 큐메세지 전송
        topicSendService.topicInterface(json);
        //web에 큐메세지 전송
        topicSendService.topicWeb(json);

    }*/

    //기사 복사
    public Article copyArticle(Long artclId, Long cueId, String userId) throws Exception {

        /*********************/
        /* 기사를 저장하는 부분 */

        //기사 아이디로 기사 검색
        Optional<Article> getArticle = articleRepository.findArticle(artclId);
        //조회된 기사 빈값 검사.

        if (getArticle.isPresent() == false) {
            throw new ResourceNotFoundException(String.format("기사아이디에 해당하는 기사가 없습니다. {%ld}", artclId));
        }

        //큐시트 아이디로 큐시트 조회 및 존재유무 확인.
        CueSheet cueSheet = cueSheetFindOrFail(cueId);

        //조회된 기사 get
        Article article = getArticle.get();

        //기사 시퀀스[오리지널 기사 0 = 그밑으로 복사된 기사 + 1 증가]
        int artclOrd = article.getArtclOrd() + 1;

        // 기사 저장하기 위한 기사복사 엔터티 생성
        Article articleEntity = getArticleEntity(article, artclOrd, cueSheet);
        articleRepository.save(articleEntity); //복사된 기사 등록


        //큐시트 복사시 검색엔진 인덱싱.
        elasticSearchArticleService.elasticPush(articleEntity);

        Set<ArticleCap> articleCapList = article.getArticleCap(); //기사자막 리스트 get
        Set<AnchorCap> anchorCapList = article.getAnchorCap(); //앵커자막 리스트 get

        articleService.articleActionLogCreate(article, userId); //기사 액션 로그 등록
        Long articleHistId = articleService.createArticleHist(article);//기사 이력 create

        /*********************/
        /* 기사 자막을 저장하는 부분 */

        if (CollectionUtils.isEmpty(articleCapList) == false) {
            for (ArticleCap articleCap : articleCapList) {

                ArticleCap articleCapEntity = articleCapEntityBuild(articleCap, articleEntity);

                articleCapRepository.save(articleCapEntity);

                articleService.createArticleCapHist(articleCapEntity, articleHistId); //기사자막 이력 저장.
            }
        }

        /*********************/
        /* 앵커 자막을 저장하는 부분 */

        if (CollectionUtils.isEmpty(anchorCapList) == false) {
            for (AnchorCap articleCap : anchorCapList) {

                AnchorCap anchorCapEntity = anchorCapEntityBuild(articleCap, articleEntity);

                anchorCapRepository.save(anchorCapEntity);

                articleService.createAnchorCapHist(anchorCapEntity, articleHistId); //앵커자막 이력 등록

            }
        }

        /*********************/
        /* 미디어 정보 저장 하는 부분 */

        //기사 아이디로 기사 미디어 조회
        List<ArticleMedia> articleMediaList = articleMediaRepository.findArticleMediaList(artclId);
        //List<ArticleMedia> articleMediaList = articleMediaRepository.findArticleMediaList(artclId);

        //조회된 기사 미디어가 있으면 복사된 기사 아이디로 기사미디어 신규 저장.
        if (ObjectUtils.isEmpty(articleMediaList) == false) {

            for (ArticleMedia articleMedia : articleMediaList) {

                ArticleMedia articleMediaEntity = getArticleMediaEntity(articleMedia, articleEntity);

                articleMediaRepository.save(articleMediaEntity);

            }
        }

        /*********************/
        /* 기사 테그를 저장하는 부분 */

        List<ArticleTag> articleTagList = articleTagRepository.findArticleTag(artclId);

        for (ArticleTag articleTag : articleTagList){

            Tag tag = articleTag.getTag();

            ArticleTag articleTagEntity = ArticleTag.builder()
                    .article(articleEntity)
                    .tag(tag)
                    .build();

            articleTagRepository.save(articleTagEntity);

        }

        return articleEntity;

    }

    // 기사 저장하기 위한 엔터티 만들기 2021-10-27
    private Article getArticleEntity(Article article, int artclOrd, CueSheet cueSheet) {

        Program program = cueSheet.getProgram();
        String brdcPgmId = "";
        //String brdcSchdDtm = "";

        //방송프로그램이 있을경우 방송프로그램 아이디 set
        if (ObjectUtils.isEmpty(program) == false) {

            brdcPgmId = program.getBrdcPgmId();
            //brdcSchdDtm = program.
        }

        Long orgArtclId = article.getOrgArtclId();//원본기사 아이디
        Long artclId = article.getOrgArtclId();
        Integer artclOrder = article.getArtclOrd();

        String getOrgApprvDivCd = article.getApprvDivCd(); //원본 픽스구분 코드를 가져온다.
        String artclFixUser = article.getArtclFixUser(); // 원본 기사 픽스자를 가져온다
        String editorFixUser = article.getEditorFixUser(); // 원본 에디터 픽스자를 가져온다
        String newApprvDivCd = ""; //복사본에 대입해줄 픽스구분코드

        //원본 픽스구분값이 앵커픽스,데이커 픽스일 경우 article_fix, editor_fix 로변경
        //에디터 픽스자가 있을경우 에디터픽스로, 아닐경우 기사픽스로 셋팅
        if (editorFixUser != null && editorFixUser.trim().isEmpty() == false) {
            newApprvDivCd = "editor_fix";
        } else if (artclFixUser != null && artclFixUser.trim().isEmpty() == false) {
            newApprvDivCd = "article_fix";
        } else if (editorFixUser == null || editorFixUser.trim().isEmpty()) {

            if (artclFixUser == null || editorFixUser.trim().isEmpty()) {
                newApprvDivCd = "fix_none";
            }
        }

        if (artclOrder == 0) { // 최초 복사일시

            return Article.builder()
                    .chDivCd(article.getChDivCd())
                    .artclKindCd(article.getArtclKindCd())
                    .artclFrmCd(article.getArtclFrmCd())
                    .artclDivCd(article.getArtclDivCd())
                    .artclFldCd(article.getArtclFldCd())
                    .apprvDivCd(newApprvDivCd)//픽스구분코트
                    .prdDivCd(article.getPrdDivCd())
                    .artclTypCd(article.getArtclTypCd())
                    .artclTypDtlCd(article.getArtclTypDtlCd())
                    .artclCateCd(article.getArtclCateCd())
                    .artclTitl(article.getArtclTitl())
                    .artclTitlEn(article.getArtclTitlEn())
                    .artclCtt(article.getArtclCtt())
                    .ancMentCtt(article.getAncMentCtt())
                    .rptrNm(article.getRptrNm())
                    .userGrpId(article.getUserGrpId())
                    .artclReqdSecDivYn(article.getArtclReqdSecDivYn())
                    .artclReqdSec(article.getArtclReqdSec())
                    //.lckYn(article.getLckYn())
                    //.lckDtm(article.getLckDtm())
                    .apprvDtm(article.getApprvDtm())
                    .artclOrd(artclOrd)//기사 시퀀스 +1
                    .brdcCnt(article.getBrdcCnt())
                    .orgArtclId(article.getOrgArtclId())//원본기사 아이디set
                    .urgYn(article.getUrgYn())
                    .frnotiYn(article.getFrnotiYn())
                    .embgYn(article.getEmbgYn())
                    .embgDtm(article.getEmbgDtm())
                    .inputrNm(article.getInputrNm())
                    .delYn(article.getDelYn())
                    .notiYn(article.getNotiYn())
                    .regAppTyp(article.getRegAppTyp())
                    .brdcPgmId(brdcPgmId) //프로그램 아이디
                    .brdcSchdDtm(article.getBrdcSchdDtm())//방송시간
                    .inputrId(article.getInputrId())
                    //.updtrId(article.getUpdtrId())
                    //.delrId(article.getDelrId())
                    .apprvrId(article.getApprvrId())
                    //.lckrId(article.getLckrId())
                    .rptrId(article.getRptrId())
                    .artclCttTime(article.getArtclCttTime())
                    .ancMentCttTime(article.getAncMentCttTime())
                    .artclExtTime(article.getArtclExtTime())
                    .videoTime(article.getVideoTime())
                    .deptCd(article.getDeptCd())
                    //.deviceCd(article.getDeviceCd())
                    .parentArtlcId(article.getArtclId())//복사한 기사 아이디 set
                    .issue(article.getIssue())
                    .cueSheet(cueSheet)//큐시트 아이디 set
                    .artclFixUser(article.getArtclFixUser())
                    .editorFixUser(article.getEditorFixUser())
                    //.anchorFixUser(article.getAnchorFixUser())
                    //.deskFixUser(article.getDeskFixUser())
                    .artclFixDtm(article.getArtclFixDtm())
                    .editorFixDtm(article.getEditorFixDtm())
                    //.anchorFixDtm(article.getAnchorFixDtm())
                    //.deskFixDtm(article.getDeskFixDtm())
                    .build();
        } else { //원본기사 아이디가 있을시[복사된 기사 다시 복사일시]

            return Article.builder()
                    .chDivCd(article.getChDivCd())
                    .artclKindCd(article.getArtclKindCd())
                    .artclFrmCd(article.getArtclFrmCd())
                    .artclDivCd(article.getArtclDivCd())
                    .artclFldCd(article.getArtclFldCd())
                    .apprvDivCd(newApprvDivCd) //픽스구분코트
                    .prdDivCd(article.getPrdDivCd())
                    .artclTypCd(article.getArtclTypCd())
                    .artclTypDtlCd(article.getArtclTypDtlCd())
                    .artclCateCd(article.getArtclCateCd())
                    .artclTitl(article.getArtclTitl())
                    .artclTitlEn(article.getArtclTitlEn())
                    .artclCtt(article.getArtclCtt())
                    .ancMentCtt(article.getAncMentCtt())
                    .rptrNm(article.getRptrNm())
                    .userGrpId(article.getUserGrpId())
                    .artclReqdSecDivYn(article.getArtclReqdSecDivYn())
                    .artclReqdSec(article.getArtclReqdSec())
                    //.lckYn(article.getLckYn())
                    //.lckDtm(article.getLckDtm())
                    .apprvDtm(article.getApprvDtm())
                    .artclOrd(artclOrd)//기사 시퀀스 +1
                    .brdcCnt(article.getBrdcCnt())
                    .orgArtclId(article.getOrgArtclId())//원본기사 아이디set
                    .urgYn(article.getUrgYn())
                    .frnotiYn(article.getFrnotiYn())
                    .embgYn(article.getEmbgYn())
                    .embgDtm(article.getEmbgDtm())
                    .inputrNm(article.getInputrNm())
                    .delYn(article.getDelYn())
                    .notiYn(article.getNotiYn())
                    .regAppTyp(article.getRegAppTyp())
                    .brdcPgmId(brdcPgmId)
                    .brdcSchdDtm(article.getBrdcSchdDtm())
                    .inputrId(article.getInputrId())
                    //.updtrId(article.getUpdtrId())
                    //.delrId(article.getDelrId())
                    .apprvrId(article.getApprvrId())
                    //.lckrId(article.getLckrId())
                    .rptrId(article.getRptrId())
                    .artclCttTime(article.getArtclCttTime())
                    .ancMentCttTime(article.getAncMentCttTime())
                    .artclExtTime(article.getArtclExtTime())
                    .videoTime(article.getVideoTime())
                    .deptCd(article.getDeptCd())
                    //.deviceCd(article.getDeviceCd())
                    .parentArtlcId(article.getArtclId())//복사한 기사 아이디 set
                    .issue(article.getIssue())
                    .cueSheet(cueSheet)//큐시트 아이디 set
                    .artclFixUser(article.getArtclFixUser())
                    .editorFixUser(article.getEditorFixUser())
                    //.anchorFixUser(article.getAnchorFixUser())
                    //.deskFixUser(article.getDeskFixUser())
                    .artclFixDtm(article.getArtclFixDtm())
                    .editorFixDtm(article.getEditorFixDtm())
                    //.anchorFixDtm(article.getAnchorFixDtm())
                    //.deskFixDtm(article.getDeskFixDtm())
                    .build();
        }
    }

    //기사자막 빌드
    public ArticleCap articleCapEntityBuild(ArticleCap getArticleCap, Article articleEntity) {
        return ArticleCap.builder()
                .capDivCd(getArticleCap.getCapDivCd())
                .lnNo(getArticleCap.getLnNo())
                .lnOrd(getArticleCap.getLnOrd())
                .capCtt(getArticleCap.getCapCtt())
                .capRmk(getArticleCap.getCapRmk())
                .article(articleEntity)
                .capTemplate(getArticleCap.getCapTemplate())
                .symbol(getArticleCap.getSymbol())
                .build();
    }

    //앵커자막 빌드
    public AnchorCap anchorCapEntityBuild(AnchorCap articleCap, Article articleEntity) {

        return AnchorCap.builder()
                .capDivCd(articleCap.getCapDivCd())
                .lnNo(articleCap.getLnNo())
                .lnOrd(articleCap.getLnOrd())
                .capCtt(articleCap.getCapCtt())
                .capRmk(articleCap.getCapRmk())
                .article(articleEntity)
                .capTemplate(articleCap.getCapTemplate())
                .symbol(articleCap.getSymbol())
                .build();

    }

    public ArticleMedia getArticleMediaEntity(ArticleMedia articleMedia, Article articleEntity) {

        return ArticleMedia.builder()
                .mediaTypCd(articleMedia.getMediaTypCd())
                .mediaOrd(articleMedia.getMediaOrd())
                .contId(articleMedia.getContId())
                .trnsfFileNm(articleMedia.getTrnsfFileNm())
                .mediaDurtn(articleMedia.getMediaDurtn())
                .mediaMtchDtm(articleMedia.getMediaMtchDtm())
                .trnsfStCd(articleMedia.getTrnsfStCd())
                .assnStCd(articleMedia.getAssnStCd())
                .videoEdtrNm(articleMedia.getVideoEdtrNm())
                .delYn(articleMedia.getDelYn())
                .delDtm(articleMedia.getDelDtm())
                .inputrId(articleMedia.getInputrId())
                .updtrId(articleMedia.getUpdtrId())
                .delrId(articleMedia.getDelrId())
                .videoEdtrId(articleMedia.getVideoEdtrId())
                .videoId(articleMedia.getVideoId())
                .artclMediaTitl(articleMedia.getArtclMediaTitl())
                .article(articleEntity)
                .build();
    }

    //큐시트 자막 출력 [ xml ]
    public String capDownload(Long cueId, String brdcPgmId) {

        List<CueSheetCapDownloadCgDTO> cueSheetCapDownloadCgDTOS = new ArrayList<>();

        int seq = 1001;

        //CueSheet cueSheet = cueSheetFindOrFail(cueId);

        //CueSheetDTO cueSheetDTO = cueSheetMapper.toDto(cueSheet);
        List<CueCapTmplt> cueCapTmpltList = cueCapTmpltRepository.findCueCapTmpltByPgmId(brdcPgmId);

        List<String> divCdList = new ArrayList<>();
        for (CueCapTmplt cueCapTmplt : cueCapTmpltList) {
            String divCd = cueCapTmplt.getCgDivCd();
            divCdList.add(divCd);
        }


        List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findByCueItemList(cueId); //큐시트 아이템 출력

        for (CueSheetItem cueSheetItem : cueSheetItemList) {

            Article article = cueSheetItem.getArticle(); //기사 정보를 가져온다.
            Long cueItemId = cueSheetItem.getCueItemId();

            if (ObjectUtils.isEmpty(article)) { //일반 큐시트 아이템, 템플릿아이템

                List<CueSheetItemCap> cueSheetItemCapList = cueSheetItemCapRepotitory.findCueSheetItemCapList(cueItemId);

                for (CueSheetItemCap cueSheetItemCap : cueSheetItemCapList) {

                    String cueCapDivCd = cueSheetItemCap.getCueItemCapDivCd();
                    String capCtt = cueSheetItemCap.getCapCtt();

                    for (CueCapTmplt cueCapTmplt : cueCapTmpltList) {

                        String capDivCd = cueCapTmplt.getCgDivCd();
                        CapTemplate capTemplate = cueCapTmplt.getCapTemplate();

                        if (divCdList.contains(cueCapDivCd)) {
                            if (capDivCd.equals(cueCapDivCd)) {

                                CueSheetCapDownloadCgDTO cueSheetCapDownloadCg = CueSheetCapDownloadCgDTO.builder()
                                        .page(seq)
                                        .content(capCtt)
                                        .template(capTemplate.getCapTmpltNm())
                                        .build();

                                cueSheetCapDownloadCgDTOS.add(cueSheetCapDownloadCg);

                                ++seq;
                            }
                        } else {
                            CueSheetCapDownloadCgDTO cueSheetCapDownloadCg = CueSheetCapDownloadCgDTO.builder()
                                    .page(seq)
                                    .content(capCtt)
                                    .template("")
                                    .build();

                            cueSheetCapDownloadCgDTOS.add(cueSheetCapDownloadCg);

                            ++seq;
                        }
                    }

                }

            } else {

                Set<AnchorCap> anchorCapList = article.getAnchorCap();

                for (AnchorCap anchorCap : anchorCapList) {

                    String anchorCapDivCd = anchorCap.getCapDivCd();
                    String capCtt = anchorCap.getCapCtt();

                    for (CueCapTmplt cueCapTmplt : cueCapTmpltList) {
                        String capDivCd = cueCapTmplt.getCgDivCd();
                        CapTemplate capTemplate = cueCapTmplt.getCapTemplate();

                        if (divCdList.contains(anchorCapDivCd)) {
                            if (capDivCd.equals(anchorCapDivCd)) {

                                CueSheetCapDownloadCgDTO cueSheetCapDownloadCg = CueSheetCapDownloadCgDTO.builder()
                                        .page(seq)
                                        .content(capCtt)
                                        .template(capTemplate.getCapTmpltNm())
                                        .build();

                                cueSheetCapDownloadCgDTOS.add(cueSheetCapDownloadCg);

                                ++seq;
                            }
                        } else {
                            CueSheetCapDownloadCgDTO cueSheetCapDownloadCg = CueSheetCapDownloadCgDTO.builder()
                                    .page(seq)
                                    .content(capCtt)
                                    .template("")
                                    .build();

                            cueSheetCapDownloadCgDTOS.add(cueSheetCapDownloadCg);

                            ++seq;
                        }
                    }
                }


                Set<ArticleCap> articleCapList = article.getArticleCap();

                for (ArticleCap articleCap : articleCapList) {

                    String anchorCapDivCd = articleCap.getCapDivCd();
                    String capCtt = articleCap.getCapCtt();

                    for (CueCapTmplt cueCapTmplt : cueCapTmpltList) {
                        String capDivCd = cueCapTmplt.getCgDivCd();
                        CapTemplate capTemplate = cueCapTmplt.getCapTemplate();

                        if (divCdList.contains(anchorCapDivCd)) {
                            if (capDivCd.equals(anchorCapDivCd)) {

                                CueSheetCapDownloadCgDTO cueSheetCapDownloadCg = CueSheetCapDownloadCgDTO.builder()
                                        .page(seq)
                                        .content(capCtt)
                                        .template(capTemplate.getCapTmpltNm())
                                        .build();

                                cueSheetCapDownloadCgDTOS.add(cueSheetCapDownloadCg);

                                ++seq;
                            }
                        } else {
                            CueSheetCapDownloadCgDTO cueSheetCapDownloadCg = CueSheetCapDownloadCgDTO.builder()
                                    .page(seq)
                                    .content(capCtt)
                                    .template("")
                                    .build();

                            cueSheetCapDownloadCgDTOS.add(cueSheetCapDownloadCg);

                            ++seq;
                        }
                    }
                }


            }

        }


        CueSheetCapDownloadXMLDTO cueSheetCapDownloadXMLDTO = new CueSheetCapDownloadXMLDTO();
        cueSheetCapDownloadXMLDTO.setCd(cueSheetCapDownloadCgDTOS);

        //articleDTO TO XML 파싱
        String xml = JAXBXmlHelper.marshal(cueSheetCapDownloadXMLDTO, CueSheetCapDownloadXMLDTO.class);


        return xml;
    }

    //큐시트 락이 6시간이상 걸려있는 큐시트 체크후 락 해제
    public void cueSheetLockChk() {

        //현재시간에서 -6시간 뺀 Date값을 가져온다
        Date formatDate = dateChangeHelper.LockChkTime();

        List<CueSheet> cueSheetList = cueSheetRepository.findLockCueChkList(formatDate, "Y");

        for (CueSheet cueSheet : cueSheetList) {

            Long cueId = cueSheet.getCueId();
            String lckrNm = cueSheet.getLckrNm();
            String lckrId = cueSheet.getLckrId();

            CueSheetDTO cueSheetDTO = cueSheetMapper.toDto(cueSheet);
            cueSheetDTO.setLckDtm(null);
            cueSheetDTO.setLckrId(null);
            cueSheetDTO.setLckYn("N");

            cueSheetMapper.updateFromDto(cueSheetDTO, cueSheet);
            cueSheetRepository.save(cueSheet);

            log.info("CueSheet UnLock From Server. CueSheet Id : " + cueId + " Lock User Name : " + lckrNm + " ( " + lckrId + " )");
        }
    }

}

    /*//조회된 큐시트아이템 기사형식의 방송아이콘 URL추가생성
    public ArticleCueItemDTO symbolUrlSet(ArticleCueItemDTO articleCueItemDTO){

        //ArticleCueItemDTO returnArticleCueItemDTO = new ArticleCueItemDTO(); //리턴시킬 큐시트 아이템 생성

        if (ObjectUtils.isEmpty(articleCueItemDTO) == false){ //큐시트 아이템 기사가 있는 경우.

            //큐시트아이템 기사에 기사자막 리스트 get
            List<ArticleCapSimpleDTO> articleCapSimpleDTOList = articleCueItemDTO.getArticleCapDTO();
            //큐시트아이템 기사에 앵커자막 리스트 get
            List<AnchorCapSimpleDTO> anchorCapSimpleDTOList = articleCueItemDTO.getAnchorCap();

            //새로 생성하여 set해줄 기사자막 생성
            List<ArticleCapSimpleDTO> returnArticleCapSimpleDTOList = new ArrayList<>();
            //새로 생성하여 set해줄 앵커자막 생성
            List<AnchorCapSimpleDTO> returnAnchorCapSimpleDTOList = new ArrayList<>();

            if (CollectionUtils.isEmpty(articleCapSimpleDTOList) == false){ //큐시트아이템 기사에 기사자막 리스트가 있는경우
                for (ArticleCapSimpleDTO articleCapSimpleDTO : articleCapSimpleDTOList){ //기사자막 리스트한껀식 Url set

                    SymbolDTO symbolDTO = new SymbolDTO(); //새로 값을 받을 방송아이콘DTO생성

                    symbolDTO = articleCapSimpleDTO.getSymbol();//기사자막에 방송아이콘get

                    if (ObjectUtils.isEmpty(symbolDTO) == false){//기사자막에 방송아이콘이 있는경우
                        //기사자막에 방송아이콘에 파일로그 get
                        String fileLoc = articleCapSimpleDTO.getSymbol().getAttachFile().getFileLoc();
                        //파일 Url+ 가져온 파일로그
                        String url = fileUrl + fileLoc; //url + 파일로그
                        //생성된 파일 url set
                        symbolDTO.setUrl(url);

                        articleCapSimpleDTO.setSymbol(symbolDTO); //기사자막에 파일url 셋팅된 방송아이콘  set

                    }
                    returnArticleCapSimpleDTOList.add(articleCapSimpleDTO);//새로 생성된 기사자막 리스트에 url추가된 기사자막 추가
                    articleCueItemDTO.setArticleCapDTO(returnArticleCapSimpleDTOList);
                }
                //returnArticleCueItemDTO.setArticleCapDTO(returnArticleCapSimpleDTOList); //새로 생성된 기사자막 리스트 기사에 set
            }

            if (CollectionUtils.isEmpty(anchorCapSimpleDTOList) == false){ //큐시트아이템 기사에 앵커자막 리스트가 있는경우
                for (AnchorCapSimpleDTO anchorCapSimpleDTO : anchorCapSimpleDTOList){ //기사자막 리스트한껀식 Url set

                    SymbolDTO symbolDTO = new SymbolDTO(); //새로 값을 받을 방송아이콘DTO생성

                    symbolDTO = anchorCapSimpleDTO.getSymbol();//기사자막에 방송아이콘get

                    if (ObjectUtils.isEmpty(symbolDTO) == false){//앵커자막에 방송아이콘이 있는경우
                        //앵커자막에 방송아이콘에 파일로그 get
                        String fileLoc = anchorCapSimpleDTO.getSymbol().getAttachFile().getFileLoc();
                        //파일 Url+ 가져온 파일로그
                        String url = fileUrl + fileLoc; //url + 파일로그
                        //생성된 파일 url set
                        symbolDTO.setUrl(url);

                        anchorCapSimpleDTO.setSymbol(symbolDTO); //앵커자막에 파일url 셋팅된 방송아이콘  set

                    }
                    returnAnchorCapSimpleDTOList.add(anchorCapSimpleDTO);//새로 생성된 앵커자막 리스트에 url추가된 기사자막 추가
                    articleCueItemDTO.setAnchorCap(returnAnchorCapSimpleDTOList);
                }
                //returnArticleCueItemDTO.setAnchorCap(returnAnchorCapSimpleDTOList); //새로 생성된 앵커자막 리스트 기사에 set
            }

        }
        return articleCueItemDTO;//방송아이콘URL추가된 기사 리턴
    }*/

//큐시트 상세조회***************JPA연관관계 설정당시 조회
    /*public CueSheetDTO find(Long cueId){

        CueSheet cueSheet = cueSheetFindOrFail(cueId);

        CueSheetDTO cueSheetDTO = cueSheetMapper.toDto(cueSheet);

        List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetDTO.getCueSheetItem();

        List<CueSheetItemDTO> newCueSheetItemDTOList = new ArrayList<>();
        for (Iterator<CueSheetItemDTO> itr = cueSheetItemDTOList.iterator(); itr.hasNext();){

            CueSheetItemDTO cueSheetItemDTO = itr.next();

            String delYn = cueSheetItemDTO.getDelYn();//큐시트 아이템의 삭제여부 값
            ArticleCueItemDTO articleCueItemDTO = cueSheetItemDTO.getArticle();//큐시트 아이템의 기사 정보 get

            if (ObjectUtils.isEmpty(articleCueItemDTO) == false){ //큐시트 아이템에 기사가 포함된 경우.
                String articleDelYn = articleCueItemDTO.getDelYn();//큐시트 아이템에 포함된 기사의 삭제 여부값
                if ("Y".equals(articleDelYn)){ //기사 삭제 값이 Y인경우 조회된 큐시트아이템 삭제
                    continue;
                }
                *//*articleCueItemDTO = symbolUrlSet(articleCueItemDTO);//기사에 방송아이콘의 방송아이콘URL 추가.
                cueSheetItemDTO.setArticle(articleCueItemDTO);//방송아이콘 추가한 기사 큐시트 아이템에 set*//*
            }
            if ("Y".equals(delYn)) { //조회된 큐시트 아이템 삭제여부값이 Y인 경우/.
                continue;
            }

            newCueSheetItemDTOList.add(cueSheetItemDTO);
        }
        cueSheetDTO.setCueSheetItem(newCueSheetItemDTOList);

        return cueSheetDTO;

    }*/