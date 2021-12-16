package com.gemiso.zodiac.app.baseProgram;

import com.gemiso.zodiac.app.baseProgram.dto.BaseProgramCreateDTO;
import com.gemiso.zodiac.app.baseProgram.dto.BaseProgramDTO;
import com.gemiso.zodiac.app.baseProgram.dto.BaseProgramSimpleDTO;
import com.gemiso.zodiac.app.baseProgram.dto.BaseProgramUpdateDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(description = "기본편성 API")
@RestController
@RequestMapping("/baseprogram")
@Slf4j
@RequiredArgsConstructor
public class BaseProgramController {

    private final BaseProgramService baseProgramService;

    @Operation(summary = "기본편성 목록조회", description = "기본편성 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<BaseProgramDTO>> findAll(@Parameter(name = "basPgmschId", description = "기본편성 아이디")
                                                        @RequestParam(value = "basPgmschId", required = false) String basPgmschId,
                                                        @Parameter(name = "brdcStartDt", description = "방송시작일자")
                                                        @RequestParam(value = "brdcStartDt", required = false) String brdcStartDt,
                                                        @Parameter(name = "brdcEndDt", description = "방송종료일자")
                                                        @RequestParam(value = "brdcEndDt", required = false) String brdcEndDt,
                                                        @Parameter(name = "brdcStartClk", description = "방송시각")
                                                        @RequestParam(value = "brdcStartClk", required = false) String brdcStartClk,
                                                        @Parameter(name = "brdcPgmId", description = "방송프로그램 아이디")
                                                        @RequestParam(value = "brdcPgmId", required = false) String brdcPgmId) {

        List<BaseProgramDTO> baseProgramDTOList = baseProgramService.findAll(basPgmschId, brdcStartDt,
                brdcEndDt, brdcStartClk, brdcPgmId);

        return new AnsApiResponse<>(baseProgramDTOList);
    }

    @Operation(summary = "기본편성 상세조회", description = "기본편성 상세조회")
    @GetMapping(path = "/{basePgmschId}")
    public AnsApiResponse<BaseProgramDTO> find(@Parameter(name = "basePgmschId", required = true, description = "기본편성 아이디")
                                               @PathVariable("basePgmschId") String basePgmschId) {

        BaseProgramDTO baseProgramDTO = baseProgramService.find(basePgmschId);

        return new AnsApiResponse<>(baseProgramDTO);
    }

    @Operation(summary = "기본편성 등록", description = "기본편성 등록")
    @PostMapping(name = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<BaseProgramSimpleDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                                       @RequestBody @Valid BaseProgramCreateDTO baseProgramCreateDTO) {

        BaseProgramSimpleDTO baseProgramSimpleDTO = baseProgramService.create(baseProgramCreateDTO);

        return new AnsApiResponse<>(baseProgramSimpleDTO);
    }

    @Operation(summary = "기본편성 수정", description = "기본편성 수정")
    @PutMapping(path = "/{basePgmschId}")
    public AnsApiResponse<BaseProgramSimpleDTO> update(@Parameter(description = "필수값<br> ", required = true)
                                                       @RequestBody @Valid BaseProgramUpdateDTO baseProgramUpdateDTO,
                                                       @Parameter(name = "basePgmschId", required = true, description = "기본편성 아이디")
                                                       @PathVariable("basePgmschId") String basePgmschId) {

        BaseProgramSimpleDTO baseProgramSimpleDTO = baseProgramService.update(baseProgramUpdateDTO, basePgmschId);

        return new AnsApiResponse<>(baseProgramSimpleDTO);
    }

    @Operation(summary = "기본편성 삭제", description = "기본편성 삭제")
    @DeleteMapping(name = "/{basePgmschId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "basePgmschId", required = true, description = "기본편성 아이디")
                                    @PathVariable("basePgmschId") String basePgmschId) {

        baseProgramService.delete(basePgmschId);

        return AnsApiResponse.noContent();
    }
}
