package com.gemiso.zodiac.app.capTemplateGrp;

import com.gemiso.zodiac.app.capTemplateGrp.dto.CapTemplateGrpCreateDTO;
import com.gemiso.zodiac.app.capTemplateGrp.dto.CapTemplateGrpDTO;
import com.gemiso.zodiac.app.capTemplateGrp.dto.CapTemplateGrpUpdateDTO;
import com.gemiso.zodiac.app.capTemplateGrp.mapper.CapTemplateGrpCreateMapper;
import com.gemiso.zodiac.app.capTemplateGrp.mapper.CapTemplateGrpMapper;
import com.gemiso.zodiac.app.capTemplateGrp.mapper.CapTemplateGrpUpdateMapper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CapTemplateGrpService {

    private final CapTemplateGrpRepository capTemplateGrpRepository;

    private final CapTemplateGrpMapper capTemplateGrpMapper;
    private final CapTemplateGrpCreateMapper capTemplateGrpCreateMapper;
    private final CapTemplateGrpUpdateMapper capTemplateGrpUpdateMapper;

    //private final UserAuthService userAuthService;



    public List<CapTemplateGrpDTO> findAll(String searchWord){

        BooleanBuilder booleanBuilder = getSearch(searchWord);

        List<CapTemplateGrp> capTemplateGrpList = (List<CapTemplateGrp>) capTemplateGrpRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "inputDtm"));

        List<CapTemplateGrpDTO> capTemplateGrpDTOList = capTemplateGrpMapper.toDtoList(capTemplateGrpList);

        return capTemplateGrpDTOList;
    }

    public CapTemplateGrpDTO find(Long tmpltGrpId){

        CapTemplateGrp capTemplateGrp = templateFindOrFail(tmpltGrpId);

        CapTemplateGrpDTO capTemplateGrpDTO = capTemplateGrpMapper.toDto(capTemplateGrp);

        return capTemplateGrpDTO;

    }

    public Long create(CapTemplateGrpCreateDTO capTemplateGrpCreateDTO, String userId){

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        //String userId = userAuthService.authUser.getUserId();
        capTemplateGrpCreateDTO.setInputrId(userId);//입력자 추가.

        CapTemplateGrp capTemplateGrp = capTemplateGrpCreateMapper.toEntity(capTemplateGrpCreateDTO);

        capTemplateGrpRepository.save(capTemplateGrp);

        return capTemplateGrp.getTmpltGrpId();

    }

    public void update(CapTemplateGrpUpdateDTO capTemplateGrpUpdateDTO, Long tmplGrpId, String userId){
        
        CapTemplateGrp capTemplateGrp = templateFindOrFail(tmplGrpId);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        //String userId = userAuthService.authUser.getUserId();
        capTemplateGrpUpdateDTO.setUpdtrId(userId);//수정자 추가.

        capTemplateGrpUpdateMapper.updateFromDto(capTemplateGrpUpdateDTO, capTemplateGrp);
        capTemplateGrpRepository.save(capTemplateGrp);

    }

    public void delete(Long tmplGrpId){

        CapTemplateGrp capTemplateGrp = templateFindOrFail(tmplGrpId);

        CapTemplateGrpDTO capTemplateGrpDTO = capTemplateGrpMapper.toDto(capTemplateGrp);

        capTemplateGrpDTO.setDelYn("Y");

        capTemplateGrpMapper.updateFromDto(capTemplateGrpDTO, capTemplateGrp);
        capTemplateGrpRepository.save(capTemplateGrp);

    }

    public CapTemplateGrp templateFindOrFail(Long tmpltGrpId){

       /*return  userGroupRepository.findById(userGrpId)
                .orElseThrow(() -> new ResourceNotFoundException("UserGroupId not found. userGroupId : " + userGrpId));*/
        Optional<CapTemplateGrp> template = capTemplateGrpRepository.findByTemplate(tmpltGrpId);

        if (!template.isPresent()){
            throw new ResourceNotFoundException("자막 템플릿 그룹을 찾을 수 없습니다. 자막 템플릿 그룹 아이디 : " + tmpltGrpId);
        }

        return template.get();
    }

    private BooleanBuilder getSearch(String searchWord) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QCapTemplateGrp qCapTemplateGrp = QCapTemplateGrp.capTemplateGrp;

        booleanBuilder.and(qCapTemplateGrp.delYn.eq("N"));

        if(searchWord != null && searchWord.trim().isEmpty() == false){
            booleanBuilder.and(qCapTemplateGrp.tmpltGrpNm.contains(searchWord));
        }

        return booleanBuilder;
    }
}
