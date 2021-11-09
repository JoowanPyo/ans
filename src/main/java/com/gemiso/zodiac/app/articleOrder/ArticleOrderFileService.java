package com.gemiso.zodiac.app.articleOrder;

import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderFileCreateDTO;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderFileDTO;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderSimpleDTO;
import com.gemiso.zodiac.app.articleOrder.mapper.ArticleOrderFileCreateMapper;
import com.gemiso.zodiac.app.articleOrder.mapper.ArticleOrderFileMapper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleOrderFileService {

    private final ArticleOrderFileRepository articleOrderFileRepository;

    private final ArticleOrderFileMapper articleOrderFileMapper;
    private final ArticleOrderFileCreateMapper articleOrderFileCreateMapper;


    public List<ArticleOrderFileDTO> findAll(Long orderId){ //의뢰파일첨부 목록조회

        BooleanBuilder booleanBuilder = getSearch(orderId);//목록조회 조회조건 생성.

        //생성된 조회조건으로 해당 목록조회
        List<ArticleOrderFile> articleOrderFiles = (List<ArticleOrderFile>) articleOrderFileRepository.findAll(booleanBuilder);

        //엔티티 DTO변환
        List<ArticleOrderFileDTO> articleOrderFileDTOList = articleOrderFileMapper.toDtoList(articleOrderFiles);

        return articleOrderFileDTOList;
    }

    public BooleanBuilder getSearch(Long orderId){//목록조회 조회조건 생성.

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QArticleOrderFile qArticleOrderFile = QArticleOrderFile.articleOrderFile;

        if (ObjectUtils.isEmpty(orderId) == false){
            booleanBuilder.and(qArticleOrderFile.articleOrder.orderId.eq(orderId));
        }

        return booleanBuilder;
    }

    //기사의회첨부파일 조회.
    public ArticleOrderFileDTO find(Long id){ // 의뢰첨부파일 상세조회

        ArticleOrderFile articleOrderFile = findOrderFile(id);

        ArticleOrderFileDTO articleOrderFileDTO = articleOrderFileMapper.toDto(articleOrderFile);

        return articleOrderFileDTO;
    }

    //기사의회첨부파일 등록.
    public void create(List<ArticleOrderFileCreateDTO> articleOrderFileCreateDTOList, Long orderId){


/*        //이미 등록된 파일이 있는지 확인. [ 기사오더 아이디로 조회 ]
        Optional<ArticleOrderFile> getArticleOrderFile = articleOrderFileRepository.findOrderId(orderId);

        if (getArticleOrderFile.isPresent()){ //의뢰에 이미 첨부파일이 등록되어 있을 경우 삭제.
            ArticleOrderFile articleOrderFile = getArticleOrderFile.get(); //조회된 기사의뢰첨부파일 옵셔널에서 get
            Long articleOrderFileId = articleOrderFile.getId(); //기사의뢰첨부파일 아이디 get
            articleOrderFileRepository.deleteById(articleOrderFileId); //기존에 등록된 기사의뢰첨부파일 삭제
        }*/

        //의뢰아이디를 의뢰DTO로 빌드해서 첨부파일DTO에 set
        ArticleOrderSimpleDTO articleOrderSimpleDTO = ArticleOrderSimpleDTO.builder().orderId(orderId).build();

        //의뢰 첨부파일 List 등록
        for (ArticleOrderFileCreateDTO orderFileCreateDTO : articleOrderFileCreateDTOList) {

            orderFileCreateDTO.setArticleOrder(articleOrderSimpleDTO); //oderId set

            //기사의회첨부파일 엔티티 변환후 등록.
            ArticleOrderFile articleOrderFile = articleOrderFileCreateMapper.toEntity(orderFileCreateDTO);
            articleOrderFileRepository.save(articleOrderFile); //저장
        }



        //생성된 아이디 리턴.
        /*return articleOrderFile.getId();*/
    }

    //의회 첨부파일 업데이트.
    public void update(Long orderId, List<ArticleOrderFileCreateDTO> articleOrderFileCreateDTOList){

        //기사 오더아이디로 등되어있는 첨부파일 List조회
        List<ArticleOrderFile> articleOrderFileList = articleOrderFileRepository.findOrderFileList(orderId);

        for (ArticleOrderFile articleOrderFile : articleOrderFileList){ //등록되어 있던 리스트 전부 삭제
            Long id = articleOrderFile.getId();

            articleOrderFileRepository.deleteById(id);
        }

        //의뢰 아이디 DTO로 변환후 set
        ArticleOrderSimpleDTO articleOrderSimpleDTO = ArticleOrderSimpleDTO.builder().orderId(orderId).build();

        //의뢰 첨부파일 List 등록
        for (ArticleOrderFileCreateDTO orderFileCreateDTO : articleOrderFileCreateDTOList) {

            orderFileCreateDTO.setArticleOrder(articleOrderSimpleDTO); //oderId set

            //기사의회첨부파일 엔티티 변환후 등록.
            ArticleOrderFile articleOrderFile = articleOrderFileCreateMapper.toEntity(orderFileCreateDTO);
            articleOrderFileRepository.save(articleOrderFile); //저장
        }

    }

    //의회 첨부파일 삭제.
    public void delete(Long orderFileId){

        ArticleOrderFile articleOrderFile = findOrderFile(orderFileId);//기사의뢰 첨부파일 조회 및 존재유무 확인 및 엔티티 조회

        articleOrderFileRepository.deleteById(orderFileId);//삭제.

    }

    //기사의뢰 첨부파일 조회 및 존재유무 확인.
    public ArticleOrderFile findOrderFile(Long id){

        Optional<ArticleOrderFile> articleOrderFile = articleOrderFileRepository.findById(id);

        if (articleOrderFile.isPresent() == false){
            throw new ResourceNotFoundException("기사의뢰 첨부파일을 찾을 수 없습니다. 기사의뢰 첨부파일 아이디 : "+id);
        }

        return articleOrderFile.get();
    }
}
