package com.gemiso.zodiac.app.capTemplate;

import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateCreateDTO;
import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateDTO;
import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateOrdUpdateDTO;
import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateUpdateDTO;
import com.gemiso.zodiac.app.capTemplate.mapper.CapTemplateCreateMapper;
import com.gemiso.zodiac.app.capTemplate.mapper.CapTemplateMapper;
import com.gemiso.zodiac.app.capTemplate.mapper.CapTemplateUpdateMapper;
import com.gemiso.zodiac.app.file.AttachFile;
import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CapTemplateService {

    private final CapTemplateRepository capTemplateRepository;

    private final CapTemplateMapper capTemplateMapper;
    private final CapTemplateCreateMapper capTemplateCreateMapper;
    private final CapTemplateUpdateMapper capTemplateUpdateMapper;

    //private final UserAuthService userAuthService;

    //private final CapTemplateGrpService capTemplateGrpService;


    public List<CapTemplateDTO> findAll(Long brdc_pgm_id, String cap_class_cd, String use_yn, String search_word){

        BooleanBuilder booleanBuilder = getSearch(brdc_pgm_id, cap_class_cd, use_yn, search_word);

        List<CapTemplate> capTemplateList = (List<CapTemplate>) capTemplateRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "capTmpltOrd"));

        List<CapTemplateDTO> capTemplateDTOList = capTemplateMapper.toDtoList(capTemplateList);

        return capTemplateDTOList;
    }

    public CapTemplateDTO find(Long capTmpltId){

        CapTemplate capTemplate = capFindOrFail(capTmpltId);

        CapTemplateDTO capTemplateDTO = capTemplateMapper.toDto(capTemplate);

        //CapTemplateGrpDTO capTemplateGrpDTO = capTemplateGrpService.find(capTemplateDTO.getTemplate().getTmpltGrpId());

        //capTemplateDTO.setTemplate(capTemplateGrpDTO);

        return capTemplateDTO;

    }

    public Long create(CapTemplateCreateDTO capTemplateCreateDTO, Long tmpltGrpId, String userId){

        CapTemplate capTemplate = new CapTemplate(); //return Object

        // 템플릿 그룹 유무 확인.
        //CapTemplateGrp capTemplateGrp = capTemplateGrpService.templateFindOrFail(tmpltGrpId);

        //자막템플릿에 넣어줄 자막템플릿그룹 아이디 빌드
        //CapTemplateGrpDTO capTemplateGrpDTO = CapTemplateGrpDTO.builder().tmpltGrpId(tmpltGrpId).build();

        //자막템플릿에 자막템플릿그룹 아이디 set
        //capTemplateCreateDTO.setCapTemplateGrp(capTemplateGrpDTO);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        //String userId = userAuthService.authUser.getUserId();
        capTemplateCreateDTO.setInputrId(userId); //등록자 추가.
        
        //if (capTemplateCreateDTO.getCapTmpltOrd() == 0){ //Ord값이 빈값으로 들어왔을경우 max(ord)값 셋팅
            Optional<Integer> capTmplOrd = capTemplateRepository.findOrd(); //max Ord값 get
            if (capTmplOrd.isPresent()){
                int setCapTmplOrd = capTmplOrd.get();
                capTemplateCreateDTO.setCapTmpltOrd(setCapTmplOrd); //max Ord값 set
            }else {
                capTemplateCreateDTO.setCapTmpltOrd(1); //Ord가 null일때 set 1
            }
/*
            capTemplate = capTemplateCreateMapper.toEntity(capTemplateCreateDTO);

            capTemplateRepository.save(capTemplate);
        }else { //Ord값을 지정하여 들어왔을경우*/

            capTemplate = capTemplateCreateMapper.toEntity(capTemplateCreateDTO);

            capTemplateRepository.save(capTemplate);

            Long capTmplId = capTemplate.getCapTmpltId();

            List<CapTemplate> capTemplateList = capTemplateRepository.findCapList();

            if (!ObjectUtils.isEmpty(capTemplateList)){

                for (int i = 0; i < capTemplateList.size(); i++){
                    if (capTmplId.equals(capTemplateList.get(i).getCapTmpltId())){
                        capTemplateList.remove(i); //신규저장했던 CapTmpl을 리스트에서 삭제
                    }
                }

                capTemplateList.add(capTemplate.getCapTmpltOrd(), capTemplate); //리스트에 Ord값으로 들어왔던 순번으로 Set

                int index = 1;
                for (CapTemplate caps : capTemplateList){ //index값을 Ord에 대입하여 순차적으로 Ord을 Set

                    CapTemplateDTO capTemplateDTO = capTemplateMapper.toDto(caps);
                    capTemplateDTO.setCapTmpltOrd(index);
                    CapTemplate capTemplateEntity = capTemplateMapper.toEntity(capTemplateDTO);
                    capTemplateRepository.save(capTemplateEntity);
                    index++;

                }
            }

        //}


        return capTemplate.getCapTmpltId();

    }

    public void update(CapTemplateUpdateDTO capTemplateUpdateDTO, Long capTmpltId, String userId){

        CapTemplate capTemplate = capFindOrFail(capTmpltId);

        AttachFileDTO newAttachFile = capTemplateUpdateDTO.getAttachFile();
        AttachFile orgAttachFile = capTemplate.getAttachFile();

        if (ObjectUtils.isEmpty(newAttachFile)){
            capTemplate.setAttachFile(null);
        }else {

            Long newFileId = newAttachFile.getFileId();

            if (ObjectUtils.isEmpty(orgAttachFile) == false){

                Long orgFileId = Optional.ofNullable(orgAttachFile.getFileId()).orElse(0L);

                //파일 변경시.
                if (orgFileId.equals(newFileId) == false){

                    AttachFile attachFile = AttachFile.builder().fileId(newFileId).build();

                    capTemplate.setAttachFile(attachFile);
                }
            }
        }
        // 토큰 인증된 사용자 아이디를 입력자로 등록
        //String userId = userAuthService.authUser.getUserId();
        capTemplateUpdateDTO.setUpdtrId(userId); //수정자 추가

        capTemplateUpdateMapper.updateFromDto(capTemplateUpdateDTO, capTemplate);
        capTemplateRepository.save(capTemplate); //update

        //List<CapTemplate> capTemplateList = capTemplateRepository.findCapList();

       /* if (!ObjectUtils.isEmpty(capTemplateList)){

            for (int i = 0; i < capTemplateList.size(); i++){
                if (capTmpltId.equals(capTemplateList.get(i).getCapTmpltId())){
                    capTemplateList.remove(i); //신규저장했던 CapTmpl을 리스트에서 삭제
                }
            }

            capTemplateList.add(capTemplate.getCapTmpltOrd(), capTemplate); //리스트에 Ord값으로 들어왔던 순번으로 Set

            int index = 1;
            for (CapTemplate caps : capTemplateList){ //index값을 Ord에 대입하여 순차적으로 Ord을 Set

                CapTemplateDTO capTemplateDTO = capTemplateMapper.toDto(caps);
                capTemplateDTO.setCapTmpltOrd(index);
                CapTemplate capTemplateEntity = capTemplateMapper.toEntity(capTemplateDTO);
                capTemplateRepository.save(capTemplateEntity);
                index++;

            }
        }*/

    }
    
    public void delete(Long[] capTmpltId, String userId){

        for (Long capTmpltIds : capTmpltId) {

            CapTemplate capTemplate = capFindOrFail(capTmpltIds);

            CapTemplateDTO capTemplateDTO = capTemplateMapper.toDto(capTemplate);

            // 토큰 인증된 사용자 아이디를 입력자로 등록
            //String userId = userAuthService.authUser.getUserId();
            capTemplateDTO.setDelrId(userId);
            capTemplateDTO.setDelDtm(new Date());
            capTemplateDTO.setDelYn("Y");

            capTemplateMapper.updateFromDto(capTemplateDTO, capTemplate);
            capTemplateRepository.save(capTemplate);
        }
        
    }

    public void orderUpdate(CapTemplateOrdUpdateDTO capTemplateOrdUpdateDTO, Long capTmpltId, String userId){

        CapTemplate capTemplate = capFindOrFail(capTmpltId);
        // 토큰 인증된 사용자 아이디를 입력자로 등록
        //String userId = userAuthService.authUser.getUserId();

        CapTemplateDTO capTemplateDTO = capTemplateMapper.toDto(capTemplate);
        capTemplateDTO.setCapTmpltOrd(capTemplateOrdUpdateDTO.getCapTmpltOrd());
        capTemplateDTO.setUpdtrId(userId);

        capTemplateMapper.updateFromDto(capTemplateDTO, capTemplate);

        capTemplateRepository.save(capTemplate);

        List<CapTemplate> capTemplateList = capTemplateRepository.findCapList();

        if (ObjectUtils.isEmpty(capTemplateList) == false){

            for (int i = 0; i < capTemplateList.size(); i++){
                if (capTmpltId.equals(capTemplateList.get(i).getCapTmpltId())){
                    capTemplateList.remove(i); //신규저장했던 CapTmpl을 리스트에서 삭제
                }
            }

            capTemplateList.add(capTemplate.getCapTmpltOrd(), capTemplate); //리스트에 Ord값으로 들어왔던 순번으로 Set

            int index = 1;
            for (CapTemplate caps : capTemplateList){ //index값을 Ord에 대입하여 순차적으로 Ord을 Set

                CapTemplateDTO capTemplateOrdDTO = capTemplateMapper.toDto(caps);
                capTemplateOrdDTO.setCapTmpltOrd(index);
                CapTemplate capTemplateEntity = capTemplateMapper.toEntity(capTemplateOrdDTO);
                capTemplateRepository.save(capTemplateEntity);
                index++;

            }
        }


    }


    public CapTemplate capFindOrFail(Long capTmpltId){

        Optional<CapTemplate> cap = capTemplateRepository.finByCap(capTmpltId);

        if (!cap.isPresent()){
            throw new ResourceNotFoundException("자막 템플릿을 찾을 수 없습니다. 자막 템플릿 아이디 : " + capTmpltId);
        }

        return cap.get();

    }

    private BooleanBuilder getSearch(Long brdcPgmId, String capClassCd, String useYn, String searchWord) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QCapTemplate qCap = QCapTemplate.capTemplate;

        booleanBuilder.and(qCap.delYn.eq("N"));
        /*if(!ObjectUtils.isEmpty(brdc_pgm_id)){
            booleanBuilder.and(qCap.brdcPgmId.eq(brdc_pgm_id));
        }*/
        if(capClassCd != null && capClassCd.trim().isEmpty() == false){
            booleanBuilder.and(qCap.capClassCd.eq(capClassCd));
        }
        if(useYn != null && useYn.trim().isEmpty() == false){
            booleanBuilder.and(qCap.useYn.eq(useYn));
        }
        if(searchWord != null && searchWord.trim().isEmpty() == false){
            booleanBuilder.and(qCap.capTmpltNm.contains(searchWord));
        }

        return booleanBuilder;
    }
}
