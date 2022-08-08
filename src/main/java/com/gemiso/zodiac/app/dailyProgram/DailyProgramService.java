package com.gemiso.zodiac.app.dailyProgram;

import com.gemiso.zodiac.app.dailyProgram.dto.DailyProgramCreateDTO;
import com.gemiso.zodiac.app.dailyProgram.dto.DailyProgramDTO;
import com.gemiso.zodiac.app.dailyProgram.mapper.DailyProgramCreateMapper;
import com.gemiso.zodiac.app.dailyProgram.mapper.DailyProgramMapper;
import com.gemiso.zodiac.core.helper.DateChangeHelper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DailyProgramService {

    private final DailyProgramRepository dailyProgramRepository;

    private final DailyProgramMapper dailyProgramMapper;
    private final DailyProgramCreateMapper dailyProgramCreateMapper;

    //private final UserAuthService userAuthService;

    private final DateChangeHelper dateChangeHelper;


    public List<DailyProgramDTO> findAll(Date sdate, Date edate, String brdcPgmId, String brdcPgmNm, String brdcDivCd, Long stdioId,
                                         Long subrmId, String searchWord){

       /* BooleanBuilder booleanBuilder = getSearch(sdate, edate, brdcPgmId, brdcPgmNm, brdcDivCd, stdioId, subrmId, searchWord);

        //order by 정령조건 생성[ ASC 방송일시, DESC 방송시작시간]
        List<Sort.Order> orders = new ArrayList<>();
        Sort.Order order1 = new Sort.Order(Sort.Direction.DESC, "brdcDt");
        orders.add(order1);
        Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "brdcStartTime");
        orders.add(order2);

        List<DailyProgram> dailyProgramList = (List<DailyProgram>) dailyProgramRepository.findAll(
                booleanBuilder, Sort.by(orders));*/

        List<DailyProgram> dailyProgramList = (List<DailyProgram>) dailyProgramRepository.findByDailyProgramList(
                sdate, edate, brdcPgmId, brdcPgmNm, brdcDivCd, stdioId, subrmId, searchWord);

        List<DailyProgramDTO> dailyProgramDTOList = dailyProgramMapper.toDtoList(dailyProgramList);

        return dailyProgramDTOList;
    }

    //일일편성 상세조회
    public DailyProgramDTO find(Long id){

        DailyProgram dailyProgram = dailyProgramFindOrFail(id); //일일편성 조횐 및 존재여부 확인

        DailyProgramDTO dailyProgramDTO = dailyProgramMapper.toDto(dailyProgram);

        return dailyProgramDTO;
    }
    //일일편성 등록
    public Long create(DailyProgramCreateDTO dailyProgramCreateDTO, String userId){

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        //String userId = userAuthService.authUser.getUserId();
        dailyProgramCreateDTO.setInputrId(userId);

        DailyProgram dailyProgram = dailyProgramCreateMapper.toEntity(dailyProgramCreateDTO);

        dailyProgramRepository.save(dailyProgram);

        return dailyProgram.getDailyPgmId();

    }

    //일일편성 조횐 및 존재여부 확인
    public DailyProgram dailyProgramFindOrFail(Long id){

        Optional<DailyProgram> dailyProgram = dailyProgramRepository.findById(id);

        if (dailyProgram.isPresent() == false){
            throw new ResourceNotFoundException("일일편성을 찾을 수 없습니다. 일일편성 아이디 : "+id);
        }

        return dailyProgram.get();

    }

    // 일일편성 목록조회 조회조건 빌드
    public BooleanBuilder getSearch(Date sdate, Date edate, String brdcPgmId, String brdcPgmNm, String brdcDivCd,
                                    Long stdioId, Long subrmId, String searchWord){

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QDailyProgram qDailyProgram = QDailyProgram.dailyProgram;

        //날짜조회시 방송일자 조회
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false){
            String StringSdate = dateChangeHelper.dateToStringNoTime(sdate); //Date To String( yyyy-MM-dd )
            String stringEdate = dateChangeHelper.dateToStringNoTime(edate); //Date To String( yyyy-MM-dd )
            booleanBuilder.and(qDailyProgram.brdcDt.between(StringSdate, stringEdate));
        }
        //조회조건이 방송구분 코드로 들어온 경우
        if (brdcDivCd != null && brdcDivCd.trim().isEmpty() == false){
            booleanBuilder.and(qDailyProgram.brdcDivCd.eq(brdcDivCd));
        }
        if(brdcPgmId != null && brdcPgmId.trim().isEmpty() == false){
            booleanBuilder.and(qDailyProgram.program.brdcPgmId.eq(brdcPgmId));
        }
        if(brdcPgmNm != null && brdcPgmNm.trim().isEmpty() == false){
            booleanBuilder.and(qDailyProgram.brdcPgmNm.contains(brdcPgmNm));
        }
        //스튜디어 아이디가 조회조건으로 들어온경우
        if (ObjectUtils.isEmpty(stdioId) == false){
            booleanBuilder.and(qDailyProgram.stdioId.eq(stdioId));
        }
        //부조 아이디가 조회조건으로 들어온 경우
        if (ObjectUtils.isEmpty(subrmId) == false){
            booleanBuilder.and(qDailyProgram.subrmId.eq(subrmId));
        }
        //방송프로그램명이 조회조건으로 들어온 경우
        if (searchWord != null && searchWord.trim().isEmpty() == false){
            booleanBuilder.and(qDailyProgram.brdcPgmNm.contains(searchWord));
        }

        return booleanBuilder;
    }
}
