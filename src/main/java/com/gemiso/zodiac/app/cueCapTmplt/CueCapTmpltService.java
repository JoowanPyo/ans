package com.gemiso.zodiac.app.cueCapTmplt;

import com.gemiso.zodiac.app.capTemplate.CapTemplate;
import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateSimpleDTO;
import com.gemiso.zodiac.app.cueCapTmplt.dto.CueCapTmpltCreateDTO;
import com.gemiso.zodiac.app.cueCapTmplt.dto.CueCapTmpltDTO;
import com.gemiso.zodiac.app.cueCapTmplt.dto.CueCapTmpltUpdateDTO;
import com.gemiso.zodiac.app.cueCapTmplt.mapper.CueCapTmpltCreateMapper;
import com.gemiso.zodiac.app.cueCapTmplt.mapper.CueCapTmpltMapper;
import com.gemiso.zodiac.app.cueCapTmplt.mapper.CueCapTmpltUpdateMapper;
import com.gemiso.zodiac.app.cueSheetItemCap.mapper.CueSheetItemCapUpdateMapper;
import com.gemiso.zodiac.app.program.dto.ProgramSimpleDTO;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CueCapTmpltService {

    private final CueCapTmpltRepository cueCapTmpltRepository;

    private final CueCapTmpltMapper cueCapTmpltMapper;
    private final CueCapTmpltCreateMapper cueCapTmpltCreateMapper;
    private final CueCapTmpltUpdateMapper cueCapTmpltUpdateMapper;


    public List<CueCapTmpltDTO> findAll(String brdcPgmId){ //큐시트 자막 템플릿 목록 조회

        BooleanBuilder booleanBuilder = getSearch(brdcPgmId); //조회조건 빌드

        //생성된 조회조건으로 목록조회.
        List<CueCapTmplt> cueCapTmpltList = (List<CueCapTmplt>) cueCapTmpltRepository.findAll(booleanBuilder);

        //리턴시켜줄 큐시트 자막 템플릿 목록 DTO변환
        List<CueCapTmpltDTO> cueCapTmpltDTOList = cueCapTmpltMapper.toDtoList(cueCapTmpltList);

        return cueCapTmpltDTOList;
    }

    public CueCapTmpltDTO find(Long id){ //큐시트 자막 템플릿 단건 조회

        CueCapTmplt cueCapTmplt = findCueCapTmplt(id); //큐시트 자막 템플릿 단건 조회 및 존재유무 확인.

        //리턴 보낼 큐시트 자막 템플릿 DTO변환
        CueCapTmpltDTO cueCapTmpltDTO = cueCapTmpltMapper.toDto(cueCapTmplt);

        return cueCapTmpltDTO;
    }

    public CueCapTmpltDTO create(List<CueCapTmpltCreateDTO> cueCapTmpltCreateDTOList, String brdcPgmId){

        //기존에 등록되어 있던 큐시트 자막 템플릿을 List get
        List<CueCapTmplt> cueCapTmpltList = cueCapTmpltRepository.findCueCapTmpltList(brdcPgmId);

        //조회된 기존에 등록되어 있던 리스트 삭제.
        for (CueCapTmplt cueCapTmplt : cueCapTmpltList){
            Long id = cueCapTmplt.getId();
            cueCapTmpltRepository.deleteById(id);
        }

        //큐시트 자막 템플릿에 넣어줄 프로그램아이디 빌드
        ProgramSimpleDTO programSimpleDTO = ProgramSimpleDTO.builder().brdcPgmId(brdcPgmId).build();

        //큐시트 자막 템플릿 재등록
        for (CueCapTmpltCreateDTO cueCapTmpltCreateDTO : cueCapTmpltCreateDTOList){
            
            cueCapTmpltCreateDTO.setProgram(programSimpleDTO);

            CueCapTmplt cueCapTmpltEntity = cueCapTmpltCreateMapper.toEntity(cueCapTmpltCreateDTO);

            cueCapTmpltRepository.save(cueCapTmpltEntity);
        }

        CueCapTmpltDTO cueCapTmpltDTO = new CueCapTmpltDTO();
        cueCapTmpltDTO.setProgram(programSimpleDTO);

        return cueCapTmpltDTO;

    }

    public CueCapTmpltDTO update(CueCapTmpltUpdateDTO cueCapTmpltUpdateDTO, Long id){

        CueCapTmplt cueCapTmplt = findCueCapTmplt(id); //큐시트 자막 템플릿 단건 조회 및 존재유무 확인.

        //자막 템플릿은 PK로 연관 관계 연결이 되어 있기 때문에 새로운 자막템플릿으로 교체시 조회된 엔티티에서 삭제 후 set
        CapTemplateSimpleDTO capTemplateSimpleDTO = cueCapTmpltUpdateDTO.getCapTemplate();
        if (ObjectUtils.isEmpty(capTemplateSimpleDTO) == false){
            cueCapTmplt.setCapTemplate(null);
        }

        //조회된 엔티티에 Update로 들어온 데이터 set
        cueCapTmpltUpdateMapper.updateFromDto(cueCapTmpltUpdateDTO, cueCapTmplt);
        cueCapTmpltRepository.save(cueCapTmplt); //수정

        CueCapTmpltDTO cueCapTmpltDTO = cueCapTmpltMapper.toDto(cueCapTmplt); //조회하여 업데이트된 엔티티 데이터 DTO 변환 후 리턴.

        return cueCapTmpltDTO;
    }

    public void delete(Long id){

        CueCapTmplt cueCapTmplt = findCueCapTmplt(id); //큐시트 자막 템플릿 단건 조회 및 존재유무 확인.

        cueCapTmpltRepository.deleteById(id);

    }

    public CueCapTmplt findCueCapTmplt(Long id){ //큐시트 자막 템플릿 단건조회 및 존재유무 확인.

        Optional<CueCapTmplt> cueCapTmplt = cueCapTmpltRepository.findCueCapTmplt(id);

        if (cueCapTmplt.isPresent() == false){ //큐시트 자막 템플릿 아이디로 조회 건수가 없으면 에러.
            throw new ResourceNotFoundException("큐시트 자막 템플릿을 찾을 수 없습니다. 큐시트 자막 템플릿 아이디 : "+id);
        }

        return cueCapTmplt.get(); //조회된 큐시트 자막 템플릿 리턴.
    }

    public BooleanBuilder getSearch(String brdcPgmId){ //조회조건 빌드

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QCueCapTmplt qCueCapTmplt = QCueCapTmplt.cueCapTmplt;

        if (ObjectUtils.isEmpty(brdcPgmId) == false){ //방송프로그램 아이디 조회조건 검색.
            booleanBuilder.and(qCueCapTmplt.program.brdcPgmId.eq(brdcPgmId));
        }

        return booleanBuilder;
    }

}
