package com.gemiso.zodiac.app.cueSheetTemplate;

import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateCreateDTO;
import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateDTO;
import com.gemiso.zodiac.app.cueSheetTemplate.mapper.CueSheetTemplateCreateMapper;
import com.gemiso.zodiac.app.cueSheetTemplate.mapper.CueSheetTemplateMapper;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    private final UserAuthService userAuthService;


    public List<CueSheetTemplateDTO> findAll(String searchWord, String pgmschTime){

        BooleanBuilder booleanBuilder = getSearch(searchWord, pgmschTime);

        List<CueSheetTemplate> cueSheetTemplateList = (List<CueSheetTemplate>) cueSheetTemplateRepository.findAll(booleanBuilder);

        List<CueSheetTemplateDTO> cueSheetTemplateDTOList = cueSheetTemplateMapper.toDtoList(cueSheetTemplateList);

        return cueSheetTemplateDTOList;
    }

    public CueSheetTemplateDTO find(Long cueTmpltId){

        CueSheetTemplate cueSheetTemplate = cueSheetTemplateFindOrFail(cueTmpltId);

        CueSheetTemplateDTO cueSheetTemplateDTO = cueSheetTemplateMapper.toDto(cueSheetTemplate);

        return cueSheetTemplateDTO;
    }

    public Long create(CueSheetTemplateCreateDTO cueSheetTemplateCreateDTO){

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        cueSheetTemplateCreateDTO.setInputrId(userId);

        CueSheetTemplate cueSheetTemplate = cueSheetTemplateCreateMapper.toEntity(cueSheetTemplateCreateDTO);

        cueSheetTemplateRepository.save(cueSheetTemplate);

        return cueSheetTemplate.getCueTmpltId();
    }

    public CueSheetTemplate cueSheetTemplateFindOrFail(Long cueTmpltId){

        Optional<CueSheetTemplate> cueSheetTemplate = cueSheetTemplateRepository.findCueTemplate(cueTmpltId);

        if (cueSheetTemplate.isPresent() == false){
            throw new ResourceNotFoundException("큐시트 아이템이 없습니다.");
        }

        return cueSheetTemplate.get();

    }

    public BooleanBuilder getSearch(String searchWord, String pgmschTime){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QCueSheetTemplate qCueSheetTemplate = QCueSheetTemplate.cueSheetTemplate;

        booleanBuilder.and(qCueSheetTemplate.delYn.eq("N"));

        if (StringUtils.isEmpty(searchWord) == false){
            booleanBuilder.and(qCueSheetTemplate.brdcPgmNm.contains(searchWord)
                    .or(qCueSheetTemplate.cueTmpltNm.contains(searchWord)));
        }
        if (!StringUtils.isEmpty(pgmschTime)){
            booleanBuilder.and(qCueSheetTemplate.pgmschTime.eq(pgmschTime));
        }

        return booleanBuilder;
    }
}
