package com.gemiso.zodiac.app.nod;

import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.cueSheet.CueSheetRepository;
import com.gemiso.zodiac.app.cueSheet.CueSheetService;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetDTO;
import com.gemiso.zodiac.app.cueSheet.mapper.CueSheetMapper;
import com.gemiso.zodiac.app.nod.dto.NodCreateDTO;
import com.gemiso.zodiac.app.nod.dto.NodDTO;
import com.gemiso.zodiac.app.nod.mapper.NodCreateMapper;
import com.gemiso.zodiac.app.nod.mapper.NodMapper;
import com.gemiso.zodiac.app.program.Program;
import com.gemiso.zodiac.app.program.ProgramService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class NodService {

    private final NodRepository nodRepository;
    private final CueSheetRepository cueSheetRepository;

    private final NodMapper nodMapper;
    private final CueSheetMapper cueSheetMapper;
    private final NodCreateMapper nodCreateMapper;

    private final ProgramService programService;
    private final CueSheetService cueSheetService;


    public List<CueSheetDTO> findCue(Date nowDate, Integer before, Integer after, String cueDivCd){


        Calendar cal = Calendar.getInstance();
        cal.setTime(nowDate);
        cal.add(Calendar.HOUR, -before);

        //검색시작날짜
        Date sdate = cal.getTime();

        cal.setTime(nowDate);
        cal.add(Calendar.HOUR, after);

        Date edate = cal.getTime();

        List<CueSheet>  cueSheetList = cueSheetRepository.findNodCue(sdate, edate, cueDivCd);

        List<CueSheetDTO> cueSheetDTOList = cueSheetMapper.toDtoList(cueSheetList);


        return cueSheetDTOList;

    }

    public NodDTO find(Long nodId){

        Nod nod = findNod(nodId);

        NodDTO nodDTO = nodMapper.toDto(nod);

        return nodDTO;
    }

    public Long create(NodCreateDTO nodCreateDTO){

        //큐시트 검증
        Long cueId = nodCreateDTO.getCueId();
        cueSheetService.cueSheetFindOrFail(cueId);

        //프로그램검증
        String brdcPgmId = nodCreateDTO.getBrdcPgmId();
        programService.programFindOrFail(brdcPgmId);

        Nod nod = nodCreateMapper.toEntity(nodCreateDTO);

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
