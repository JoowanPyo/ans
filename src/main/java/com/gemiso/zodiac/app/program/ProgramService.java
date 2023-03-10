package com.gemiso.zodiac.app.program;

import com.gemiso.zodiac.app.program.dto.ProgramCreateDTO;
import com.gemiso.zodiac.app.program.dto.ProgramDTO;
import com.gemiso.zodiac.app.program.dto.ProgramUpdateDTO;
import com.gemiso.zodiac.app.program.mapper.ProgramCrateMapper;
import com.gemiso.zodiac.app.program.mapper.ProgramMapper;
import com.gemiso.zodiac.app.program.mapper.ProgramUpdateMapper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
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
public class ProgramService {

    private final ProgramRepository programRepository;

    private final ProgramMapper programMapper;
    private final ProgramCrateMapper programCrateMapper;
    private final ProgramUpdateMapper programUpdateMapper;

    //private final UserAuthService userAuthService;

    public List<ProgramDTO> findAll(String brdcPgmNm, String useYn) {

        //BooleanBuilder booleanBuilder = getSearch(brdcPgmNm);

        //List<Program> programList = (List<Program>) programRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "inputDtm"));

        List<Program> programList = (List<Program>) programRepository.findByProgram(brdcPgmNm, useYn);

        List<ProgramDTO> programDTOList = programMapper.toDtoList(programList);

        return programDTOList;
    }

    public ProgramDTO find(String brdcPgmId) {

        Program program = programFindOrFail(brdcPgmId);

        ProgramDTO programDTO = programMapper.toDto(program);

        return programDTO;

    }

    public String create(ProgramCreateDTO programCreateDTO, String userId) {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        //String userId = userAuthService.authUser.getUserId();
        programCreateDTO.setInputrId(userId);

        Program program = programCrateMapper.toEntity(programCreateDTO);

        programRepository.save(program);

        return program.getBrdcPgmId();

    }

    public void update(ProgramUpdateDTO programUpdateDTO, String brdcPgmId, String userId) {

        Program program = programFindOrFail(brdcPgmId);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        //String userId = userAuthService.authUser.getUserId();
        programUpdateDTO.setUpdtrId(userId);

        programUpdateMapper.updateFromDto(programUpdateDTO, program);
        programRepository.save(program);

    }

    public void delete(String brdcPgmId, String userId) {

        Program program = programFindOrFail(brdcPgmId);

        ProgramDTO programDTO = programMapper.toDto(program);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        //String userId = userAuthService.authUser.getUserId();
        programDTO.setDelrId(userId);
        programDTO.setDelDtm(new Date());
        programDTO.setDelYn("Y");
        programDTO.setUseYn("N");


        programMapper.updateFromDto(programDTO, program);
        programRepository.save(program);

    }


    public Program programFindOrFail(String brdcPgmId) {

        Optional<Program> program = programRepository.findByProgramId(brdcPgmId);

        if (!program.isPresent()) {
            throw new ResourceNotFoundException("프로그램을 찾을 수 없습니다. 프로그램 아이디 : " + brdcPgmId);
        }
        return program.get();
    }

    private BooleanBuilder getSearch(String brdcPgmNm) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QProgram qProgram = QProgram.program;

        booleanBuilder.and(qProgram.delYn.eq("N"));

        if (brdcPgmNm != null && brdcPgmNm.trim().isEmpty() == false) {
            booleanBuilder.and(qProgram.brdcPgmNm.contains(brdcPgmNm));
        }

        return booleanBuilder;
    }
}
