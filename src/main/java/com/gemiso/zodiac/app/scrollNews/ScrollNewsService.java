package com.gemiso.zodiac.app.scrollNews;

import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsCreateDTO;
import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsDTO;
import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsSimpleDTO;
import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsUpdateDTO;
import com.gemiso.zodiac.app.scrollNews.mapper.ScrollNewsCreateMapper;
import com.gemiso.zodiac.app.scrollNews.mapper.ScrollNewsMapper;
import com.gemiso.zodiac.app.scrollNews.mapper.ScrollNewsUpdateMapper;
import com.gemiso.zodiac.app.scrollNewsDetail.ScrollNewsDetail;
import com.gemiso.zodiac.app.scrollNewsDetail.ScrollNewsDetailRepository;
import com.gemiso.zodiac.app.scrollNewsDetail.dto.ScrollNewsDetailCreateDTO;
import com.gemiso.zodiac.app.scrollNewsDetail.dto.ScrollNewsDetailCttJsonDTO;
import com.gemiso.zodiac.app.scrollNewsDetail.mapper.ScrollNewsDetailCreateMapper;
import com.gemiso.zodiac.core.helper.MarshallingJsonHelper;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ScrollNewsService {

    private final ScrollNewsRepository scrollNewsRepository;
    private final ScrollNewsDetailRepository scrollNewsDetailRepository;

    private final ScrollNewsMapper scrollNewsMapper;
    private final ScrollNewsCreateMapper scrollNewsCreateMapper;
    private final ScrollNewsUpdateMapper scrollNewsUpdateMapper;
    private final ScrollNewsDetailCreateMapper scrollNewsDetailCreateMapper;
    
    private final UserAuthService userAuthService;

    private final MarshallingJsonHelper marshallingJsonHelper;

    //스크롤 뉴스 목록조회
    public List<ScrollNewsDTO> findAll(Date sdate, Date edate, String delYn){

        //스크롤 뉴스 조회조건 빌드
        BooleanBuilder booleanBuilder = getSearch(sdate, edate, delYn);

        //빌드된 조회조건으로 스크롤 뉴스 엔티티 목록 조회
        List<ScrollNews> scrollNews = 
                (List<ScrollNews>) scrollNewsRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "inputDtm"));
        
        //목록조회된 엔티티 리스트 DTO변환
        List<ScrollNewsDTO> scrollNewsDTOList = scrollNewsMapper.toDtoList(scrollNews);
        
        //articleDTO 리스트 리턴
        return scrollNewsDTOList;
    }

    //스크롤 뉴스 상세조회
    public ScrollNewsDTO find(Long scrlNewsId) {

        ScrollNews scrollNews = findScrollNews(scrlNewsId); //스크롤뉴스 아이디로 조회 및 존재유무 확인

        ScrollNewsDTO scrollNewsDTO = scrollNewsMapper.toDto(scrollNews);

        return scrollNewsDTO;

    }

    //스크롤 뉴스 등록
    public Long create(ScrollNewsCreateDTO scrollNewsCreateDTO) throws Exception {

        String userId = userAuthService.authUser.getUserId(); //토큰 사용자 아이디 get
        scrollNewsCreateDTO.setInputrId(userId); //입력자 set

        ScrollNews scrollNews = scrollNewsCreateMapper.toEntity(scrollNewsCreateDTO);//등록DTO 엔티티 빌드.
        scrollNewsRepository.save(scrollNews);// 등록

        Long scrlNewsId = scrollNews.getScrlNewsId(); //스크롤 뉴스 상세에 set && 리턴해줄 스크롤 뉴스 아이디
        //스크롤 뉴스 상세 등록 리스트 get
        List<ScrollNewsDetailCreateDTO> scrollNewsDetailCreateDTO = scrollNewsCreateDTO.getScrollNewsDetails();

        createDetail(scrollNewsDetailCreateDTO, scrlNewsId); //스크럴 뉴스 상세 등록.

        return scrlNewsId;
    }

    public void update(ScrollNewsUpdateDTO scrollNewsUpdateDTO, Long scrlNewsId) throws Exception {

        ScrollNews scrollNews = findScrollNews(scrlNewsId); //스크롤뉴스 아이디로 조회 및 존재유무 확인

        String userId = userAuthService.authUser.getUserId();//토큰 사용자 아이디 get
        scrollNewsUpdateDTO.setUpdtrId(userId); //수정자 등록

        scrollNewsUpdateMapper.updateFromDto(scrollNewsUpdateDTO, scrollNews);//스크롤뉴스 정보 업데이트

        scrollNewsRepository.save(scrollNews); //수정

        //스크롤 뉴스 상세 수정 리스트 get
        List<ScrollNewsDetailCreateDTO> scrollNewsDetailCreateDTO = scrollNewsUpdateDTO.getScrollNewsDetail();

        //스크롤 뉴스 상세 수정.
        updateDetail(scrollNewsDetailCreateDTO, scrlNewsId);
    }

    //스크롤 뉴스 삭제
    public void delete(Long scrlNewsId) {

        ScrollNews scrollNews = findScrollNews(scrlNewsId); //스크롤뉴스 아이디로 조회 및 존재유무 확인

        String userId = userAuthService.authUser.getUserId();//토큰 사용자 아이디 get

        ScrollNewsDTO scrollNewsDTO = scrollNewsMapper.toDto(scrollNews);
        scrollNewsDTO.setDelDtm(new Date()); //삭제 일시 set
        scrollNewsDTO.setDelrId(userId); // 삭제자 set
        scrollNewsDTO.setDelYn("Y"); //삭제 여부값 Y

        scrollNewsMapper.updateFromDto(scrollNewsDTO, scrollNews);
        scrollNewsRepository.save(scrollNews); //스크롤 뉴스 삭제

    }

    //스크롤 뉴스 상세 업데이트
    public void updateDetail(List<ScrollNewsDetailCreateDTO> scrollNewsDetailCreateDTO, Long scrlNewsId) throws Exception {

        //스크롤뉴스 상세 List조회[기존에 등록되어 있던 스크롤뉴스 상세 삭재 후 재등록]
        List<ScrollNewsDetail> scrollNewsDetails = scrollNewsDetailRepository.findDetailsList(scrlNewsId);

        //조회된 스크롤뉴스 상세 리스트 삭제
        for (ScrollNewsDetail scrollNewsDetail : scrollNewsDetails) {
            Long id = scrollNewsDetail.getId();
            scrollNewsDetailRepository.deleteById(id);
        }

        createDetail(scrollNewsDetailCreateDTO, scrlNewsId); //스크롤뉴스 상세 재등록

    }

    //스크롤 뉴스 상세 리스트 등록
    public void createDetail(List<ScrollNewsDetailCreateDTO> scrollNewsDetailCreateDTO, Long scrlNewsId) throws Exception {

        //스크롤 뉴스 상세에 등록할 스크롤 뉴스 아이디 빌드
        ScrollNewsSimpleDTO scrollNewsSimpleDTO = ScrollNewsSimpleDTO.builder().scrlNewsId(scrlNewsId).build();

        //스크를 뉴스 상세 등록DTO 리스트 등록
        for (ScrollNewsDetailCreateDTO dto : scrollNewsDetailCreateDTO) {

            //내용 Json타입으로 변환
            List<ScrollNewsDetailCttJsonDTO> ctts = dto.getCttJsons();
            String returnCtt = "";
            if (CollectionUtils.isEmpty(ctts) == false){
                returnCtt = marshallingJsonHelper.MarshallingJson(ctts);
            }

           //dto.setCttJson(returnCtt);//내용 Json타입으로 변환
            dto.setScrollNews(scrollNewsSimpleDTO);//스크롤 뉴스 아이디 set
            ScrollNewsDetail scrollNewsDetail = scrollNewsDetailCreateMapper.toEntity(dto);//스크롤 뉴스 상세DTO 엔티티 변환
            scrollNewsDetail.setCttJson(returnCtt);
            scrollNewsDetailRepository.save(scrollNewsDetail);//스크롤 뉴스 상세 등록

        }

    }

    //내용 Json변환
    //기사 액션로그 엔티티 Json변환
 /*   public String entityToJson(String ctt) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        String jsonInString = mapper.writeValueAsString(ctt);

        return jsonInString;
    }*/


    //스크롤뉴스 아이디로 조회 및 존재유무 확인
    public ScrollNews findScrollNews(Long scrlNewsId) {

        //null방지를 위해 옵셔널로 스크롤 뉴스 조회[조회 조건 스크롤 뉴스 아이디 && 삭제여부 N]
        Optional<ScrollNews> scrollNews = scrollNewsRepository.findScrollNews(scrlNewsId);

        if (scrollNews.isPresent() == false) { //조회된 스크롤 뉴스가 없으면 에러처리.
            throw new ResourceNotFoundException("스크롤 뉴스를 찾을 수 없습니다. 스크롤 뉴스 아이디 : " + scrlNewsId);
        }

        return scrollNews.get(); //조회된 스크롤 뉴스 리턴
    }

    //스크롤 뉴스 목록조회 조건 빌드
    public BooleanBuilder getSearch(Date sdate, Date edate, String delYn){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QScrollNews qScrollNews = QScrollNews.scrollNews;

        //조회조건이 날짜로 들어온 경우
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false){
            booleanBuilder.and(qScrollNews.brdcDtm.between(sdate, edate));
        }

        //조회조건이 삭제 여부값으로 들어온 경우
        if (delYn != null && delYn.trim().isEmpty() ==false){
            booleanBuilder.and(qScrollNews.delYn.eq(delYn));
        }
        else { //삭제 여부값이 안들어왔을 경우 디폴트 N
            booleanBuilder.and(qScrollNews.delYn.eq("N"));
        }

        return booleanBuilder;
    }

}
