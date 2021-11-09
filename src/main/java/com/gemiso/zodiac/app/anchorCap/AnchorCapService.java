package com.gemiso.zodiac.app.anchorCap;

import com.gemiso.zodiac.app.anchorCap.dto.AnchorCapCreateDTO;
import com.gemiso.zodiac.app.anchorCap.dto.AnchorCapDTO;
import com.gemiso.zodiac.app.anchorCap.dto.AnchorCapUpdateDTO;
import com.gemiso.zodiac.app.anchorCap.mapper.AnchorCapCreateMapper;
import com.gemiso.zodiac.app.anchorCap.mapper.AnchorCapMapper;
import com.gemiso.zodiac.app.anchorCap.mapper.AnchorCapUpdateMapper;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.capTemplate.CapTemplate;
import com.gemiso.zodiac.app.symbol.Symbol;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AnchorCapService {

    private final AnchorCapRepository anchorCapRepository;

    private final AnchorCapMapper anchorCapMapper;
    private final AnchorCapCreateMapper anchorCapCreateMapper;
    private final AnchorCapUpdateMapper anchorCapUpdateMapper;


    public List<AnchorCapDTO> findAll(Long anchorCapId){

        BooleanBuilder booleanBuilder = getSearch(anchorCapId);

        List<AnchorCap> anchorCapList =
                (List<AnchorCap>) anchorCapRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "lnNo"));

        List<AnchorCapDTO> anchorCapDTOList = anchorCapMapper.toDtoList(anchorCapList);

        return anchorCapDTOList;
    }
    
    //앵커자막 상세조회
    public AnchorCapDTO find(Long anchorCapId){

        AnchorCap anchorCap = findAnchorCap(anchorCapId);//앵커자막 여부 확인 및 조회.

        AnchorCapDTO anchorCapDTO = anchorCapMapper.toDto(anchorCap); //조회된 앵커자막 DTO변환

        return anchorCapDTO;

    }

    //앵커자막 등록
    public Long create(AnchorCapCreateDTO anchorCapCreateDTO){

        AnchorCap anchorCap = anchorCapCreateMapper.toEntity(anchorCapCreateDTO); //등록 앵커자막 엔티티 변환

        anchorCap = relationshipCreate(anchorCap, anchorCapCreateDTO); //등록할 기사, 템플릿, 방송아이콘 엔티티 변환후 set

        anchorCapRepository.save(anchorCap); //등록

        return anchorCap.getAnchorCapId(); //responce Id
    }

    //앵커자막 업데이트
    public void update(AnchorCapUpdateDTO anchorCapUpdateDTO, Long anchorCapId){

        anchorCapUpdateDTO.setAnchorCapId(anchorCapId);

        AnchorCap anchorCap = findAnchorCap(anchorCapId);//앵커자막 여부 확인 및 조회.

        anchorCap = relationshipUpdate(anchorCap, anchorCapUpdateDTO); //등록할 기사, 템플릿, 방송아이콘 엔티티 변환후 set

        anchorCapUpdateMapper.updateFromDto(anchorCapUpdateDTO, anchorCap); // update 맵퍼를 활용해서 수정사항 update

        anchorCapRepository.save(anchorCap); //update

    }

    //앵커자막 삭제
    public void delete(Long anchorCapId){

        AnchorCap anchorCap = findAnchorCap(anchorCapId);//앵커자막 여부 확인 및 조회.

        anchorCapRepository.deleteById(anchorCapId); //앵커자막 삭제.

    }

    public AnchorCap relationshipUpdate(AnchorCap anchorCap, AnchorCapUpdateDTO anchorCapUpdateDTO){

        //Long articleId = anchorCapCreateDTO.getArticleId();
        Long capTmpltId = anchorCapUpdateDTO.getCapTemplateId();
        String symbolId = anchorCapUpdateDTO.getSymbolId();

        /*if (ObjectUtils.isEmpty(articleId) == false) { //기사아이디가 있으면 엔엔티 빌드후 set
            Article article = Article.builder().artclId(articleId).build();
            anchorCap.setArticle(article);
        }*/
        if (ObjectUtils.isEmpty(capTmpltId) == false){ //템플릿아이가 있으면 엔엔티 빌드후 set
            CapTemplate capTemplate = CapTemplate.builder().capTmpltId(capTmpltId).build();
            anchorCap.setCapTemplate(capTemplate);
        }
        else
        {
            anchorCap.setCapTemplate(null); //null이나 공백으로 들어올 경우 데이터 삭제
        }
        if (StringUtils.isEmpty(symbolId) == false){ //방송아이콘 아이디가 있으면 엔엔티 빌드후 set
            Symbol symbol = Symbol.builder().symbolId(symbolId).build();
            anchorCap.setSymbol(symbol);
        }
        else
        {
            anchorCap.setSymbol(null);//null이나 공백으로 들어올 경우 데이터 삭제
        }

        return anchorCap;

    }

    //앵커자막 여부 확인 및 조회.
    public AnchorCap findAnchorCap(Long anchorCapId){

        Optional<AnchorCap> anchorCap = anchorCapRepository.findAnchorCap(anchorCapId);

        if (anchorCap.isPresent() == false){
            throw  new ResourceNotFoundException("앵커자막을 찾을 수 없습니다. 앵커자막 아이디 : "+anchorCapId);
        }

        return anchorCap.get();
    }

    //앵커자막에 들어오는 기사, 템플릿, 방송아이콘 set
    public AnchorCap relationshipCreate(AnchorCap anchorCap, AnchorCapCreateDTO anchorCapCreateDTO){

        Long articleId = anchorCapCreateDTO.getArticleId();
        Long capTmpltId = anchorCapCreateDTO.getCapTemplateId();
        String symbolId = anchorCapCreateDTO.getSymbolId();

        if (ObjectUtils.isEmpty(articleId) == false) { //기사아이디가 있으면 엔엔티 빌드후 set
            Article article = Article.builder().artclId(articleId).build();
            anchorCap.setArticle(article);
        }
        if (ObjectUtils.isEmpty(capTmpltId) == false){ //템플릿아이가 있으면 엔엔티 빌드후 set
            CapTemplate capTemplate = CapTemplate.builder().capTmpltId(capTmpltId).build();
            anchorCap.setCapTemplate(capTemplate);
        }
        if (StringUtils.isEmpty(symbolId) == false){ //방송아이콘 아이디가 있으면 엔엔티 빌드후 set
            Symbol symbol = Symbol.builder().symbolId(symbolId).build();
            anchorCap.setSymbol(symbol);
        }

        return anchorCap;
    }

    //목록조회 검색조건 빌드
    public BooleanBuilder getSearch(Long anchorCapId){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QAnchorCap qAnchorCap = QAnchorCap.anchorCap;

        if (ObjectUtils.isEmpty(anchorCapId) == false){ //앵커자막 아이디 검색조건 추가
            booleanBuilder.and(qAnchorCap.anchorCapId.eq(anchorCapId));
        }

        return booleanBuilder;

    }
}
