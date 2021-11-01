package com.gemiso.zodiac.app.cueSheet;

import com.gemiso.zodiac.app.cueSheet.dto.*;
import com.gemiso.zodiac.app.cueSheet.mapper.CueSheetCreateMapper;
import com.gemiso.zodiac.app.cueSheet.mapper.CueSheetMapper;
import com.gemiso.zodiac.app.cueSheet.mapper.CueSheetOrderLockMapper;
import com.gemiso.zodiac.app.cueSheet.mapper.CueSheetUpdateMapper;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItemRepository;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemCreateDTO;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemCreateMapper;
import com.gemiso.zodiac.app.program.ProgramService;
import com.gemiso.zodiac.app.program.dto.ProgramDTO;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CueSheetService {

    private final CueSheetRepository cueSheetRepository;
    private final CueSheetItemRepository cueSheetItemRepository;

    private final CueSheetMapper cueSheetMapper;
    private final CueSheetCreateMapper cueSheetCreateMapper;
    private final CueSheetUpdateMapper cueSheetUpdateMapper;
    private final CueSheetOrderLockMapper cueSheetOrderLockMapper;
    private final CueSheetItemCreateMapper cueSheetItemCreateMapper;

    private final UserAuthService userAuthService;

    private final ProgramService programService;

    public CueSheetFindAllDTO findAll(Date sdate, Date edate, Long brdcPgmId, String brdcPgmNm, String searchWord){

        BooleanBuilder booleanBuilder = getSearch( sdate,  edate,  brdcPgmId,  brdcPgmNm,  searchWord);

        List<CueSheet> cueSheets = (List<CueSheet>) cueSheetRepository.findAll(booleanBuilder);

        List<CueSheetDTO> cueSheetDTOList = cueSheetMapper.toDtoList(cueSheets);

        CueSheetFindAllDTO cueSheetFindAllDTO = unionPgm(cueSheetDTOList); //큐시트 유니온 방송프로그램.

        return cueSheetFindAllDTO;


    }

    public CueSheetFindAllDTO unionPgm(List<CueSheetDTO> cueSheetDTOList){
        //방송프로그램 전체조회.
        List<ProgramDTO> programDTOList = programService.findAll("");

        //조회된 큐시트에서 사용(중복)된 프로그램을 삭제한다.
        for (CueSheetDTO cueSheet : cueSheetDTOList){

            Optional<ProgramDTO> getProgramDTO =  Optional.ofNullable(cueSheet.getProgram());
            //Optional<Long> getBrdcPgmId = Optional.ofNullable(cueSheet.getProgram().getBrdcPgmId());

            if (getProgramDTO.isPresent()) {
                ProgramDTO ProgramDTO = getProgramDTO.get();
                Long getBrdcPgmId = ProgramDTO.getBrdcPgmId();

                //ConcurrentModificationException에러가 나서 이터레이터로 수정.
                Iterator<ProgramDTO> iter = programDTOList.listIterator();
                while (iter.hasNext()) {
                    ProgramDTO programDTO = iter.next();
                    Long orgBrdcPgmId = programDTO.getBrdcPgmId();
                    if (getBrdcPgmId.equals(orgBrdcPgmId)) {
                        iter.remove();
                    }

                }
            }
        }
        //조회된 큐시트 + 방송프로그램
        CueSheetFindAllDTO cueSheetFindAllDTO = new CueSheetFindAllDTO();
        cueSheetFindAllDTO.setCueSheetDTO(cueSheetDTOList);
        cueSheetFindAllDTO.setProgramDTO(programDTOList);

        return cueSheetFindAllDTO;
    }
    public CueSheetDTO find(Long cueId){

        CueSheet cueSheet = cueSheetFindOrFail(cueId);

        CueSheetDTO cueSheetDTO = cueSheetMapper.toDto(cueSheet);

        return cueSheetDTO;

    }

    public Long create(CueSheetCreateDTO cueSheetCreateDTO){

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        cueSheetCreateDTO.setInputrId(userId);

        CueSheet cueSheet = cueSheetCreateMapper.toEntity(cueSheetCreateDTO);

        cueSheetRepository.save(cueSheet);

        return cueSheet.getCueId();
    }

    public void update(CueSheetUpdateDTO cueSheetUpdateDTO, Long cueId){

        //수정! 잠금여부? 잠금사용자, 잠금일시 도 수정할 때 정보를 넣어주나?

        CueSheet cueSheet = cueSheetFindOrFail(cueId);

        //cueSheet.setProgram(null);

        cueSheetUpdateDTO.setCueId(cueId);

        cueSheetUpdateMapper.updateFromDto(cueSheetUpdateDTO, cueSheet);

        cueSheetRepository.save(cueSheet);


        //수정! 버전정보 안들어가나요?

    }

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

    }

    public CueSheet cueSheetFindOrFail(Long cueId){

        Optional<CueSheet> cueSheet = cueSheetRepository.findByCue(cueId);

        if (!cueSheet.isPresent()){
            throw new ResourceNotFoundException("CueSheetId not found. cueSheetId : " + cueId);
        }

        return cueSheet.get();

    }

    private BooleanBuilder getSearch(Date sdate, Date edate, Long brdcPgmId, String brdcPgmNm, String searchWord) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QCueSheet qCueSheet = QCueSheet.cueSheet;

        booleanBuilder.and(qCueSheet.delYn.eq("N"));

        if (!StringUtils.isEmpty(sdate) && !StringUtils.isEmpty(edate)){
            booleanBuilder.and(qCueSheet.inputDtm.between(sdate, edate));
        }
        if(!StringUtils.isEmpty(brdcPgmId)){
            booleanBuilder.and(qCueSheet.program.brdcPgmId.eq(brdcPgmId));
        }
        if(!StringUtils.isEmpty(brdcPgmNm)){
            booleanBuilder.and(qCueSheet.brdcPgmNm.contains(brdcPgmNm));
        }
        if(!StringUtils.isEmpty(searchWord)){
            booleanBuilder.and(qCueSheet.brdcPgmNm.contains(searchWord).or(qCueSheet.anc1Nm.contains(searchWord))
                    .or(qCueSheet.pd2Nm.contains(searchWord)));
        }

        return booleanBuilder;
    }

    public void cueSheetOrderLock(CueSheetOrderLockDTO cueSheetOrderLockDTO, Long cueId){

        CueSheet cueSheet = cueSheetFindOrFail(cueId);

        String lckYn = cueSheetOrderLockDTO.getLckYn();

        if (lckYn != null && lckYn.equals("Y")) { //락 여부값이 Y 가 아닐경우 lck값 초기화
            String userId = userAuthService.authUser.getUserId();
            cueSheetOrderLockDTO.setLckrId(userId);
            cueSheetOrderLockDTO.setLckDtm(new Date());
            cueSheetOrderLockDTO.setLckYn("Y");
        } else {
            cueSheet.setLckrId(null);
            cueSheet.setLckDtm(null);
            //cueSheetOrderLockDTO.setLckYn("N");
        }

        cueSheetOrderLockMapper.updateFromDto(cueSheetOrderLockDTO, cueSheet);

        cueSheetRepository.save(cueSheet);

    }

    public void cueSheetUnLock(Long cueId){

        CueSheet cueSheet = cueSheetFindOrFail(cueId);

        //큐시트 lock정보 초기화
        cueSheet.setLckrId(null);
        cueSheet.setLckDtm(null);
        cueSheet.setLckYn("N");

        cueSheetRepository.save(cueSheet);

    }

    public Long copy(Long cueId){

        CueSheet getCueSheet = cueSheetFindOrFail(cueId);//원본 큐시트 get

        CueSheetCreateDTO cueSheetCreateDTO = cueSheetCreateMapper.toDto(getCueSheet);//원본 큐시트의 데이터를 createDTO에set

        String userId = userAuthService.authUser.getUserId(); // 로그인 Id로 입력자 set
        cueSheetCreateDTO.setInputrId(userId);
        cueSheetCreateDTO.setInputDtm(null); // 원본 큐시트 입력시간 초기화

        CueSheet cueSheet = cueSheetCreateMapper.toEntity(cueSheetCreateDTO);
        cueSheetRepository.save(cueSheet);

        Long newCueId = cueSheet.getCueId(); //복사된 큐시트 아이디 get

        copyCueItem(cueId, newCueId);//복사된 큐시트의 아이템 리스트 복사


        return newCueId;

    }
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
}
