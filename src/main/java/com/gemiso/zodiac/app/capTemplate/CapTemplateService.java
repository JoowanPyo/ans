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

        // ????????? ?????? ?????? ??????.
        //CapTemplateGrp capTemplateGrp = capTemplateGrpService.templateFindOrFail(tmpltGrpId);

        //?????????????????? ????????? ????????????????????? ????????? ??????
        //CapTemplateGrpDTO capTemplateGrpDTO = CapTemplateGrpDTO.builder().tmpltGrpId(tmpltGrpId).build();

        //?????????????????? ????????????????????? ????????? set
        //capTemplateCreateDTO.setCapTemplateGrp(capTemplateGrpDTO);

        // ?????? ????????? ????????? ???????????? ???????????? ??????
        //String userId = userAuthService.authUser.getUserId();
        capTemplateCreateDTO.setInputrId(userId); //????????? ??????.
        
        //if (capTemplateCreateDTO.getCapTmpltOrd() == 0){ //Ord?????? ???????????? ?????????????????? max(ord)??? ??????
            Optional<Integer> capTmplOrd = capTemplateRepository.findOrd(); //max Ord??? get
            if (capTmplOrd.isPresent()){
                int setCapTmplOrd = capTmplOrd.get();
                capTemplateCreateDTO.setCapTmpltOrd(setCapTmplOrd); //max Ord??? set
            }else {
                capTemplateCreateDTO.setCapTmpltOrd(1); //Ord??? null?????? set 1
            }
/*
            capTemplate = capTemplateCreateMapper.toEntity(capTemplateCreateDTO);

            capTemplateRepository.save(capTemplate);
        }else { //Ord?????? ???????????? ??????????????????*/

            capTemplate = capTemplateCreateMapper.toEntity(capTemplateCreateDTO);

            capTemplateRepository.save(capTemplate);

            Long capTmplId = capTemplate.getCapTmpltId();

            List<CapTemplate> capTemplateList = capTemplateRepository.findCapList();

            if (!ObjectUtils.isEmpty(capTemplateList)){

                for (int i = 0; i < capTemplateList.size(); i++){
                    if (capTmplId.equals(capTemplateList.get(i).getCapTmpltId())){
                        capTemplateList.remove(i); //?????????????????? CapTmpl??? ??????????????? ??????
                    }
                }

                capTemplateList.add(capTemplate.getCapTmpltOrd(), capTemplate); //???????????? Ord????????? ???????????? ???????????? Set

                int index = 1;
                for (CapTemplate caps : capTemplateList){ //index?????? Ord??? ???????????? ??????????????? Ord??? Set

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

                //?????? ?????????.
                if (orgFileId.equals(newFileId) == false){

                    AttachFile attachFile = AttachFile.builder().fileId(newFileId).build();

                    capTemplate.setAttachFile(attachFile);
                }
            }
        }
        // ?????? ????????? ????????? ???????????? ???????????? ??????
        //String userId = userAuthService.authUser.getUserId();
        capTemplateUpdateDTO.setUpdtrId(userId); //????????? ??????

        capTemplateUpdateMapper.updateFromDto(capTemplateUpdateDTO, capTemplate);
        capTemplateRepository.save(capTemplate); //update

        //List<CapTemplate> capTemplateList = capTemplateRepository.findCapList();

       /* if (!ObjectUtils.isEmpty(capTemplateList)){

            for (int i = 0; i < capTemplateList.size(); i++){
                if (capTmpltId.equals(capTemplateList.get(i).getCapTmpltId())){
                    capTemplateList.remove(i); //?????????????????? CapTmpl??? ??????????????? ??????
                }
            }

            capTemplateList.add(capTemplate.getCapTmpltOrd(), capTemplate); //???????????? Ord????????? ???????????? ???????????? Set

            int index = 1;
            for (CapTemplate caps : capTemplateList){ //index?????? Ord??? ???????????? ??????????????? Ord??? Set

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

            // ?????? ????????? ????????? ???????????? ???????????? ??????
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
        // ?????? ????????? ????????? ???????????? ???????????? ??????
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
                    capTemplateList.remove(i); //?????????????????? CapTmpl??? ??????????????? ??????
                }
            }

            capTemplateList.add(capTemplate.getCapTmpltOrd(), capTemplate); //???????????? Ord????????? ???????????? ???????????? Set

            int index = 1;
            for (CapTemplate caps : capTemplateList){ //index?????? Ord??? ???????????? ??????????????? Ord??? Set

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
            throw new ResourceNotFoundException("?????? ???????????? ?????? ??? ????????????. ?????? ????????? ????????? : " + capTmpltId);
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
