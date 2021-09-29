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

        //하위 권한이 있을 경우 하위 권한 GET
        if (!StringUtils.isEmpty(hrnkCd)){
           Optional<Integer> chillOrd = appAuthRepository.findChildOrd(hrnkCd);
           if (chillOrd.isPresent()) {
               Integer getChillOrd = chillOrd.get();
               int setChillOrd = getChillOrd;
               appAuthCreatDTO.setOrd(setChillOrd + 1);
           }else {
               appAuthCreatDTO.setOrd(1);
           }
        }else { //하위 권한이 없을 경우 상위 Ord GET
            Optional<Integer> parentOrd = appAuthRepository.findParentOrd();

            if (parentOrd.isPresent()){
                Integer ord = parentOrd.get();
                int setOrd = ord;
                appAuthCreatDTO.setOrd(setOrd+1);
            }else {
                appAuthCreatDTO.setOrd(1);
            }

        }

        AppAuth appAuth = appAuthCreateMapper.toEntity(appAuthCreatDTO);

        appAuthRepository.save(appAuth);

       return appAuthMapper.toDto(appAuth);

    }

    public AppAuthDTO find(Long appAuthId) {

        AppAuth appAuth = appAuthFindOrFail(appAuthId);

        AppAuthDTO appAuthDTO = appAuthMapper.toDto(appAuth);

        return appAuthDTO;
    }

    public void update(AppAuthUpdateDTO appAuthUpdateDTO, Long appAuthId){

        AppAuth appAuth = appAuthFindOrFail(appAuthId);

        appAuthUpdateDTO.setAppAuthId(appAuthId);
        appAuthUpdateDTO.setUpdtrId("updtrId");

        appAuthUpdateMapper.updateFromDto(appAuthUpdateDTO, appAuth);

        appAuthRepository.save(appAuth);

    }

    public void delete(Long appAuthId){

        AppAuth appAuthEntity = appAuthFindOrFail(appAuthId);
        AppAuthDTO appAuthDTO = appAuthMapper.toDto(appAuthEntity);

        appAuthDTO.setDelYn("Y");
        appAuthDTO.setDelrId("삭제자 아이디");
        appAuthDTO.setDelDtm(new Date());

        AppAuth appAuth = appAuthMapper.toEntity(appAuthDTO);

        appAuthRepository.save(appAuth);

    }

    public AppAuth appAuthFindOrFail(Long appAuthId){

        Optional<AppAuth> appAuth = appAuthRepository.findByAppAuthId(appAuthId);

        if (!appAuth.isPresent()){
            throw new ResourceNotFoundException("AppAuth not found. AppAuthId: "+appAuthId);
        }

        return appAuth.get();
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

        //.and((Predicate) Sort.by((List<Sort.ArticleOrder>) qAppAuth.ord).descending());

        return booleanBuilder;
    }
}
