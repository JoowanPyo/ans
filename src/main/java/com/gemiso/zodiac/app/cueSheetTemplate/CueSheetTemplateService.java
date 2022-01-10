package com.gemiso.zodiac.app.cueSheetTemplate;

import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateCreateDTO;
import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateDTO;
import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateUpdateDTO;
import com.gemiso.zodiac.app.cueSheetTemplate.mapper.CueSheetTemplateCreateMapper;
import com.gemiso.zodiac.app.cueSheetTemplate.mapper.CueSheetTemplateMapper;
import com.gemiso.zodiac.app.cueSheetTemplate.mapper.CueSheetTemplateUpdateMapper;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CueSheetTemplateService {

    private final CueSheetTemplateRepository cueSheetTemplateRepository;

    private final CueSheetTemplateMapper cueSheetTemplateMapper;
    private final CueSheetTemplateCreateMapper cueSheetTemplateCreateMapper;
    private final CueSheetTemplateUpdateMapper cueSheetTemplateUpdateMapper;

    private final UserAuthService userAuthService;


    //큐시트 템플릿 목록조회
    public List<CueSheetTemplateDTO> findAll(String searchWord, String brdcPgmId){

        BooleanBuilder booleanBuilder = getSearch(searchWord, brdcPgmId); //목록조회 조회조건 빌드.

        //생성된 목록조건으로 큐시트 템플릿 목록조회.
        List<CueSheetTemplate> cueSheetTemplateList = (List<CueSheetTemplate>) cueSheetTemplateRepository.findAll(booleanBuilder);

        //조회된 엔티티 리스트 DTO리스트로 변환후 return.
        List<CueSheetTemplateDTO> cueSheetTemplateDTOList = cueSheetTemplateMapper.toDtoList(cueSheetTemplateList);

        return cueSheetTemplateDTOList;
    }

    //큐시트 템플릿 단건조회
    public CueSheetTemplateDTO find(Long cueTmpltId){

        CueSheetTemplate cueSheetTemplate = cueSheetTemplateFindOrFail(cueTmpltId);//큐시트 템플릿 존재 유무 확인 및 단건조회.

        //조회된 큐시트 템플릿 엔티티 DTO로 변환후 리턴.
        CueSheetTemplateDTO cueSheetTemplateDTO = cueSheetTemplateMapper.toDto(cueSheetTemplate);

        return cueSheetTemplateDTO;
    }

    //큐시트 템플릿 등록
    public Long create(CueSheetTemplateCreateDTO cueSheetTemplateCreateDTO){

        // 토큰 인증된 사용자 아이디를 입력자로 등록.
        String userId = userAuthService.authUser.getUserId();
        cueSheetTemplateCreateDTO.setInputrId(userId);

        //DTO -> 엔티티 변환.
        CueSheetTemplate cueSheetTemplate = cueSheetTemplateCreateMapper.toEntity(cueSheetTemplateCreateDTO);

        //큐시트 템플릿 엔티티 등록.
        cueSheetTemplateRepository.save(cueSheetTemplate);

        return cueSheetTemplate.getCueTmpltId();
    }

    //큐시트 템플릿 수정
    public void update(Long cueTmpltId, CueSheetTemplateUpdateDTO cueSheetTemplateUpdateDTO){

        CueSheetTemplate cueSheetTemplate = cueSheetTemplateFindOrFail(cueTmpltId);//큐시트 템플릿 존재 유무 확인 및 단건조회.

        // 토큰 인증된 사용자 아이디를 입력자로 등록.
        String userId = userAuthService.authUser.getUserId();
        cueSheetTemplateUpdateDTO.setUpdtrId(userId);
        //조회된 기존등록된 큐시트 템플릿 정보에 업데이트 정보로 들어온 바뀐 정보 set.
        cueSheetTemplateUpdateMapper.updateFromDto(cueSheetTemplateUpdateDTO, cueSheetTemplate);
        //큐시트 템플릿 엔티티 업데이트.
        cueSheetTemplateRepository.save(cueSheetTemplate);


    }

    public void delete(Long cueTmpltId){

        CueSheetTemplate cueSheetTemplate = cueSheetTemplateFindOrFail(cueTmpltId);//큐시트 템플릿 존재 유무 확인 및 단건조회.
        //조회된 큐시트 템플릿 엔티티 DTO변환 [데이터 셋팅 위해 DTO 변환].
        CueSheetTemplateDTO cueSheetTemplateDTO = cueSheetTemplateMapper.toDto(cueSheetTemplate);

        cueSheetTemplateDTO.setDelDtm(new Date());//삭제일시 set.
        cueSheetTemplateDTO.setDelYn("Y");//삭제여부값 "Y"
        // 토큰 인증된 사용자 아이디를 입력자로 등록.
        String userId = userAuthService.authUser.getUserId();
        cueSheetTemplateDTO.setDelrId(userId);
        //조회된 기존등록된 큐시트 템플릿 정보에 업데이트 정보로 들어온 바뀐 정보 set.
        cueSheetTemplateMapper.updateFromDto(cueSheetTemplateDTO, cueSheetTemplate);
        //삭제 등록.
        cueSheetTemplateRepository.save(cueSheetTemplate);

    }

    public CueSheetTemplate cueSheetTemplateFindOrFail(Long cueTmpltId){

        Optional<CueSheetTemplate> cueSheetTemplate = cueSheetTemplateRepository.findCueTemplate(cueTmpltId);

        if (cueSheetTemplate.isPresent() == false){
            throw new ResourceNotFoundException("큐시트 아이템이 없습니다.");
        }

        return cueSheetTemplate.get();

    }

    public BooleanBuilder getSearch(String searchWord, String brdcPgmId){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QCueSheetTemplate qCueSheetTemplate = QCueSheetTemplate.cueSheetTemplate;

        booleanBuilder.and(qCueSheetTemplate.delYn.eq("N"));

        //검색어가 조회조건으로 들어온 경우
        if (searchWord != null && searchWord.trim().isEmpty() ==false){
            booleanBuilder.and(qCueSheetTemplate.brdcPgmNm.contains(searchWord)
                    .or(qCueSheetTemplate.cueTmpltNm.contains(searchWord)));
        }
        //방송프로그램 아이디가 조회 조건으로 들어온 경우
        if (brdcPgmId != null && brdcPgmId.trim().isEmpty() ==false){
            booleanBuilder.and(qCueSheetTemplate.program.brdcPgmId.eq(brdcPgmId));
        }

        return booleanBuilder;
    }
}
