package com.gemiso.zodiac.app.code;

import com.gemiso.zodiac.app.code.dto.CodeCreateDTO;
import com.gemiso.zodiac.app.code.dto.CodeDTO;
import com.gemiso.zodiac.app.code.dto.CodeUpdateDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
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
    public AnsApiResponse<List<CodeDTO>> findAll(@Parameter(name = "searchWord", description = "검색어")
                                                 @RequestParam(value = "searchWord", required = false) String searchWord,
                                                 @Parameter(name = "useYn", description = "사용 여부")
                                                 @RequestParam(value = "useYn", required = false) String useYn,
                                                 @Parameter(name = "hrnkCdIds", description = "상위코드 아이디(List<Long>)", in = ParameterIn.PATH)
                                                 @RequestParam(value = "hrnkCdIds", required = false) List<String> hrnkCdIds) {

        List<CodeDTO> codeDTOList = codeService.findAll(searchWord, useYn, hrnkCdIds);

        return new AnsApiResponse<>(codeDTOList);
    }

    @Operation(summary = "기사 유형코드 조회", description = "기사 유형코드 조회")
    @GetMapping(path = "/{artclTypCd}")
    public AnsApiResponse<List<CodeDTO>> findArticleType(@Parameter(name = "artclTypCd", description = "기사유형 코드")
                                                         @RequestParam(value = "artclTypCd", required = false) String artclTypCd) {

        List<CodeDTO> codeDTOList = codeService.findArticleType(artclTypCd);

        return new AnsApiResponse<>(codeDTOList);
    }

    @Operation(summary = "코드 상세 조회", description = "코드 상세 조회")
    @GetMapping(path = "/{cdId}")
    public AnsApiResponse<CodeDTO> find(@Parameter(name = "cdId", description = "코드 아이디") @PathVariable Long cdId) {

        CodeDTO codeDTO = codeService.find(cdId);

        return new AnsApiResponse<>(codeDTO);
    }

    @Operation(summary = "코드 등록", description = "코드 등록")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<CodeDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                          @RequestBody CodeCreateDTO codeCreateDTO) {

        Long cdId = codeService.create(codeCreateDTO);

        //코드 등록 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        /*CodeDTO returnCodeDTO = new CodeDTO();
        returnCodeDTO.setCdId(cdId);*/

        CodeDTO codeDTO = codeService.find(cdId);

        return new AnsApiResponse<>(codeDTO);
    }

    @Operation(summary = "코드 수정", description = "코드 수정")
    @PutMapping(path = "/{cdId}")
    public AnsApiResponse<CodeDTO> update(@Parameter(name = "codeUpdateDTO", required = true, description = "필수값<br>")
                                          @Valid @RequestBody CodeUpdateDTO codeUpdateDTO,
                                          @Parameter(name = "cdId", required = true) @PathVariable("cdId") Long cdId) {

        codeService.update(codeUpdateDTO, cdId);

        //코드 수정 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        //CodeDTO codeDTO = new CodeDTO();
        //codeDTO.setCdId(cdId);

        CodeDTO codeDTO = codeService.find(cdId);

        return new AnsApiResponse<>(codeDTO);
    }

    @Operation(summary = "코드 삭제", description = "코드 삭제")
    @DeleteMapping(path = "/{cdId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<CodeDTO> delete(@Parameter(name = "cdId", description = "코드 아이디") @PathVariable("cdId") Long cdId) {

        codeService.delete(cdId);

        return AnsApiResponse.noContent();
    }
}
