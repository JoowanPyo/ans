package com.gemiso.zodiac.app.code;

import com.gemiso.zodiac.app.code.dto.CodeCreateDTO;
import com.gemiso.zodiac.app.code.dto.CodeDTO;
import com.gemiso.zodiac.app.code.dto.CodeUpdateDTO;
import com.gemiso.zodiac.core.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(description = "코드 API")
@RestController
@RequestMapping("/codes")
@Log4j2
@RequiredArgsConstructor
public class CodeController {

    private final CodeService codeService;

    @Operation(summary = "코드 목록조회", description = "코드 목록조회")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<CodeDTO>> findAll(@Parameter(name = "searchWord", description = "검색어")
                                              @RequestParam(value = "searchWord", required = false) String searchWord,
                                              @Parameter(name = "useYn", description = "사용 여부")
                                              @RequestParam(value = "useYn", required = false) String useYn,
                                              @Parameter(name = "hrnkCdIds", description = "상위코드 아이디(List<Long>)", in = ParameterIn.PATH)
                                              @RequestParam(value = "hrnkCdIds", required = false) List<Long> hrnkCdIds) {

        List<CodeDTO> codeDTOList = codeService.findAll(searchWord, useYn, hrnkCdIds);

        return new ApiResponse<>(codeDTOList);
    }

    @Operation(summary = "코드 상세 조회", description = "코드 상세 조회")
    @GetMapping(path = "/{cdId}")
    public ApiResponse<CodeDTO> find(@Parameter(name = "cdId", description = "코드 아이디") @PathVariable Long cdId) {

        CodeDTO codeDTO = codeService.find(cdId);

        return new ApiResponse<>(codeDTO);
    }

    @Operation(summary = "코드 등록", description = "코드 등록")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CodeDTO> create(@Parameter(description = "필수값<br> ", required = true) @RequestBody CodeCreateDTO codeCreateDTO,
                                       @RequestHeader(value = "authorization", required = false) String authorization) throws Exception {

        CodeDTO returnCodeDTO = codeService.create(codeCreateDTO, authorization);

        return new ApiResponse<>(returnCodeDTO);
    }

    @Operation(summary = "코드 수정", description = "코드 수정")
    @PutMapping(path = "/{cdId}")
    public ApiResponse<CodeDTO> update(@Parameter(name = "codeUpdateDTO", required = true, description = "필수값<br>")@Valid @RequestBody CodeUpdateDTO codeUpdateDTO,
                                       @Parameter(name = "cdId", required = true) @PathVariable("cdId") Long cdId,
                                       @RequestHeader(value = "authorization", required = false) String authorization) throws Exception {

        codeService.update(codeUpdateDTO, cdId, authorization);

        CodeDTO codeDTO = codeService.find(cdId);

        return new ApiResponse<>(codeDTO);
    }

    @Operation(summary = "코드 삭제", description = "코드 삭제")
    @DeleteMapping(path = "/{cdId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<CodeDTO> delete(@Parameter(name = "cdId", description = "코드 아이디") @PathVariable("userId") Long cdId,
                                       @RequestHeader(value = "authorization", required = false) String authorization) throws Exception {

        codeService.delete(cdId, authorization);

        return ApiResponse.noContent();
    }
}
