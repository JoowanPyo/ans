package com.gemiso.zodiac.app.dailyProgram;

import com.gemiso.zodiac.app.dailyProgram.dto.DailyProgramCreateDTO;
import com.gemiso.zodiac.app.dailyProgram.dto.DailyProgramDTO;
import com.gemiso.zodiac.app.dailyProgram.mapper.DailyProgramCreateMapper;
import com.gemiso.zodiac.app.dailyProgram.mapper.DailyProgramMapper;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import com.gemiso.zodiac.core.service.UserAuthService;
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
public class DailyProgramService {

    private final DailyProgramRepository dailyProgramRepository;

    private final DailyProgramMapper dailyProgramMapper;
    private final DailyProgramCreateMapper dailyProgramCreateMapper;

    private final UserAuthService userAuthService;


    public DailyProgramDTO find(Long id){

        DailyProgram dailyProgram = dailyProgramFindOrFail(id);

        DailyProgramDTO dailyProgramDTO = dailyProgramMapper.toDto(dailyProgram);

        return dailyProgramDTO;
    }

    public Long create(DailyProgramCreateDTO dailyProgramCreateDTO){

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        dailyProgramCreateDTO.setInputrId(userId);

        DailyProgram dailyProgram = dailyProgramCreateMapper.toEntity(dailyProgramCreateDTO);

        dailyProgramRepository.save(dailyProgram);

        return dailyProgram.getId();

    }

    public DailyProgram dailyProgramFindOrFail(Long id){

        Optional<DailyProgram> dailyProgram = dailyProgramRepository.findById(id);

        if (dailyProgram.isPresent() == false){
            throw new ResourceNotFoundException("큐시트 아이템이 없습니다.");
        }

        return dailyProgram.get();

    }
}
