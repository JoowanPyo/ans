package com.gemiso.zodiac.app.rundown;

import com.gemiso.zodiac.app.rundown.dto.RundownCreateDTO;
import com.gemiso.zodiac.app.rundown.dto.RundownDTO;
import com.gemiso.zodiac.app.rundown.dto.RundownUpdateDTO;
import com.gemiso.zodiac.app.rundownItem.dto.RundownItemDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.gemiso.zodiac.core.service.JwtGetUserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Api(description = "런다운 API")
@RestController
@RequestMapping("/rundown")
@Slf4j
@RequiredArgsConstructor
public class RundownController {

    private final RundownService rundownService;

    private final JwtGetUserService jwtGetUserService;


    @Operation(summary = "런다운 목록조회", description = "런다운 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<RundownDTO>> findAll(@Parameter(name = "rundownDt", description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd") Date rundownDt,
                                                    @Parameter(name = "rundownTime", description = "회의시간 ( 오전 /오후 )")
                                                    @RequestParam(value = "rundownTime", required = false) String rundownTime) {

        List<RundownDTO> rundownDTOS = rundownService.findAll(rundownDt, rundownTime);

        return new AnsApiResponse<>(rundownDTOS);
    }

    @Operation(summary = "런다운 상세조회", description = "런다운 상세조회")
    @GetMapping(path = "/{rundownId}")
    public AnsApiResponse<RundownDTO> find(@Parameter(name = "rundownId", description = "런다운 아이디", required = true)
                                           @PathVariable("rundownId") Long rundownId) throws Exception {

        RundownDTO rundownDTO = rundownService.find(rundownId);

        return new AnsApiResponse<>(rundownDTO);

    }

    @Operation(summary = "런다운 등록", description = "런다운 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<RundownDTO> create(@Parameter(name = "rundownCreateDTO", required = true, description = "필수값<br>  , ")
                                             @Valid @RequestBody RundownCreateDTO rundownCreateDTO,
                                             @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        String userId = jwtGetUserService.getUser(Authorization);

        log.info("RUNDOWN CREATE : DTO - " + rundownCreateDTO.toString() + " UserId : " + userId);

        Long rundownId = rundownService.create(rundownCreateDTO, userId);

        RundownDTO rundownDTO = rundownService.find(rundownId);


        return new AnsApiResponse<>(rundownDTO);
    }

    @Operation(summary = "런다운 수정", description = "런다운 수정")
    @PutMapping(path = "/{rundownId}")
    public AnsApiResponse<RundownDTO> update(@Parameter(name = "rundownUpdateDTO", required = true, description = "필수값<br>")
                                             @Valid @RequestBody RundownUpdateDTO rundownUpdateDTO,
                                             @Parameter(name = "rundownId", required = true) @PathVariable("rundownId") Long rundownId,
                                             @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        String userId = jwtGetUserService.getUser(Authorization);

        log.info("RUNDOWN UPDATE : DTO - " + rundownUpdateDTO.toString() + " UserId : " + userId);

        rundownService.update(rundownUpdateDTO, rundownId, userId);

        RundownDTO rundownDTO = rundownService.find(rundownId);

        return new AnsApiResponse<>(rundownDTO);
    }

    @Operation(summary = "런다운 삭제", description = "런다운 삭제")
    @DeleteMapping(path = "/{rundownId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "rundownId", description = "런다운 아이디")
                                    @PathVariable("rundownId") Long rundownId,
                                    @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        String userId = jwtGetUserService.getUser(Authorization);

        log.info("RUNDOWN DELTE : ID - " + rundownId + " UserId : " + userId);

        rundownService.delete(rundownId);

        return AnsApiResponse.noContent();
    }

}
