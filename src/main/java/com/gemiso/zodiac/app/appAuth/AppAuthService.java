package com.gemiso.zodiac.app.appAuth;

import com.gemiso.zodiac.app.appAuth.dto.AppAuthCreateDTO;
import com.gemiso.zodiac.app.appAuth.dto.AppAuthDTO;
import com.gemiso.zodiac.app.appAuth.dto.AppAuthUpdateDTO;
import com.gemiso.zodiac.app.appAuth.mapper.AppAuthCreateMapper;
import com.gemiso.zodiac.app.appAuth.mapper.AppAuthMapper;
import com.gemiso.zodiac.app.appAuth.mapper.AppAuthUpdateMapper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class AppAuthService{

    private final AppAuthRepository appAuthRepository;

    private final AppAuthMapper appAuthMapper;
    private final AppAuthCreateMapper appAuthCreateMapper;
    private final AppAuthUpdateMapper appAuthUpdateMapper;

    public List<AppAuthDTO> findAll(String useYn, String delYn, String hrnkAppAuthCd, String searchWord){

        BooleanBuilder booleanBuilder = getSearch(useYn, delYn, hrnkAppAuthCd, searchWord);

        List<AppAuth> appAuthList = (List<AppAuth>) appAuthRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "ord"));

        List<AppAuthDTO> appAuthDtoList = appAuthMapper.toDtoList(appAuthList);

        return appAuthDtoList;
    }

    public AppAuthDTO create(AppAuthCreateDTO appAuthCreatDTO) {

        String hrnkCd = appAuthCreatDTO.getHrnkAppAuthCd();

        if (!StringUtils.isEmpty(hrnkCd)){
           int chillOrd = appAuthRepository.findChildOrd(hrnkCd);
            appAuthCreatDTO.setOrd(chillOrd+1);
        }else {
            int parentOrd = appAuthRepository.findParentOrd();
            appAuthCreatDTO.setOrd(parentOrd+1);
        }

        AppAuth appAuth = appAuthCreateMapper.toEntity(appAuthCreatDTO);

        appAuthRepository.save(appAuth);

       return appAuthMapper.toDto(appAuth);

    }

    public AppAuthDTO find(Long appAuthId) {
        Optional<AppAuth> appAuth = appAuthRepository.findById(appAuthId);

        if (!appAuth.isPresent()){
            new ResourceNotFoundException("AppAuth not found. appAuthId : " + appAuthId);
        }

        AppAuthDTO appAuthDTO = appAuthMapper.toDto(appAuth.get());

        return appAuthDTO;
    }

    public void update(AppAuthUpdateDTO appAuthUpdateDTO, Long appAuthId){

        appAuthUpdateDTO.setAppAuthId(appAuthId);
        appAuthUpdateDTO.setUpdtrId("updtrId");

        AppAuth appAuthEntity = appAuthUpdateMapper.toEntity(appAuthUpdateDTO);

        appAuthRepository.save(appAuthEntity);

    }

    public void delete(Long appAuthId){

        AppAuth appAuthEntity = appAuthRepository.findById(appAuthId)
                .orElseThrow(() -> new ResourceNotFoundException("AppAuth not found. appAuthId: "+appAuthId));
        AppAuthDTO appAuthDTO = appAuthMapper.toDto(appAuthEntity);

        appAuthDTO.setDelYn("Y");
        appAuthDTO.setDelrId("삭제자 아이디");
        appAuthDTO.setDelDtm(new Date());

        AppAuth appAuth = appAuthMapper.toEntity(appAuthDTO);

        appAuthRepository.save(appAuth);

    }

    private BooleanBuilder getSearch(String useYn, String delYn, String hrnkAppAuthCd, String searchWord) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QAppAuth qAppAuth = QAppAuth.appAuth;

        if(!StringUtils.isEmpty(useYn)){
            booleanBuilder.and(qAppAuth.useYn.eq(useYn));
        }
        if(!StringUtils.isEmpty(delYn)){
            booleanBuilder.and(qAppAuth.delYn.eq(delYn));
        }
        if(!StringUtils.isEmpty(hrnkAppAuthCd)){
            booleanBuilder.and(qAppAuth.hrnkAppAuthCd.eq(hrnkAppAuthCd));
        }
        if(!StringUtils.isEmpty(searchWord)){
            booleanBuilder.and(qAppAuth.appAuthNm.contains(searchWord));
        }

        //.and((Predicate) Sort.by((List<Sort.Order>) qAppAuth.ord).descending());

        return booleanBuilder;
    }
}
