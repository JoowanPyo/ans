package com.gemiso.zodiac.app.nod;

import com.gemiso.zodiac.app.nod.dto.NodDTO;
import com.gemiso.zodiac.app.nod.mapper.NodMapper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class NodService {

    private final NodRepository nodRepository;

    private final NodMapper nodMapper;


    public NodDTO find(Long nodId){

        Nod nod = findNod(nodId);

        NodDTO nodDTO = nodMapper.toDto(nod);

        return nodDTO;
    }

    public Long create(NodDTO nodDTO){

        Nod nod = nodMapper.toEntity(nodDTO);

        nodRepository.save(nod);

        return nod.getNodId();

    }

    public Nod findNod(Long nodId){

        Optional<Nod> nodEntity = nodRepository.findNod(nodId);

        if (nodEntity.isPresent() == false){
            throw new ResourceNotFoundException("NOD를 찾을 수 없습니다. NOD 아이디 "+nodId);
        }

        return nodEntity.get();
    }
}
