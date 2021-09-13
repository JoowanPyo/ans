package com.gemiso.zodiac.app.program;

import com.gemiso.zodiac.app.program.dto.ProgramCreateDTO;
import com.gemiso.zodiac.app.program.dto.ProgramDTO;
import com.gemiso.zodiac.app.program.dto.ProgramUpdateDTO;
import com.gemiso.zodiac.app.program.mapper.ProgramCrateMapper;
import com.gemiso.zodiac.app.program.mapper.ProgramMapper;
import com.gemiso.zodiac.app.program.mapper.ProgramUpdateMapper;
import com.gemiso.zodiac.app.user.QUser;
import com.gemiso.zodiac.core.service.AuthAddService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ProgramService {

    private final ProgramRepository programRepository;

    private final ProgramMapper programMapper;
    private final ProgramCrateMapper programCrateMapper;
    private final ProgramUpdateMapper programUpdateMapper;

    private final AuthAddService authAddService;

    public List<ProgramDTO> findAll(String brdcPgmNm) {

        BooleanBuilder booleanBuilder = getSearch(brdcPgmNm);

        List<Program> programList = (List<Program>) programRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "inputDtm"));

        List<ProgramDTO> programDTOList = programMapper.toDtoList(programList);

        return programDTOList;
    }

    public ProgramDTO find(Long brdcPgmId) {

        Program program = programFindOrFail(brdcPgmId);

        ProgramDTO programDTO = programMapper.toDto(program);

        return programDTO;

    }

    public Long create(ProgramCreateDTO programCreateDTO) {

        //작성자 추가.
        String userId = authAddService.authUser.getUserId();
        programCreateDTO.setInputrId(userId);

        Program program = programCrateMapper.toEntity(programCreateDTO);

        programRepository.save(program);

        return program.getBrdcPgmId();

    }

    public void update(ProgramUpdateDTO programUpdateDTO, Long brdcPgmId) {

        Program program = programFindOrFail(brdcPgmId);

        //업데이트 작성자 추가.
        String userId = authAddService.authUser.getUserId();
        programUpdateDTO.setUpdtrId(userId);

        programUpdateMapper.updateFromDto(programUpdateDTO, program);
        programRepository.save(program);

    }

    public void delete(Long brdcPgmId) {

        Program program = programFindOrFail(brdcPgmId);

        ProgramDTO programDTO = programMapper.toDto(program);

        //삭제 정보 추가.
        //삭제자 추가.
        String userId = authAddService.authUser.getUserId();
        programDTO.setDelDtm(new Date());
        programDTO.setDelYn("Y");
        programDTO.setDelrId(userId);

        programMapper.updateFromDto(programDTO, program);
        programRepository.save(program);

    }


    public Program programFindOrFail(Long brdcPgmId) {

        Optional<Program> program = programRepository.findByProgramId(brdcPgmId);

        if (!program.isPresent()) {
            throw new ResourceNotFoundException("Program not found. brdcPgmId : " + brdcPgmId);
        }
        return program.get();
    }

    private BooleanBuilder getSearch(String brdcPgmNm) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QProgram qProgram = QProgram.program;

        booleanBuilder.and(qProgram.delYn.eq("N"));

        if (!StringUtils.isEmpty(brdcPgmNm)) {
            booleanBuilder.and(qProgram.brdcPgmNm.contains(brdcPgmNm));
        }

        return booleanBuilder;
    }
}
