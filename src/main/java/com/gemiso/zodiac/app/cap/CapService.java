package com.gemiso.zodiac.app.cap;

import com.gemiso.zodiac.app.cap.dto.CapCreateDTO;
import com.gemiso.zodiac.app.cap.dto.CapDTO;
import com.gemiso.zodiac.app.cap.dto.CapUpdateDTO;
import com.gemiso.zodiac.app.cap.mapper.CapCreateMapper;
import com.gemiso.zodiac.app.cap.mapper.CapMapper;
import com.gemiso.zodiac.app.cap.mapper.CapUpdateMapper;
import com.gemiso.zodiac.app.template.TemplateService;
import com.gemiso.zodiac.app.template.dto.TemplateDTO;
import com.gemiso.zodiac.core.service.AuthAddService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CapService {

    private final CapRepository capRepository;

    private final CapMapper capMapper;
    private final CapCreateMapper capCreateMapper;
    private final CapUpdateMapper capUpdateMapper;

    private final AuthAddService authAddService;

    private final TemplateService templateService;


    public List<CapDTO> findAll(Long brdc_pgm_id, String cap_class_cd, String use_yn, String search_word){

        BooleanBuilder booleanBuilder = getSearch(brdc_pgm_id, cap_class_cd, use_yn, search_word);

        List<Cap> capList = (List<Cap>) capRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "capTmpltOrd"));

        List<CapDTO> capDTOList = capMapper.toDtoList(capList);

        return capDTOList;
    }

    public CapDTO find(Long capTmpltId){

        Cap cap = capFindOrFail(capTmpltId);

        CapDTO capDTO = capMapper.toDto(cap);

        TemplateDTO templateDTO = templateService.find(capDTO.getTemplate().getTmpltGrpId());

        capDTO.setTemplate(templateDTO);

        return capDTO;

    }

    public Long create(CapCreateDTO capCreateDTO){

        Cap cap = new Cap();

        String userId = authAddService.authUser.getUserId();
        capCreateDTO.setInputrId(userId); //등록자 추가.
        
        if (capCreateDTO.getCapTmpltOrd() == 0){ //Ord값이 빈값으로 들어왔을경우 max(ord)값 셋팅
            Optional<Integer> capTmplOrd = capRepository.findOrd(); //max Ord값 get
            if (capTmplOrd.isPresent()){
                int setCapTmplOrd = capTmplOrd.get();
                capCreateDTO.setCapTmpltOrd(setCapTmplOrd); //max Ord값 set
            }else {
                capCreateDTO.setCapTmpltOrd(1); //Ord가 null일때 set 1
            }

            cap = capCreateMapper.toEntity(capCreateDTO);

            capRepository.save(cap);
        }else { //Ord값을 지정하여 들어왔을경우

            cap = capCreateMapper.toEntity(capCreateDTO);

            capRepository.save(cap);

            Long capTmplId = cap.getCapTmpltId();

            List<Cap> capList = capRepository.findCapList();

            if (!ObjectUtils.isEmpty(capList)){

                for (int i = 0; i < capList.size(); i++){
                    if (capTmplId.equals(capList.get(i).getCapTmpltId())){
                        capList.remove(i); //신규저장했던 CapTmpl을 리스트에서 삭제
                    }
                }

                capList.add(cap.getCapTmpltOrd(), cap); //리스트에 Ord값으로 들어왔던 순번으로 Set

                int index = 1;
                for (Cap caps : capList){ //index값을 Ord에 대입하여 순차적으로 Ord을 Set

                    CapDTO capDTO = capMapper.toDto(caps);
                    capDTO.setCapTmpltOrd(index);
                    Cap capEntity = capMapper.toEntity(capDTO);
                    capRepository.save(capEntity);
                    index++;

                }
            }

        }


        return cap.getCapTmpltId();

    }

    public void update(CapUpdateDTO capUpdateDTO, Long capTmpltId){

        Cap cap = capFindOrFail(capTmpltId);

        String userId = authAddService.authUser.getUserId();
        capUpdateDTO.setUpdtrId(userId); //수정자 추가

        capUpdateMapper.updateFromDto(capUpdateDTO, cap);
        capRepository.save(cap); //update

        List<Cap> capList = capRepository.findCapList();

        if (!ObjectUtils.isEmpty(capList)){

            for (int i = 0; i < capList.size(); i++){
                if (capTmpltId.equals(capList.get(i).getCapTmpltId())){
                    capList.remove(i); //신규저장했던 CapTmpl을 리스트에서 삭제
                }
            }

            capList.add(cap.getCapTmpltOrd(), cap); //리스트에 Ord값으로 들어왔던 순번으로 Set

            int index = 1;
            for (Cap caps : capList){ //index값을 Ord에 대입하여 순차적으로 Ord을 Set

                CapDTO capDTO = capMapper.toDto(caps);
                capDTO.setCapTmpltOrd(index);
                Cap capEntity = capMapper.toEntity(capDTO);
                capRepository.save(capEntity);
                index++;

            }
        }

    }
    
    public void delete(Long[] capTmpltId){

        for (Long capTmpltIds : capTmpltId) {

            Cap cap = capFindOrFail(capTmpltIds);

            CapDTO capDTO = capMapper.toDto(cap);

            String userId = authAddService.authUser.getUserId();
            capDTO.setDelrId(userId);
            capDTO.setDelDtm(new Date());
            capDTO.setDelYn("Y");

            capMapper.updateFromDto(capDTO, cap);
            capRepository.save(cap);
        }
        
    }


    public Cap capFindOrFail(Long capTmpltId){

        Optional<Cap> cap = capRepository.finByCap(capTmpltId);

        if (!cap.isPresent()){
            throw new ResourceNotFoundException("Cap template not found. Cap template Id : " + capTmpltId);
        }

        return cap.get();

    }

    private BooleanBuilder getSearch(Long brdc_pgm_id, String cap_class_cd, String use_yn, String search_word) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QCap qCap = QCap.cap;

        if(!ObjectUtils.isEmpty(brdc_pgm_id)){
            booleanBuilder.and(qCap.brdcPgmId.eq(brdc_pgm_id));
        }
        if(!StringUtils.isEmpty(cap_class_cd)){
            booleanBuilder.and(qCap.capClassCd.eq(cap_class_cd));
        }
        if(!StringUtils.isEmpty(use_yn)){
            booleanBuilder.and(qCap.useYn.eq(use_yn));
        }
        if(!StringUtils.isEmpty(search_word)){
            booleanBuilder.and(qCap.capTmpltNm.contains(search_word));
        }

        return booleanBuilder;
    }
}
