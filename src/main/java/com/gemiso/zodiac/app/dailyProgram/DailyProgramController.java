package com.gemiso.zodiac.app.dailyProgram;


import com.gemiso.zodiac.app.dailyProgram.dto.DailyProgramCreateDTO;
import com.gemiso.zodiac.app.dailyProgram.dto.DailyProgramDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(description = "일일편성 API")
@RestController
@RequestMapping("/daily")
@Slf4j
@RequiredArgsConstructor
public class DailyProgramController {

    private final DailyProgramService dailyProgramService;


    @Operation(summary = "일일편성 상세조회", description = "일일편성 상세조회")
    @GetMapping(path = "/{id}")
    public AnsApiResponse<DailyProgramDTO> find(@Parameter(name = "id", required = true, description = "일일편성 아이디")
                                                 @PathVariable("id") long id){

        DailyProgramDTO dailyProgramDTO = dailyProgramService.find(id);

        return new AnsApiResponse<>(dailyProgramDTO);
    }
    
    @Operation(summary = "일일편성 등록", description = "일일편성 등록")
    @PostMapping(path = "")
    public AnsApiResponse<DailyProgramDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                               @RequestBody @Valid DailyProgramCreateDTO dailyProgramCreateDTO) {

        Long id = dailyProgramService.create(dailyProgramCreateDTO);

        DailyProgramDTO dailyProgramDTO = dailyProgramService.find(id);

        return new AnsApiResponse<>(dailyProgramDTO);
    }
}
