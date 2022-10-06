package com.gemiso.zodiac.app.rundown;

import com.gemiso.zodiac.app.rundown.dto.RundownCreateDTO;
import com.gemiso.zodiac.app.rundown.dto.RundownDTO;
import com.gemiso.zodiac.app.rundown.dto.RundownUpdateDTO;
import com.gemiso.zodiac.app.rundown.mapper.RundownCreateMapper;
import com.gemiso.zodiac.app.rundown.mapper.RundownMapper;
import com.gemiso.zodiac.app.rundown.mapper.RundownUpdateMapper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RundownService {

    private final RundownRepository rundownRepository;

    private final RundownMapper rundownMapper;
    private final RundownCreateMapper rundownCreateMapper;
    private final RundownUpdateMapper rundownUpdateMapper;


    //런다운 목록조회
    public List<RundownDTO> findAll(Date rundownDt, String rundownTime){

        List<Rundown> rundowns = rundownRepository.findRundowns(rundownDt, rundownTime);

        List<RundownDTO> rundownDTOS = rundownMapper.toDtoList(rundowns);

        return rundownDTOS;

    }

    //런다운 상세 조회
    public RundownDTO find(Long rundownId){

        Rundown rundown = rundownFindOrFail(rundownId);

        RundownDTO rundownDTO = rundownMapper.toDto(rundown);

        return rundownDTO;
    }

    //런다운 등록
    public Long create(RundownCreateDTO rundownCreateDTO, String userId){

        rundownCreateDTO.setInputrId(userId);

        Rundown rundown = rundownCreateMapper.toEntity(rundownCreateDTO);

        rundownRepository.save(rundown);

        return rundown.getRundownId();

    }

    //런다운 업데이트
    public void update(RundownUpdateDTO rundownUpdateDTO, Long rundownId, String userId){

        Rundown rundown = rundownFindOrFail(rundownId);

        rundownUpdateDTO.setUpdtrId(userId);

        rundownUpdateMapper.updateFromDto(rundownUpdateDTO, rundown);

        rundownRepository.save(rundown);


    }

    //런다운 삭제
    public void delete(Long rundownId){

        Rundown rundown = rundownFindOrFail(rundownId);

        rundownRepository.deleteById(rundownId);

    }

    //런다운 조회 및 존재유무 확인
    public Rundown rundownFindOrFail(Long rundownId){

        Optional<Rundown> rundownEntity = rundownRepository.findRundown(rundownId);

        if (rundownEntity.isPresent() == false){
            throw new ResourceNotFoundException("런다운을 찾을 수 없습니다. 런다운 아이디 : "+rundownId);
        }

        return rundownEntity.get();
    }

}