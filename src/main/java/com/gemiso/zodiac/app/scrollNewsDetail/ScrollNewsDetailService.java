package com.gemiso.zodiac.app.scrollNewsDetail;

import com.gemiso.zodiac.app.scrollNews.QScrollNews;
import com.gemiso.zodiac.app.scrollNews.ScrollNews;
import com.gemiso.zodiac.app.scrollNewsDetail.dto.ScrollNewsDetailDTO;
import com.gemiso.zodiac.app.scrollNewsDetail.mapper.ScrollNewsDetailMapper;
import com.gemiso.zodiac.app.scrollNewsDetail.mapper.ScrollNewsDetailUpdateMapper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ScrollNewsDetailService {

    private final ScrollNewsDetailRepository scrollNewsDetailRepository;

    private final ScrollNewsDetailMapper scrollNewsDetailMapper;

    //스크롤 뉴스 상세 목록조회
    public List<ScrollNewsDetailDTO> findAll(Long scrlNewsId, String category){

        //목록조회 조건 빌드
        BooleanBuilder booleanBuilder = getSearch(scrlNewsId, category);

        //빌드된 조회 조건으로 엔티티 목록조회
        List<ScrollNewsDetail> scrollNewsDetailList =
                (List<ScrollNewsDetail>) scrollNewsDetailRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "cttOrd"));

        //조회된 엔티티 리스트 DTO변환
        List<ScrollNewsDetailDTO> scrollNewsDetailDTOList = scrollNewsDetailMapper.toDtoList(scrollNewsDetailList);

        //DTO 리스트 리턴
        return scrollNewsDetailDTOList;

    }

    //스크롤 뉴스 상세 단건조회
    public ScrollNewsDetailDTO find(Long id){

        //스크롤 뉴스 상세 단건조회[ 조회조건 스크롤 뉴스 상세 아이디] 및 존재유무 확인
        ScrollNewsDetail scrollNewsDetail = findDetail(id);

        //엔티티 DTO 변환
        ScrollNewsDetailDTO scrollNewsDetailDTO = scrollNewsDetailMapper.toDto(scrollNewsDetail);

        //DTO 리턴
        return scrollNewsDetailDTO;
    }

    //스크롤 뉴스 상세 삭제
    public void delete(Long id){

        //스크롤 뉴스 상세 단건조회[ 조회조건 스크롤 뉴스 상세 아이디] 및 존재유무 확인
        ScrollNewsDetail scrollNewsDetail = findDetail(id);

        scrollNewsDetailRepository.deleteById(id); //스크롤 뉴스 상세 아이디로 삭제

    }

    //스크롤 뉴스 상세 단건조회[ 조회조건 스크롤 뉴스 상세 아이디] 및 존재유무 확인
    public ScrollNewsDetail findDetail(Long id){
        
        //null방지를 위해 옵셔널로 단건조회
        Optional<ScrollNewsDetail> scrollNewsDetail = scrollNewsDetailRepository.findDetail(id);

        if (scrollNewsDetail.isPresent() == false){ //조회된 건수가 없으면 에러
            throw new ResourceNotFoundException("스크롤 뉴스 상세를 찾을 수 없습니다. 스크롤 뉴스 상세 아이디 : " +id);
        }

        //조회된 스크롤뉴스 상세 리턴
        return scrollNewsDetail.get();
    }

    //스크롤 뉴스 상세 목록조회 조회조건 빌드
    public BooleanBuilder getSearch(Long scrlNewsId, String category){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QScrollNewsDetail qScrollNewsDetail = QScrollNewsDetail.scrollNewsDetail;

        //스크롤 뉴스 아이디로 조회조건이 들어왔을 경우.
        if (ObjectUtils.isEmpty(scrlNewsId) ==false){
            booleanBuilder.and(qScrollNewsDetail.scrollNews.scrlNewsId.eq(scrlNewsId));
        }

        //카테고리코드
        if (category != null && category.trim().isEmpty() == false){
            booleanBuilder.and(qScrollNewsDetail.category.eq(category));
        }

        return booleanBuilder;
    }
}
