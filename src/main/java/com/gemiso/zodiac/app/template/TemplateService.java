package com.gemiso.zodiac.app.template;

import com.gemiso.zodiac.app.template.dto.TemplateCreateDTO;
import com.gemiso.zodiac.app.template.dto.TemplateDTO;
import com.gemiso.zodiac.app.template.dto.TemplateUpdateDTO;
import com.gemiso.zodiac.app.template.mapper.TemplateCreateMapper;
import com.gemiso.zodiac.app.template.mapper.TemplateMapper;
import com.gemiso.zodiac.app.template.mapper.TemplateUpdateMapper;
import com.gemiso.zodiac.core.service.AuthAddService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TemplateService {

    private final TemplateRepository templateRepository;

    private final TemplateMapper templateMapper;
    private final TemplateCreateMapper templateCreateMapper;
    private final TemplateUpdateMapper templateUpdateMapper;

    private final AuthAddService authAddService;



    public List<TemplateDTO> findAll(String searchWord){

        BooleanBuilder booleanBuilder = getSearch(searchWord);

        List<Template> templateList = (List<Template>) templateRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "inputDtm"));

        List<TemplateDTO> templateDTOList = templateMapper.toDtoList(templateList);

        return templateDTOList;
    }

    public TemplateDTO find(Long tmpltGrpId){

        Template template = templateFindOrFail(tmpltGrpId);

        TemplateDTO templateDTO = templateMapper.toDto(template);

        return templateDTO;

    }

    public Long create(TemplateCreateDTO templateCreateDTO){

        String userId = authAddService.authUser.getUserId();
        templateCreateDTO.setInputrId(userId);//입력자 추가.

        Template template = templateCreateMapper.toEntity(templateCreateDTO);

        templateRepository.save(template);

        return template.getTmpltGrpId();

    }

    public void update(TemplateUpdateDTO templateUpdateDTO, Long tmplGrpId){
        
        Template template = templateFindOrFail(tmplGrpId);

        String userId = authAddService.authUser.getUserId();
        templateUpdateDTO.setUpdtrId(userId);//수정자 추가.

        templateUpdateMapper.updateFromDto(templateUpdateDTO, template);
        templateRepository.save(template);

    }

    public void delete(Long tmplGrpId){

        Template template = templateFindOrFail(tmplGrpId);

        TemplateDTO templateDTO = templateMapper.toDto(template);

        templateDTO.setDelYn("Y");

        templateMapper.updateFromDto(templateDTO, template);
        templateRepository.save(template);

    }

    public Template templateFindOrFail(Long tmpltGrpId){

       /*return  userGroupRepository.findById(userGrpId)
                .orElseThrow(() -> new ResourceNotFoundException("UserGroupId not found. userGroupId : " + userGrpId));*/
        Optional<Template> template = templateRepository.findByTemplate(tmpltGrpId);

        if (!template.isPresent()){
            throw new ResourceNotFoundException("Template group not found. Template group Id : " + tmpltGrpId);
        }

        return template.get();
    }

    private BooleanBuilder getSearch(String searchWord) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QTemplate qTemplate = QTemplate.template;

        booleanBuilder.and(qTemplate.delYn.eq("N"));

        if(!StringUtils.isEmpty(searchWord)){
            booleanBuilder.and(qTemplate.tmpltGrpNm.contains(searchWord));
        }

        return booleanBuilder;
    }
}
