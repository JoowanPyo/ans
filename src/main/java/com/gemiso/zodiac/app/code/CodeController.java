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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(description = "코드 API")
@RestController
@RequestMapping("/codes")
@Slf4j
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
                                              @RequestParam(value = "hrnkCdIds", required = false) List<String> hrnkCdIds) {

        List<CodeDTO> codeDTOList = codeService.findAll(searchWord, useYn, hrnkCdIds);

        return new ApiResponse<>(codeDTOList);
    }

    @Operation(summary = "기사 유형코드 조회", description = "기사 유형코드 조회")
    @GetMapping(path = "/articletype")
    public ApiResponse<List<CodeDTO>> findArticleType(@Parameter(name = "artclTypCd", description = "기사유형 코드")
                                                      @RequestParam(value = "artclTypCd", required = false) Long artclTypCd) {

        List<CodeDTO> codeDTOList = codeService.findArticleType(artclTypCd);

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
    public ApiResponse<CodeDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                       @RequestBody CodeCreateDTO codeCreateDTO) {

        CodeDTO returnCodeDTO = codeService.create(codeCreateDTO);

        return new ApiResponse<>(returnCodeDTO);
    }

    @Operation(summary = "코드 수정", description = "코드 수정")
    @PutMapping(path = "/{cdId}")
    public ApiResponse<CodeDTO> update(@Parameter(name = "codeUpdateDTO", required = true, description = "필수값<br>")
                                       @Valid @RequestBody CodeUpdateDTO codeUpdateDTO,
                                       @Parameter(name = "cdId", required = true) @PathVariable("cdId") Long cdId) {

        codeService.update(codeUpdateDTO, cdId);

        CodeDTO codeDTO = codeService.find(cdId);

        return new ApiResponse<>(codeDTO);
    }

    @Operation(summary = "코드 삭제", description = "코드 삭제")
    @DeleteMapping(path = "/{cdId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<CodeDTO> delete(@Parameter(name = "cdId", description = "코드 아이디") @PathVariable("cdId") Long cdId) {

        codeService.delete(cdId);

        return ApiResponse.noContent();
    }
}
