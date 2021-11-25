package com.gemiso.zodiac.app.breakingNews;

import com.gemiso.zodiac.app.breakingNews.dto.BreakingNewsCreateDTO;
import com.gemiso.zodiac.app.breakingNews.dto.BreakingNewsDTO;
import com.gemiso.zodiac.app.breakingNews.dto.BreakingNewsSimplerDTO;
import com.gemiso.zodiac.app.breakingNews.dto.BreakingNewsUpdateDTO;
import com.gemiso.zodiac.app.breakingNews.mapper.BreakingNewsCreateMapper;
import com.gemiso.zodiac.app.breakingNews.mapper.BreakingNewsMapper;
import com.gemiso.zodiac.app.breakingNews.mapper.BreakingNewsUpdateMapper;
import com.gemiso.zodiac.app.breakingNewsDetail.BreakingNewsDtl;
import com.gemiso.zodiac.app.breakingNewsDetail.BreakingNewsDtlRepository;
import com.gemiso.zodiac.app.breakingNewsDetail.dto.BreakingNewsDtlCreateDTO;
import com.gemiso.zodiac.app.breakingNewsDetail.mapper.BreakingNewsDtlCreateMapper;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BreakingNewsService {

    private final BreakingNewsRepository breakingNewsRepository;
    private final BreakingNewsDtlRepository breakingNewsDtlRepository;

    private final BreakingNewsMapper breakingNewsMapper;
    private final BreakingNewsCreateMapper breakingNewsCreateMapper;
    private final BreakingNewsUpdateMapper breakingNewsUpdateMapper;
    private final BreakingNewsDtlCreateMapper breakingNewsDtlCreateMapper;

    private final UserAuthService userAuthService;


    //속보뉴스 목록조회
    public List<BreakingNewsDTO> findAll(Date sdate, Date edate, String delYn){

        //속보뉴스 목록조회 조회조건 빌드
        BooleanBuilder booleanBuilder = getSearch(sdate, edate, delYn);

        //생성된 조회조건으로 리스트 조회
        List<BreakingNews> breakingNewsList =
                (List<BreakingNews>) breakingNewsRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "inputDtm"));

        //DTO리스트 변환
        List<BreakingNewsDTO> breakingNewsDTOList = breakingNewsMapper.toDtoList(breakingNewsList);

        //DTO리스트 리턴
        return breakingNewsDTOList;
    }

    //속보뉴스 상세조회
    public BreakingNewsDTO find(Long breakingNewsId){

        BreakingNews breakingNews = findBreakingNews(breakingNewsId);//속보뉴스 조회 및 존재유무 확인 [조회조건 속보뉴스 아이디]

        BreakingNewsDTO breakingNewsDTO = breakingNewsMapper.toDto(breakingNews);//조회된 속보뉴스 DTO변환

        return breakingNewsDTO;//속보뉴스 DTO 리턴
    }
    
    //속보뉴스 등록
    public Long create(BreakingNewsCreateDTO breakingNewsCreateDTO){
        
        String userId = userAuthService.authUser.getUserId();//토큰에서 사용자 아이디 get
        breakingNewsCreateDTO.setInputrId(userId); //등록자 set
        
        //등록DTO 엔티티 변환
        BreakingNews breakingNews = breakingNewsCreateMapper.toEntity(breakingNewsCreateDTO);
        
        //등록
        breakingNewsRepository.save(breakingNews);

        Long breakingNewsId = breakingNews.getBreakingNewsId(); //등록하고 만들어진 속보뉴스 아이디 get

        //속보뉴스 등록으로 들어온 속보뉴스 상세 리스트 get
        List<BreakingNewsDtlCreateDTO> breakingNewsDtlCreateDTOList = breakingNewsCreateDTO.getBreakingNewsDtls();

        //속보뉴스 상세 등록
        createDetail(breakingNewsDtlCreateDTOList, breakingNewsId);

        return breakingNewsId; //속보뉴스 아이디 리턴
    }

    //속보뉴스 수정
    public void update(BreakingNewsUpdateDTO breakingNewsUpdateDTO, Long breakingNewsId){

        BreakingNews breakingNews = findBreakingNews(breakingNewsId);//속보뉴스 조회 및 존재유무 확인 [조회조건 속보뉴스 아이디]

        String userId = userAuthService.authUser.getUserId();//토큰에서 사용자 아이디 get
        breakingNewsUpdateDTO.setUpdtrId(userId);//수정자 set

        breakingNewsUpdateMapper.updateFromDto(breakingNewsUpdateDTO, breakingNews);//기존정보에 업데이트 정보 set
        breakingNewsRepository.save(breakingNews);//수정

        //속보뉴스 수정으로 들어온 속보뉴스 상세 리스트 get
        List<BreakingNewsDtlCreateDTO> breakingNewsDtlCreateDTOList = breakingNewsUpdateDTO.getBreakingNewsDtls();
        //속보뉴스 상세 수정[기존등록된 속보뉴스 상세 삭제후 재등록]
        updateDetail(breakingNewsDtlCreateDTOList, breakingNewsId);
    }

    //속보뉴스 삭제.
    public void delete(Long breakingNewsId){

        BreakingNews breakingNews = findBreakingNews(breakingNewsId);//속보뉴스 조회 및 존재유무 확인 [조회조건 속보뉴스 아이디]

        BreakingNewsDTO breakingNewsDTO = breakingNewsMapper.toDto(breakingNews);
        String userId = userAuthService.authUser.getUserId();//토큰에서 사용자 아이디 get
        breakingNewsDTO.setDelrId(userId);//삭제자 set
        breakingNewsDTO.setDelDtm(new Date()); //삭제일시 set
        breakingNewsDTO.setDelYn("Y");//삭제여부값 "Y" set

        breakingNewsMapper.updateFromDto(breakingNewsDTO, breakingNews);//조회된 기존정보에 수정된 삭제정보 업데이트
        
        breakingNewsRepository.save(breakingNews); //삭제정보 저장
        
    }

    //속보뉴스 상세 수정
    public void updateDetail(List<BreakingNewsDtlCreateDTO> breakingNewsDtlCreateDTOList, Long breakingNewsId){

        //기존에 등록되어 있던 속보뉴스 상세 리스트를 불러온다.
        List<BreakingNewsDtl> breakingNewsDtls = breakingNewsDtlRepository.findDetailList(breakingNewsId);

        //조회된 속보뉴스 상세 리스트를 삭제
        for (BreakingNewsDtl detailDTO : breakingNewsDtls){

            Long id = detailDTO.getId();

            breakingNewsDtlRepository.deleteById(id);
        }

        //속보뉴스 상세 재등록
        createDetail(breakingNewsDtlCreateDTOList, breakingNewsId);
    }

    //속보뉴스 상세 리스트 등록
    public void createDetail(List<BreakingNewsDtlCreateDTO> breakingNewsDtlCreateDTOList, Long breakingNewsId){

        //속보뉴스 상세에 등록할 속보뉴스 아이디 빌드
        BreakingNewsSimplerDTO breakingNewsSimplerDTO = BreakingNewsSimplerDTO.builder().breakingNewsId(breakingNewsId).build();

        for (BreakingNewsDtlCreateDTO dtlDTO :  breakingNewsDtlCreateDTOList){

            dtlDTO.setBreakingNews(breakingNewsSimplerDTO); //속보뉴스상세에 속보뉴스 아이디 set
            
            BreakingNewsDtl breakingNewsDtl = breakingNewsDtlCreateMapper.toEntity(dtlDTO);//속보뉴스 상세 등록DTO 엔티티 변환
        
            breakingNewsDtlRepository.save(breakingNewsDtl); //속보뉴스 상세 등록
        }
    }

    //속보뉴스 조회 및 존재유무 확인 [조회조건 속보뉴스 아이디]
    public BreakingNews findBreakingNews(Long breakingNewsId){

        //null방지를 위해 옵셔널 사용 조회
        Optional<BreakingNews> breakingNews = breakingNewsRepository.findBreakingNews(breakingNewsId);

        //조회된 속보뉴스가 없으면 에러
        if (breakingNews.isPresent() == false){
            throw new ResourceNotFoundException("속보뉴스를 찾을 수 없습니다. 속보뉴스 아이디 : "+breakingNewsId);
        }
        //조회된 속보뉴스 리턴
        return breakingNews.get();
    }
    
    //속보뉴스 목록조회 조회조건 빌드
    public BooleanBuilder getSearch(Date sdate, Date edate, String delYn){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QBreakingNews qBreakingNews = QBreakingNews.breakingNews;

        //조회조건이 날짜로 들어온 경우
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false){
            booleanBuilder.and(qBreakingNews.inputDtm.between(sdate, edate));
        }

        //조회조건이 삭제 여부값으로 들어온 경우
        if (delYn != null && delYn.trim().isEmpty() ==false){
            booleanBuilder.and(qBreakingNews.delYn.eq(delYn));
        }
        else { //삭제 여부값이 안들어왔을 경우 디폴트 N
            booleanBuilder.and(qBreakingNews.delYn.eq("N"));
        }

        return booleanBuilder;
    }
}
