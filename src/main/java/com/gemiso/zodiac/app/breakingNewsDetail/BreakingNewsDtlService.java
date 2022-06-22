package com.gemiso.zodiac.app.breakingNewsDetail;

import com.gemiso.zodiac.app.breakingNews.QBreakingNews;
import com.gemiso.zodiac.app.breakingNewsDetail.dto.BreakingNewsDtlDTO;
import com.gemiso.zodiac.app.breakingNewsDetail.mapper.BreakingNewsDtlMapper;
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
public class BreakingNewsDtlService {

    private final BreakingNewsDtlRepository breakingNewsDtlRepository;

    private final BreakingNewsDtlMapper breakingNewsDtlMapper;

    //속보뉴스 상세 목록조회
    public List<BreakingNewsDtlDTO> findAll(Long breakingNewsId){

        BooleanBuilder booleanBuilder = getSearch(breakingNewsId);//속보뉴스 상세 목록조회 조회조건 빌드

        //생성된 조회조건으로 속보뉴스 상세 목록조회
        List<BreakingNewsDtl> breakingNewsDtls = 
                (List<BreakingNewsDtl>) breakingNewsDtlRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "ord"));
    
        //조회된 엔티티 리스트를 DTO변환
        List<BreakingNewsDtlDTO> breakingNewsDtlDTOList = breakingNewsDtlMapper.toDtoList(breakingNewsDtls);
        
        //articleDTO 리턴
        return breakingNewsDtlDTOList;
    }

    //속보뉴스 상세 상세조회
    public BreakingNewsDtlDTO find(Long id){

        BreakingNewsDtl breakingNewsDtl = findDetail(id); //속보뉴스 상세 조회 및 존재유무 확인[조회조건 아이디]

        //조회된 속보뉴스 상세 DTO변환
        BreakingNewsDtlDTO breakingNewsDtlDTO = breakingNewsDtlMapper.toDto(breakingNewsDtl);

        //DTO리턴
        return breakingNewsDtlDTO;
    }

    //속보뉴스 삭제
    public void delete(Long id){

        BreakingNewsDtl breakingNewsDtl = findDetail(id); //속보뉴스 상세 조회 및 존재유무 확인[조회조건 아이디]

        breakingNewsDtlRepository.deleteById(id);
    }

    //속보뉴스 상세 조회 및 존재유무 확인[조회조건 아이디]
    public BreakingNewsDtl findDetail(Long id){

        //null방지를 위해 옵셔널로 조회
        Optional<BreakingNewsDtl> breakingNewsDtl = breakingNewsDtlRepository.findDetail(id);

        if (breakingNewsDtl.isPresent() == false){//조회된 속보뉴스 상세가 없으면 에러
            throw new ResourceNotFoundException("속보뉴스 상세를 찾을 수 없습니다. 속보뉴스 상세 아이디 : "+id);
        }
        //조회된 속보뉴스 리턴
        return breakingNewsDtl.get();
    }

    //속보뉴스 상세 목록조회 조회조건 빌드
    public BooleanBuilder getSearch(Long breakingNewsId){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QBreakingNewsDtl qBreakingNewsDtl = QBreakingNewsDtl.breakingNewsDtl;

        //속보뉴스 아이디로 조회조건이 들어온 경우.
        if (ObjectUtils.isEmpty(breakingNewsId) == false){
            booleanBuilder.and(qBreakingNewsDtl.breakingNews.breakingNewsId.eq(breakingNewsId));
        }

        return booleanBuilder;
    }
}
