package com.gemiso.zodiac.app.cueSheet;

import com.gemiso.zodiac.app.article.dto.ArticleCueItemDTO;
import com.gemiso.zodiac.app.cueSheet.dto.*;
import com.gemiso.zodiac.app.cueSheet.mapper.*;
import com.gemiso.zodiac.app.cueSheetHist.CueSheetHist;
import com.gemiso.zodiac.app.cueSheetHist.CueSheetHistRepository;
import com.gemiso.zodiac.app.cueSheetHist.dto.CueSheetHistCreateDTO;
import com.gemiso.zodiac.app.cueSheetHist.mapper.CueSheetHistCreateMapper;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItemRepository;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemCreateDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemDTO;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemCreateMapper;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemMapper;
import com.gemiso.zodiac.app.dailyProgram.DailyProgramService;
import com.gemiso.zodiac.app.dailyProgram.dto.DailyProgramDTO;
import com.gemiso.zodiac.app.program.ProgramService;
import com.gemiso.zodiac.app.program.dto.ProgramDTO;
import com.gemiso.zodiac.app.program.dto.ProgramSimpleDTO;
import com.gemiso.zodiac.core.enumeration.ActionEnum;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    private final CueSheetMapper cueSheetMapper;
    private final CueSheetCreateMapper cueSheetCreateMapper;
    private final CueSheetUpdateMapper cueSheetUpdateMapper;
    private final CueSheetOrderLockMapper cueSheetOrderLockMapper;
    private final CueSheetItemCreateMapper cueSheetItemCreateMapper;
    private final CueSheetItemMapper cueSheetItemMapper;
    private final CueSheetHistCreateMapper cueSheetHistCreateMapper;
    private final CueSheetListMapper cueSheetListMapper;

    private final ProgramService programService;
    private final UserAuthService userAuthService;

    private final DailyProgramService dailyProgramService;

    //큐시트 목록조회
    public CueSheetFindAllDTO findAll(Date sdate, Date edate, String brdcPgmId, String brdcPgmNm, String searchWord){

        BooleanBuilder booleanBuilder = getSearch( sdate,  edate,  brdcPgmId,  brdcPgmNm,  searchWord);

        //order by 정령조건 생성[ ASC 방송일시, DESC 방송시작시간]
        List<Sort.Order> orders = new ArrayList<>();
        Sort.Order order1 = new Sort.Order(Sort.Direction.DESC, "brdcDt");
        orders.add(order1);
        Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "brdcStartTime");
        orders.add(order2);

        List<CueSheet> cueSheets = (List<CueSheet>) cueSheetRepository.findAll(booleanBuilder, Sort.by(orders));

        List<CueSheetListDTO> cueSheetDTOList = cueSheetListMapper.toDtoList(cueSheets);

        //List<CueSheetDTO> newCueSheetDTOList = checkDelItem(cueSheetDTOList); //삭제된 기사아이템 제거.

        CueSheetFindAllDTO cueSheetFindAllDTO = unionPgm(cueSheetDTOList, sdate, edate, brdcPgmId, brdcPgmNm, searchWord); //큐시트 유니온 방송프로그램.

        return cueSheetFindAllDTO;


    }
    //큐시트 목록조회 - 큐시트 리스트 + 일일편성 리스트 유니온
    public CueSheetFindAllDTO unionPgm(List<CueSheetListDTO> cueSheetDTOList, Date sdate, Date edate, String brdcPgmId, String brdcPgmNm, String searchWord){

        //일일편성 목록조회[큐시트에 들어온 조회조건과 같이 조회한다.]
        List<DailyProgramDTO> dailyProgramList = dailyProgramService.findAll(sdate, edate, brdcPgmId, brdcPgmNm, null,null, null, searchWord);

        //조회된 큐시트 와 조회된 일일편성 유니온.
        for (CueSheetListDTO cueSheetDTO : cueSheetDTOList){

            //프로그램 get null포인트 에러 발생 방지 optional get
            Optional<ProgramSimpleDTO> programDTO = Optional.ofNullable(cueSheetDTO.getProgram());
            String cueBrdcPgmId = "";

            if (programDTO.isPresent()){ //프로그램이 있을경우 프로그램 아이디 get
                ProgramSimpleDTO program = programDTO.get();
                cueBrdcPgmId = program.getBrdcPgmId();
            }
            String cueBrdcDt = cueSheetDTO.getBrdcDt(); // 큐시트 방송일자 get
            String cueBrdcStartTime = cueSheetDTO.getBrdcStartTime(); //큐시트 방송시간 get

            //ConcurrentModificationException에러가 나서 이터레이터로 수정.
            Iterator<DailyProgramDTO> iter = dailyProgramList.listIterator();
            while(iter.hasNext()){
                DailyProgramDTO dailyProgramDTO = iter.next();

                ProgramDTO dailyProgram = dailyProgramDTO.getProgram(); //일일편성 프로그램get
                String dailyBrdcPgmId = "";

                if (ObjectUtils.isEmpty(dailyProgramDTO) == false){ //일일편성에 프로그램이 있을경우 프로그램 아이디 get
                    dailyBrdcPgmId = dailyProgram.getBrdcPgmId();
                }
                String dailyBrdcDt = dailyProgramDTO.getBrdcDt(); //일일편성 방송일자 get
                String dailyBrdcStartTime = dailyProgramDTO.getBrdcStartTime(); //일일편성 방송시작시간 get

                if (cueBrdcPgmId.trim().isEmpty() == false && dailyBrdcPgmId.trim().isEmpty() == false
                        && dailyBrdcPgmId.equals(cueBrdcPgmId)){ //큐시트&일일편성 방송프로그램 아이디가 있고 같으면

                    if (dailyBrdcDt.equals(cueBrdcDt) && dailyBrdcStartTime.equals(cueBrdcStartTime)){ //방송일자, 방송시작시간이 같으면 삭제.
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
    public CueSheetDTO find(Long cueId){

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
                /*articleCueItemDTO = symbolUrlSet(articleCueItemDTO);//기사에 방송아이콘의 방송아이콘URL 추가.
                cueSheetItemDTO.setArticle(articleCueItemDTO);//방송아이콘 추가한 기사 큐시트 아이템에 set*/
            }
            if ("Y".equals(delYn)) { //조회된 큐시트 아이템 삭제여부값이 Y인 경우/.
                continue;
            }

            newCueSheetItemDTOList.add(cueSheetItemDTO);
        }
        cueSheetDTO.setCueSheetItem(newCueSheetItemDTOList);

        return cueSheetDTO;

    }

    //큐시트 등록
    public Long create(CueSheetCreateDTO cueSheetCreateDTO){

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        cueSheetCreateDTO.setInputrId(userId);

        CueSheet cueSheet = cueSheetCreateMapper.toEntity(cueSheetCreateDTO);

        cueSheetRepository.save(cueSheet);

        Long cueId = cueSheet.getCueId();//생성된 큐시트 아이디.

        cueSheetHistCreate(cueId, cueSheet, userId);

        return cueId;
    }

    //큐시트 수정
    public void update(CueSheetUpdateDTO cueSheetUpdateDTO, Long cueId){

        //수정! 잠금여부? 잠금사용자, 잠금일시 도 수정할 때 정보를 넣어주나?

        CueSheet cueSheet = cueSheetFindOrFail(cueId);

        //cueSheet.setProgram(null);

        cueSheetUpdateMapper.updateFromDto(cueSheetUpdateDTO, cueSheet);

        cueSheetRepository.save(cueSheet);

        cueSheetHistUpdate(cueId, cueSheet);

        //수정! 버전정보 안들어가나요?

    }

    //큐시트 삭제
    public void delete(Long cueId){

        CueSheet cueSheet = cueSheetFindOrFail(cueId);

        CueSheetDTO cueSheetDTO = cueSheetMapper.toDto(cueSheet);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        cueSheetDTO.setDelrId(userId);
        cueSheetDTO.setDelDtm(new Date());
        cueSheetDTO.setDelYn("Y");

        cueSheetMapper.updateFromDto(cueSheetDTO, cueSheet);

        cueSheetRepository.save(cueSheet);

        cueSheetHistDelete( cueId, cueSheet);

    }

    //큐시트 이력 등록
    public void cueSheetHistCreate(Long cueId, CueSheet cueSheet, String userId){

        List<CueSheetItem> cueSheetItemList = cueSheet.getCueSheetItem(); //큐시트 아이템 엔티티 조회

        //조회된 큐시트 아이템 엔티티 DTO변환[ 엔티티를 바로 써버리면 필요없는 데이터가 다 보여지기 때문.]
        List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemMapper.toDtoList(cueSheetItemList);
        /*String cueSheetItem = String.valueOf(cueSheetItemList);*/

        //큐시트 아이템 리스트를 String 으로 변환 리스트 정보를 text로 저장하기 위함.
        String cueSheetItem = null;
        if (CollectionUtils.isEmpty(cueSheetItemDTOList) == false) {
            cueSheetItem = cueSheetItemDTOList.stream()
                    .map(n -> String.valueOf(n))
                    .collect(Collectors.joining());
        }

        //큐시트 이력에 넣어줄 큐시트 아이디 빌드
        CueSheetSimpleDTO cueSheetSimpleDTO = CueSheetSimpleDTO.builder().cueId(cueId).build();

        CueSheetHistCreateDTO cueSheetHistBuild = CueSheetHistCreateDTO.builder()
                .cueVer(0)
                .cueAction(ActionEnum.CREATE.getAction(ActionEnum.CREATE))
                .inputrId(userId)
                .cueSheet(cueSheetSimpleDTO)
                .cueItemData(cueSheetItem)
                .build();

        CueSheetHist cueSheetHist = cueSheetHistCreateMapper.toEntity(cueSheetHistBuild);

        cueSheetHistRepository.save(cueSheetHist);

    }

    //큐시트 이력 수정
    public void cueSheetHistUpdate(Long cueId, CueSheet cueSheet){

        List<CueSheetItem> cueSheetItemList = cueSheet.getCueSheetItem(); //큐시트 아이템 엔티티 조회

        //조회된 큐시트 아이템 엔티티 DTO변환[ 엔티티를 바로 써버리면 필요없는 데이터가 다 보여지기 때문.]
        List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemMapper.toDtoList(cueSheetItemList); 
        /*String cueSheetItem = String.valueOf(cueSheetItemList);*/

        //큐시트 아이템 리스트를 String 으로 변환 리스트 정보를 text로 저장하기 위함.
        String cueSheetItem = cueSheetItemDTOList.stream() 
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining());


        String userId = userAuthService.authUser.getUserId();//수정자 아이디 get
        //큐시트 이력에 넣어줄 큐시트 아이디 빌드
        CueSheetSimpleDTO cueSheetSimpleDTO = CueSheetSimpleDTO.builder().cueId(cueId).build();

        //큐시트아이템 이력 버전 카운트 조회
        int getCueVer = cueSheetHistRepository.findCueVer(cueId);

        //큐시트 아이템 이력 DTO 빌드
        CueSheetHistCreateDTO cueSheetHistBuild = CueSheetHistCreateDTO.builder()
                .cueVer(++getCueVer)
                .cueAction(ActionEnum.UPDATE.getAction(ActionEnum.UPDATE))
                .cueItemData(cueSheetItem)
                .inputrId(userId)
                .cueSheet(cueSheetSimpleDTO)
                .build();

        //빌드된 큐시트아이템 이력 DTO를 엔티티 변환
        CueSheetHist cueSheetHist = cueSheetHistCreateMapper.toEntity(cueSheetHistBuild);

        //큐시트 아이템 등록
        cueSheetHistRepository.save(cueSheetHist);

    }
    
    //큐시트 이력 삭제
    public void cueSheetHistDelete(Long cueId, CueSheet cueSheet){

        List<CueSheetItem> cueSheetItemList = cueSheet.getCueSheetItem(); //큐시트 아이템 엔티티 조회

        //조회된 큐시트 아이템 엔티티 DTO변환[ 엔티티를 바로 써버리면 필요없는 데이터가 다 보여지기 때문.]
        List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemMapper.toDtoList(cueSheetItemList);
        /*String cueSheetItem = String.valueOf(cueSheetItemList);*/

        //큐시트 아이템 리스트를 String 으로 변환 리스트 정보를 text로 저장하기 위함.
        String cueSheetItem = cueSheetItemDTOList.stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining());


        String userId = userAuthService.authUser.getUserId();//수정자 아이디 get
        //큐시트 이력에 넣어줄 큐시트 아이디 빌드
        CueSheetSimpleDTO cueSheetSimpleDTO = CueSheetSimpleDTO.builder().cueId(cueId).build();

        //큐시트아이템 이력 버전 카운트 조회
        int getCueVer = cueSheetHistRepository.findCueVer(cueId);

        //큐시트 아이템 이력 DTO 빌드
        CueSheetHistCreateDTO cueSheetHistBuild = CueSheetHistCreateDTO.builder()
                .cueVer(++getCueVer)
                .cueAction(ActionEnum.DELETE.getAction(ActionEnum.DELETE))
                .cueItemData(cueSheetItem)
                .inputrId(userId)
                .cueSheet(cueSheetSimpleDTO)
                .build();

        //빌드된 큐시트아이템 이력 DTO를 엔티티 변환
        CueSheetHist cueSheetHist = cueSheetHistCreateMapper.toEntity(cueSheetHistBuild);

        //큐시트 아이템 등록
        cueSheetHistRepository.save(cueSheetHist);

    }

    //큐시트 조회 및 존재여부 확인 [단건조회]
    public CueSheet cueSheetFindOrFail(Long cueId){

        Optional<CueSheet> cueSheet = cueSheetRepository.findByCue(cueId);

        if (!cueSheet.isPresent()){
            throw new ResourceNotFoundException("CueSheetId not found. cueSheetId : " + cueId);
        }

        return cueSheet.get();

    }

    //큐시트 목록조회 조회조건 빌드
    private BooleanBuilder getSearch(Date sdate, Date edate, String brdcPgmId, String brdcPgmNm, String searchWord) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QCueSheet qCueSheet = QCueSheet.cueSheet;

        booleanBuilder.and(qCueSheet.delYn.eq("N"));

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false){
            String StringSdate = dateToString(sdate);
            String stringEdate = dateToString(edate);
            booleanBuilder.and(qCueSheet.brdcDt.between(StringSdate, stringEdate));
        }
        if(brdcPgmId != null && brdcPgmId.trim().isEmpty() == false){
            booleanBuilder.and(qCueSheet.program.brdcPgmId.eq(brdcPgmId));
        }
        if(brdcPgmNm != null && brdcPgmNm.trim().isEmpty() == false){
            booleanBuilder.and(qCueSheet.brdcPgmNm.contains(brdcPgmNm));
        }
        if(searchWord != null && searchWord.trim().isEmpty() == false){
            booleanBuilder.and(qCueSheet.brdcPgmNm.contains(searchWord).or(qCueSheet.anc1Nm.contains(searchWord))
                    .or(qCueSheet.pd2Nm.contains(searchWord)));
        }

        return booleanBuilder;
    }

    //Date To String
    public String dateToString(Date date){

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String returnDate = dateFormat.format(date);

        return returnDate;
    }
    
    //큐시트 락
    public void cueSheetOrderLock(CueSheetOrderLockDTO cueSheetOrderLockDTO, Long cueId){

        CueSheet cueSheet = cueSheetFindOrFail(cueId);

        String lckYn = cueSheetOrderLockDTO.getLckYn();

        if (lckYn != null && lckYn.equals("Y")) { //락 여부값이 Y 가 아닐경우 lck값 초기화
            String userId = userAuthService.authUser.getUserId();
            cueSheetOrderLockDTO.setLckrId(userId);
            cueSheetOrderLockDTO.setLckDtm(new Date());
            cueSheetOrderLockDTO.setLckYn("Y");
            //cueSheetOrderLockDTO.setCueStCd("cuesheet_ready");
        } else {
            cueSheet.setLckrId(null);
            cueSheet.setLckDtm(null);
            cueSheet.setDelYn("N");
            cueSheet.setCueStCd(null);
            //cueSheetOrderLockDTO.setLckYn("N");
        }

        cueSheetOrderLockMapper.updateFromDto(cueSheetOrderLockDTO, cueSheet);

        cueSheetRepository.save(cueSheet);

    }

    //큐시트 잠금 해제
    public void cueSheetUnLock(Long cueId){

        CueSheet cueSheet = cueSheetFindOrFail(cueId);

        //큐시트 lock정보 초기화
        cueSheet.setLckrId(null);
        cueSheet.setLckDtm(null);
        cueSheet.setLckYn("N");
        cueSheet.setCueStCd(null);

        cueSheetRepository.save(cueSheet);

    }
    
    //큐시트 복사
    public Long copy(Long cueId, String brdcPgmId, String brdcDt){

        CueSheet getCueSheet = cueSheetFindOrFail(cueId);//원본 큐시트 get

        CueSheetCreateDTO cueSheetCreateDTO = cueSheetCreateMapper.toDto(getCueSheet);//원본 큐시트의 데이터를 createDTO에set

        String userId = userAuthService.authUser.getUserId(); // 로그인 Id로 입력자 set
        cueSheetCreateDTO.setInputrId(userId);
        cueSheetCreateDTO.setInputDtm(null); // 원본 큐시트 입력시간 초기화

        if (brdcDt != null && brdcDt.trim().isEmpty() == false) { //방송일자를 변경해 큐시트를 복사할 경우
            cueSheetCreateDTO.setBrdcDt(brdcDt); //방송일자 set
        }
        if (brdcPgmId != null && brdcPgmId.trim().isEmpty() == false){ //방송프로그램을 변경해 큐시트를 복사할 경우
            ProgramSimpleDTO programSimpleDTO = ProgramSimpleDTO.builder()
                    .brdcPgmId(brdcPgmId).build(); //프로그램 아이디 빌드
            cueSheetCreateDTO.setProgram(programSimpleDTO);//프로그램 set

            ProgramDTO programDTO = programService.find(brdcPgmId);//방송프로그램 명 바꿔주기 위해 프로그램 get

            cueSheetCreateDTO.setBrdcPgmNm(programDTO.getBrdcPgmNm());//방송프로그램이 바뀔시 큐시트에 입력된 방송프로그램 명도 바꿔준다.
        }

        CueSheet cueSheet = cueSheetCreateMapper.toEntity(cueSheetCreateDTO);
        cueSheetRepository.save(cueSheet);

        Long newCueId = cueSheet.getCueId(); //복사된 큐시트 아이디 get

        copyCueItem(cueId, newCueId);//복사된 큐시트의 아이템 리스트 복사


        return newCueId;

    }

    //큐시트 아이템 복사.
    public void copyCueItem( Long orgCueId, Long newCueId){ //복사된 큐시트의 아이템 리스트 복사

        List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findByCueItemList(orgCueId); //원본 큐시트에 입력된 아이템 get

        List<CueSheetItemCreateDTO> cueSheetCreateDTOS = cueSheetItemCreateMapper.toDtoList(cueSheetItemList); //큐시트아이템 리스트 get


        for (CueSheetItemCreateDTO cueItemDTO : cueSheetCreateDTOS){
            CueSheetSimpleDTO cueSheetDTO = CueSheetSimpleDTO.builder().cueId(newCueId).build();
            cueItemDTO.setCueSheet(cueSheetDTO); //복사된 큐시트 아이디 set
            CueSheetItem cueSheetItem = cueSheetItemCreateMapper.toEntity(cueItemDTO);
            cueSheetItemRepository.save(cueSheetItem);
        }

    }

    //큐시트 목록조회 + 유니온 일일편성 [큐시트 인터페이스+큐시트 아이템추가 목록]
    public CueSheetInterfaceDTO findAllInterface(Date sdate, Date edate, String brdcPgmId, String brdcPgmNm, String searchWord){

        BooleanBuilder booleanBuilder = getSearch( sdate,  edate,  brdcPgmId,  brdcPgmNm,  searchWord);

        //order by 정령조건 생성[ ASC 방송일시, DESC 방송시작시간]
        List<Sort.Order> orders = new ArrayList<>();
        Sort.Order order1 = new Sort.Order(Sort.Direction.DESC, "brdcDt");
        orders.add(order1);
        Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "brdcStartTime");
        orders.add(order2);

        List<CueSheet> cueSheets = (List<CueSheet>) cueSheetRepository.findAll(booleanBuilder, Sort.by(orders));

        List<CueSheetDTO> cueSheetDTOList = cueSheetMapper.toDtoList(cueSheets);

        List<CueSheetDTO> newCueSheetDTOList = checkDelItem(cueSheetDTOList); //삭제된 기사아이템 제거.

        CueSheetInterfaceDTO cueSheetFindAllDTO = unionPgmInterface(newCueSheetDTOList, sdate, edate, brdcPgmId, brdcPgmNm, searchWord); //큐시트 유니온 방송프로그램.

        return cueSheetFindAllDTO;


    }

    //삭제된 기사아이템 제거.[큐시트 인터페이스+큐시트 아이템추가 목록]
    public List<CueSheetDTO> checkDelItem(List<CueSheetDTO> cueSheetDTOList){

        List<CueSheetDTO> newCueSheetDTOList = new ArrayList<>();

        //삭제된 큐시트 아이템 제거.
        for (CueSheetDTO cueSheetDTO : cueSheetDTOList){
            List<CueSheetItemDTO> orgCueSheetItemDTOList = cueSheetDTO.getCueSheetItem();
            List<CueSheetItemDTO> newCueSheetItemDTOList = new ArrayList<>();
            for (Iterator<CueSheetItemDTO> itr = orgCueSheetItemDTOList.iterator(); itr.hasNext();){

                CueSheetItemDTO cueSheetItemDTO = itr.next();

                String delYn = cueSheetItemDTO.getDelYn();//큐시트 아이템의 삭제여부 값
                ArticleCueItemDTO articleCueItemDTO = cueSheetItemDTO.getArticle();//큐시트 아이템의 기사의 삭제여부 값

                if (ObjectUtils.isEmpty(articleCueItemDTO) == false){ //큐시트 아이템에 기사가 포함된 경우.
                    String articleDelYn = articleCueItemDTO.getDelYn();//큐시트 아이템에 포함된 기사의 삭제 여부값
                    if ("Y".equals(articleDelYn)){ //기사 삭제 값이 Y인경우 조회된 큐시트아이템 삭제
                        continue;
                    }
                }
                if ("Y".equals(delYn)){ //조회된 큐시트 아이템 삭제여부값이 Y인 경우/.
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
    public CueSheetInterfaceDTO unionPgmInterface(List<CueSheetDTO> cueSheetDTOList, Date sdate, Date edate, String brdcPgmId, String brdcPgmNm, String searchWord) {

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
        CueSheetInterfaceDTO cueSheetInterfaceDTO = new CueSheetInterfaceDTO();
        cueSheetInterfaceDTO.setCueSheetDTO(cueSheetDTOList);
        cueSheetInterfaceDTO.setDailyProgramDTO(dailyProgramList);

        return cueSheetInterfaceDTO;
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