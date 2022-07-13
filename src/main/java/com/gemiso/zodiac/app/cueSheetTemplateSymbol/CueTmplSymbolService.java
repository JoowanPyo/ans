package com.gemiso.zodiac.app.cueSheetTemplateSymbol;

import com.gemiso.zodiac.app.cueSheetItemSymbol.CueSheetItemSymbol;
import com.gemiso.zodiac.app.cueSheetItemSymbol.dto.CueSheetItemSymbolCreateDTO;
import com.gemiso.zodiac.app.cueSheetTemplate.CueSheetTemplate;
import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItem.CueTmpltItem;
import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmpltItemSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplateSymbol.dto.CueTmplSymbolCreateDTO;
import com.gemiso.zodiac.app.cueSheetTemplateSymbol.dto.CueTmplSymbolDTO;
import com.gemiso.zodiac.app.cueSheetTemplateSymbol.dto.CueTmplSymbolSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplateSymbol.dto.CueTmplSymbolUpdateDTO;
import com.gemiso.zodiac.app.cueSheetTemplateSymbol.mapper.CueTmplSymbolCreateMapper;
import com.gemiso.zodiac.app.cueSheetTemplateSymbol.mapper.CueTmplSymbolMapper;
import com.gemiso.zodiac.app.cueSheetTemplateSymbol.mapper.CueTmplSymbolUpdateMapper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CueTmplSymbolService {

    private final CueTmplSymbolRepository cueTmplSymbolRepository;

    private final CueTmplSymbolMapper cueTmplSymbolMapper;
    private final CueTmplSymbolCreateMapper cueTmplSymbolCreateMapper;
    private final CueTmplSymbolUpdateMapper cueTmplSymbolUpdateMapper;



    //큐시트 템플릿 아이템 방송아이콘 목록조회
    public List<CueTmplSymbolDTO> findAll(Long cueTmpltItemId){

        BooleanBuilder booleanBuilder = getSearch(cueTmpltItemId);

        List<CueTmplSymbol> cueTmplSymbolList =
                (List<CueTmplSymbol>) cueTmplSymbolRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.DESC, "ord"));

        List<CueTmplSymbolDTO> cueTmplSymbolDTOList = cueTmplSymbolMapper.toDtoList(cueTmplSymbolList);

        return cueTmplSymbolDTOList;
    }

    //큐시트 템플릿 아이템 방송아이콘 단건조회
    public CueTmplSymbolDTO find(Long id){

        CueTmplSymbol cueTmplSymbol = findCueTmplSymbol(id);

        CueTmplSymbolDTO cueTmplSymbolDTO = cueTmplSymbolMapper.toDto(cueTmplSymbol);

        return cueTmplSymbolDTO;
    }

    //큐시트 템플릿 아이템 방송아이콘 등록 단건
    public CueTmplSymbolSimpleDTO create(Long cueTmpltItemId, CueTmplSymbolCreateDTO cueTmplSymbolCreateDTO){


        findSymbolList(cueTmplSymbolCreateDTO, cueTmpltItemId);

        //큐시트 아이템 방송아이콘에 넣어줄 큐시트아이템 아이디 빌드
        CueTmpltItemSimpleDTO cueTmpltItemSimpleDTO = CueTmpltItemSimpleDTO.builder().cueTmpltItemId(cueTmpltItemId).build();

        cueTmplSymbolCreateDTO.setCueTmpltItem(cueTmpltItemSimpleDTO);//큐시트아이템 아이디 set
        CueTmplSymbol cueTmplSymbol = cueTmplSymbolCreateMapper.toEntity(cueTmplSymbolCreateDTO); //엔티티 변환
        cueTmplSymbolRepository.save(cueTmplSymbol);//등록

        Long id = cueTmplSymbol.getId();

        CueTmplSymbolSimpleDTO cueTmplSymbolSimpleDTO = new CueTmplSymbolSimpleDTO();
        cueTmplSymbolSimpleDTO.setId(id);

        return cueTmplSymbolSimpleDTO;

    }

    public void findSymbolList(CueTmplSymbolCreateDTO cueTmplSymbolCreateDTO, Long cueTmpltItemId){

        //큐시트아이템 방송아이콘 List 조회
        List<CueTmplSymbol> cueSheetTmplSymbol = cueTmplSymbolRepository.findCueTmplSymbol(cueTmpltItemId);

        int newOrd = cueTmplSymbolCreateDTO.getOrd();//새로 등록할 큐시트아이템 방송아이콘 순번 get
        String newTypCd = cueTmplSymbolCreateDTO.getSymbol().getTypCd();// "tv", "radio" new구분코드

        if (ObjectUtils.isEmpty(cueSheetTmplSymbol) == false){
            for (CueTmplSymbol getCueItemSymbol : cueSheetTmplSymbol){
                int orgOrd = getCueItemSymbol.getOrd(); //등록되어 있던 큐시트아이템 방송아이콘 순번 get.
                Long orgId = getCueItemSymbol.getId();//기존아이디 get
                String ordTypCd = getCueItemSymbol.getSymbol().getTypCd();//기본 구분코드 get
                if (orgOrd == newOrd && ordTypCd.equals(newTypCd)){ //새로등록할 큐시트아이템 방송아이콘 순번과,그분타입 이미등록되어있던 방손아이콘 순번이 같으면 에러.
                    cueTmplSymbolRepository.deleteById(orgId); //새로 들어온 순번에 들어가 있던 방송아이콘 삭제.
                }
            }

        }


    }

    //큐시트 템플릿 아이템 방송아이콘 수정
    public CueTmplSymbolSimpleDTO update(Long cueTmpltItemId, CueTmplSymbolUpdateDTO cueTmplSymbolUpdateDTO){

        List<CueTmplSymbol> cueTmplSymbolList = cueTmplSymbolRepository.findCueTmplSymbol(cueTmpltItemId);

        int newOrd = cueTmplSymbolUpdateDTO.getOrd();

        for (CueTmplSymbol cueTmplSymbol : cueTmplSymbolList){

            Long orgId = cueTmplSymbol.getId();
            int orgOrd = cueTmplSymbol.getOrd();

            if (orgOrd == newOrd){
                cueTmplSymbolRepository.deleteById(orgId);
            }
        }

        CueTmpltItemSimpleDTO cueTmpltItem = CueTmpltItemSimpleDTO.builder().cueTmpltItemId(cueTmpltItemId).build();
        cueTmplSymbolUpdateDTO.setCueTmpltItem(cueTmpltItem);
        CueTmplSymbol cueTmplSymbol = cueTmplSymbolUpdateMapper.toEntity(cueTmplSymbolUpdateDTO);

        cueTmplSymbolRepository.save(cueTmplSymbol);

        Long id = cueTmplSymbol.getId();

        CueTmplSymbolSimpleDTO cueTmplSymbolSimpleDTO = new CueTmplSymbolSimpleDTO();
        cueTmplSymbolSimpleDTO.setId(id);

        return cueTmplSymbolSimpleDTO;
    }

    //큐시트 템플릿 아이템 방송아이콘 삭제
    public void delete(Long id){

        CueTmplSymbol cueTmplSymbol = findCueTmplSymbol(id);

        cueTmplSymbolRepository.deleteById(id);

    }

    //큐시트 템플릿 아이템 방송아이콘 단건조회 및 존재유무 확인
    public CueTmplSymbol findCueTmplSymbol(Long id){

        Optional<CueTmplSymbol> cueTmplSymbol = cueTmplSymbolRepository.findByCueTmplSymbol(id);

        if (cueTmplSymbol.isPresent() == false){
            throw new ResourceNotFoundException("큐시트 템플릿 아이템 방송아이콘을 찾울 수 없습니다. 아이디 : "+ id);
        }

        return cueTmplSymbol.get();
    }

    //큐시트 템플릿 아이템 방송아이콘 목록조회 조회조건 빌드
    public BooleanBuilder getSearch(Long cueTmpltItemId){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QCueTmplSymbol qCueTmplSymbol = QCueTmplSymbol.cueTmplSymbol;

        if (ObjectUtils.isEmpty(cueTmpltItemId) == false){
            booleanBuilder.and(qCueTmplSymbol.cueTmpltItem.cueTmpltItemId.eq(cueTmpltItemId));
        }

        return booleanBuilder;
    }

    //큐시트 템플릿 아이템 방송아이콘 등록 List
    public void createList(Long cueTmpltItemId, List<CueTmplSymbolCreateDTO> cueTmplSymbolCreateDTO){

        //해당 큐시트 아이템으로 등록된 큐시트 방송아이콘 리스트를 불러온다.
        List<CueTmplSymbol> cueTmplSymbolList = cueTmplSymbolRepository.findCueTmplSymbol(cueTmpltItemId);

        //큐시트 아이템으로 등록되어 있는 방송아이콘 삭제.
        for (CueTmplSymbol cueTmplSymbol : cueTmplSymbolList){
            Long id = cueTmplSymbol.getId();
            cueTmplSymbolRepository.deleteById(id);
        }

        //큐시트 아이템 방송아이콘에 넣어줄 큐시트아이템 아이디 빌드
        CueTmpltItemSimpleDTO cueTmpltItemSimpleDTO = CueTmpltItemSimpleDTO.builder().cueTmpltItemId(cueTmpltItemId).build();


        for (CueTmplSymbolCreateDTO cueTmplSymbolDTO : cueTmplSymbolCreateDTO){//새로등록할 방송아이콘 등록
            cueTmplSymbolDTO.setCueTmpltItem(cueTmpltItemSimpleDTO);//큐시트아이템 아이디 set
            CueTmplSymbol cueTmplSymbol = cueTmplSymbolCreateMapper.toEntity(cueTmplSymbolDTO); //엔티티 변환
            cueTmplSymbolRepository.save(cueTmplSymbol);//등록
        }
    }

}
