package com.gemiso.zodiac.app.articleActionLog;

import com.gemiso.zodiac.app.articleActionLog.dto.ArticleActionLogDTO;
import com.gemiso.zodiac.app.articleActionLog.mapper.ArticleActionLogMapper;
import com.gemiso.zodiac.core.helper.PageHelper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleActionLogService {

    private final ArticleActionLogRepository articleActionLogRepository;

    private final ArticleActionLogMapper articleActionLogMapper;


    //기사 액션 로그 목록조회
    public List<ArticleActionLogDTO> findAll(Long artclId){

        //long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기

        //페이지 셋팅 page, limit null일시 page = 1 limit = 50 디폴트 셋팅
        //PageHelper pageHelper = new PageHelper(1, 100);
        Pageable pageable = PageRequest.of(0,100);

        List<ArticleActionLogDTO> articleActionLogDTOList = new ArrayList<>();

        if (ObjectUtils.isEmpty(artclId) == false) {
            BooleanBuilder booleanBuilder = getSearch(artclId); //기사액션로그 목록조회 조회조건 빌드.

            //생성된 조회조건으로 기사액션로그 목록조회
            Page<ArticleActionLog> articleActionLogPage = articleActionLogRepository.findAll(booleanBuilder, pageable);

            List<ArticleActionLog> articleActionLogList = articleActionLogPage.getContent();

            //조회된 목록조회를 articleDTO List로 변환 후 리턴
            articleActionLogDTOList = articleActionLogMapper.toDtoList(articleActionLogList);

        }
        //long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        //long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
        //System.out.println("시간차이(m) : "+secDiffTime);

        return articleActionLogDTOList;
    }
    //기사 액션 로그 상세조회
    public ArticleActionLogDTO find(Long id){

        ArticleActionLog articleActionLog = findArticleActionLog(id);//기사 액션 로그 단건조회 및 존재유무 확인
        //조회된 정보 DTO변환 후 리턴
        ArticleActionLogDTO articleActionLogDTO = articleActionLogMapper.toDto(articleActionLog);

        return articleActionLogDTO;
    }
    //기사 액션 로그 단건조회 및 존재유무 확인
    public ArticleActionLog findArticleActionLog(Long id){

        //기사 액션 로그 조회 및 존재유무 확인
        //옵셔널로 null방지
        Optional<ArticleActionLog> articleActionLog = articleActionLogRepository.findArticleActionLog(id);

        if (articleActionLog.isPresent() == false){ //기사액션로그 아이디로 조회된 값이 없을 시 에러.
            throw new ResourceNotFoundException("기사 액션 로그를 찾을 수 없습니다. 기사 액션 로그 아이디 : "+id);
        }

        return articleActionLog.get(); //조회된 기사액션로그 리턴
    }
    //기사액션로그 목록조회 조회조건 빌드.
    public BooleanBuilder getSearch(Long artclId){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QArticleActionLog qArticleActionLog = QArticleActionLog.articleActionLog;

        if (ObjectUtils.isEmpty(artclId) == false){
            booleanBuilder.and(qArticleActionLog.article.artclId.eq(artclId)); //기사아이디 조회조건 추가
        }

        return booleanBuilder; //조회조건 리턴
    }
}
