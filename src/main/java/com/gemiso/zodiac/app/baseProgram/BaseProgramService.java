package com.gemiso.zodiac.app.baseProgram;

import com.gemiso.zodiac.app.baseProgram.dto.BaseProgramCreateDTO;
import com.gemiso.zodiac.app.baseProgram.dto.BaseProgramDTO;
import com.gemiso.zodiac.app.baseProgram.dto.BaseProgramSimpleDTO;
import com.gemiso.zodiac.app.baseProgram.dto.BaseProgramUpdateDTO;
import com.gemiso.zodiac.app.baseProgram.mapper.BaseProgramCreateMapper;
import com.gemiso.zodiac.app.baseProgram.mapper.BaseProgramMapper;
import com.gemiso.zodiac.app.baseProgram.mapper.BaseProgramUpdateMapper;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
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
public class BaseProgramService {

    private final BaseProgramRepository baseProgramRepository;

    private final BaseProgramMapper baseProgramMapper;
    private final BaseProgramCreateMapper baseProgramCreateMapper;
    private final BaseProgramUpdateMapper baseProgramUpdateMapper;

    private final UserAuthService userAuthService;

    //기본편성 목록조회
    public List<BaseProgramDTO> findAll(Long basPgmschId, String brdcStartDt,
                                        String brdcEndDt, String brdcStartClk, String brdcPgmId){

        BooleanBuilder booleanBuilder = getSearch(basPgmschId, brdcStartDt, brdcEndDt, brdcStartClk, brdcPgmId);

        List<BaseProgram> baseProgramList =
                (List<BaseProgram>) baseProgramRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "brdcStartDt"));

        List<BaseProgramDTO> baseProgramDTOList = baseProgramMapper.toDtoList(baseProgramList);

        return baseProgramDTOList;
    }

    //기본편성 상세조회
    public BaseProgramDTO find(Long basePgmschId){

        BaseProgram baseProgram = findBasepgm(basePgmschId);//기본편성 조회 및 존재유무 확인.

        BaseProgramDTO baseProgramDTO = baseProgramMapper.toDto(baseProgram);//엔티티 DTO 변환

        return baseProgramDTO; // 리턴
    }

    //기본편성 등록
    public BaseProgramSimpleDTO create(BaseProgramCreateDTO baseProgramCreateDTO){

        String userId = userAuthService.authUser.getUserId();//로그인 토큰에서 사용자 정보를 가져온다.

        baseProgramCreateDTO.setInputrId(userId);//입력자 set

        BaseProgram baseProgram = baseProgramCreateMapper.toEntity(baseProgramCreateDTO);//DTO 엔티티 변환

        baseProgramRepository.save(baseProgram);//등록

        //기본편성 아이디를 불러온다
        Long basePgmschId = baseProgram.getBasePgmschId();
        //기본편성 아이디를 담아 리턴할 DTO생성
        BaseProgramSimpleDTO baseProgramSimpleDTO = new BaseProgramSimpleDTO();
        //리턴할 DTO에 아이디 set
        baseProgramSimpleDTO.setBasePgmschId(basePgmschId);

        return baseProgramSimpleDTO;
    }

    //기본편성 업데이트
    public BaseProgramSimpleDTO update(BaseProgramUpdateDTO baseProgramUpdateDTO, Long basePgmschId){

        BaseProgram baseProgram = findBasepgm(basePgmschId);//기본편성 조회 및 존재유무 확인.

        String userId = userAuthService.authUser.getUserId();//로그인 토큰에서 사용자 정보를 가져온다.
        baseProgramUpdateDTO.setUpdtrId(userId); //수정자 아이디 등록

        baseProgramUpdateMapper.updateFromDto(baseProgramUpdateDTO, baseProgram);//업데이트 정보 set

        baseProgramRepository.save(baseProgram); // 수정

        //기본편성 아이디를 담아 리턴할 DTO생성
        BaseProgramSimpleDTO baseProgramSimpleDTO = new BaseProgramSimpleDTO();
        //리턴할 DTO에 아이디 set
        baseProgramSimpleDTO.setBasePgmschId(basePgmschId);

        return baseProgramSimpleDTO;
    }

    //기본편성 삭제
    public void delete(Long basePgmschId){

        BaseProgram baseProgram = findBasepgm(basePgmschId);//기본편성 조회 및 존재유무 확인.

        BaseProgramDTO baseProgramDTO = baseProgramMapper.toDto(baseProgram); // 엔티티 DTO변환

        String userId = userAuthService.authUser.getUserId();//로그인 토큰에서 사용자 정보를 가져온다.
        baseProgramDTO.setDelrId(userId); //삭제자 등록
        baseProgramDTO.setDelDtm(new Date()); //삭제일시 등록
        baseProgramDTO.setDelYn("Y"); //여제여부값 Y

        baseProgramMapper.updateFromDto(baseProgramDTO, baseProgram); //삭제값 업데이트

        baseProgramRepository.save(baseProgram); //삭제 업데이트
    }

    //기본편성 조회 및 존재유무 확인.
    public BaseProgram findBasepgm(Long basePgmschId){

        Optional<BaseProgram> baseProgram = baseProgramRepository.findBasePgm(basePgmschId);

        if (baseProgram.isPresent() == false){
            throw new ResourceNotFoundException("기본편성을 찾을 수 없습니다. 기본편성 아이디 : "+basePgmschId);
        }

        return baseProgram.get();
    }

    //기본편성 목록조회 조회조건 빌드
    public BooleanBuilder getSearch(Long basPgmschId, String brdcStartDt,
                                    String brdcEndDt, String brdcStartClk, String brdcPgmId){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QBaseProgram qBaseProgram = QBaseProgram.baseProgram;

        //기본편성 아이디로 조회
        if (ObjectUtils.isEmpty(basPgmschId) == false){
            booleanBuilder.and(qBaseProgram.basePgmschId.eq(basPgmschId));
        }

        // 방송시작일자로 검색
        if (brdcStartDt != null && brdcStartDt.trim().isEmpty() == false){
            booleanBuilder.and(qBaseProgram.brdcStartDt.eq(brdcStartDt));
        }

        //방송종료일자로 검색
        if (brdcEndDt != null && brdcEndDt.trim().isEmpty() == false){
            booleanBuilder.and(qBaseProgram.brdcEndDt.eq(brdcEndDt));
        }

        //방송시각으로 검색
        if (brdcStartClk != null && brdcStartClk.trim().isEmpty() == false){
            booleanBuilder.and(qBaseProgram.brdcStartClk.eq(brdcStartClk));
        }

        //방송프로그램 아이디로 검색
        if (brdcPgmId != null && brdcPgmId.trim().isEmpty() == false){
            booleanBuilder.and(qBaseProgram.program.brdcPgmId.eq(brdcPgmId));
        }

        return booleanBuilder;

    }
}
