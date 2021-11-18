package com.gemiso.zodiac.app.anchorCapHist;

import com.gemiso.zodiac.app.anchorCapHist.dto.AnchorCapHistDTO;
import com.gemiso.zodiac.app.anchorCapHist.mapper.AnchorCapHistMapper;
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
public class AnchorCapHistService {

    private final AnchorCapHistRepository anchorCapHistRepository;

    private final AnchorCapHistMapper anchorCapHistMapper;


    
    public List<AnchorCapHistDTO> findAll(Long artclHistId){

        BooleanBuilder booleanBuilder = getSearch(artclHistId);

        List<AnchorCapHist> anchorCapHistList = (List<AnchorCapHist>) anchorCapHistRepository.findAll(booleanBuilder);

        List<AnchorCapHistDTO> anchorCapHistDTOList = anchorCapHistMapper.toDtoList(anchorCapHistList);

        return anchorCapHistDTOList;
    }
    
    public BooleanBuilder getSearch(Long artclHistId){
        
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QAnchorCapHist qAnchorCapHist = QAnchorCapHist.anchorCapHist;

        if (ObjectUtils.isEmpty(artclHistId) == false){
            booleanBuilder.and(qAnchorCapHist.articleHist.artclHistId.eq(artclHistId));
        }

        return booleanBuilder;
    }

    public AnchorCapHistDTO find(Long ancCapHistId){

        AnchorCapHist anchorCapHist = findAnchorCapHist(ancCapHistId);

        AnchorCapHistDTO anchorCapHistDTO = anchorCapHistMapper.toDto(anchorCapHist);

        return anchorCapHistDTO;
    }

    public AnchorCapHist findAnchorCapHist(Long ancCapHistId){

        //
        Optional<AnchorCapHist> anchorCapHist = anchorCapHistRepository.findAnchorCapHist(ancCapHistId);

        if (anchorCapHist.isPresent() == false){
            throw new ResourceNotFoundException("앵커자막 이력을 찾을수 없습니다. 앵커자막 아이디 : "+ancCapHistId);
        }

        return anchorCapHist.get();
    }
}
