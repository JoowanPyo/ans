package com.gemiso.zodiac.app.program;

import com.gemiso.zodiac.app.program.dto.ProgramCreateDTO;
import com.gemiso.zodiac.app.program.dto.ProgramDTO;
import com.gemiso.zodiac.app.program.dto.ProgramUpdateDTO;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupUpdateDTO;
import com.gemiso.zodiac.app.yonhap.dto.YonhapDTO;
import com.gemiso.zodiac.core.helper.JWTParser;
import com.gemiso.zodiac.core.response.ApiErrorResponse;
import com.gemiso.zodiac.core.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/programs")
@Log4j2
@RequiredArgsConstructor
@Api(description = "프로그램 API")
public class ProgramController {

    private final ProgramService programService;

    @Operation(summary = "프로그램 목록조회", description = "프로그램 목록조회")
    @GetMapping(path = "")
    public ApiResponse<List<ProgramDTO>> findAll(@Parameter(name = "brdcPgmNm", description = "방송프로그램 명")
                                  @RequestParam(value = "brdcPgmNm", required = false) String brdcPgmNm) throws Exception {


        List<ProgramDTO> programDTOList = programService.findAll(brdcPgmNm);


        return new ApiResponse<>(programDTOList);
    }

    @Operation(summary = "프로그램 상세조회", description = "프로그램 상세조회")
    @GetMapping(path = "/{brdcPgmId}")
    public ApiResponse<ProgramDTO> find(@Parameter(name = "brdcPgmId", description = "방송 프로그램 아이디", required = true)
                               @PathVariable("brdcPgmId") Long brdcPgmId) throws Exception {

        ProgramDTO programDTO = programService.find(brdcPgmId);

        return new ApiResponse<>(programDTO, HttpStatus.OK);

    }

    @Operation(summary = "프로그램 등록", description = "프로그램 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProgramDTO> create(@Parameter(name = "programCreateDTO", required = true, description = "필수값<br>  , ")
                                 @Valid @RequestBody ProgramCreateDTO programCreateDTO) throws Exception {

        Long programId = programService.create(programCreateDTO);

        ProgramDTO programDTO = programService.find(programId);


        return new ApiResponse<>(programDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "프로그램 수정", description = "프로그램 수정")
    @PutMapping(path = "/{brdcPgmId}")
    public ApiResponse<ProgramDTO> update(@Parameter(name = "programUpdateDTO", required = true, description = "필수값<br>")
                                 @Valid @RequestBody ProgramUpdateDTO programUpdateDTO,
                                 @Parameter(name = "brdcPgmId", required = true) @PathVariable("brdcPgmId") Long brdcPgmId) throws Exception {

        programService.update(programUpdateDTO, brdcPgmId);

        ProgramDTO programDTO = programService.find(brdcPgmId);

        return new ApiResponse<>(programDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "프로그램 삭제", description = "프로그램 삭제")
    @DeleteMapping(path = "/{brdcPgmId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> delete(@Parameter(name = "brdcPgmId", description = "방송프로그램 아이디")
                                 @PathVariable("brdcPgmId") Long brdcPgmId) throws Exception {

        programService.delete(brdcPgmId);


        return ApiResponse.noContent();
    }

}
