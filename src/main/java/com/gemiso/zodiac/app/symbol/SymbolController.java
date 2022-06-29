package com.gemiso.zodiac.app.symbol;

import com.gemiso.zodiac.app.symbol.dto.*;
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

@Api(description = "방송아이콘 API")
@RestController
@RequestMapping("/symbols")
@Slf4j
@RequiredArgsConstructor
public class SymbolController {

    private final SymbolService symbolService;

    @Operation(summary = "방송아이콘 목록조회", description = "방송아이콘 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<SymbolDTO>> findAll(@Parameter(name = "useYn", description = "사용여부 (N , Y)")
                                                   @RequestParam(value = "useYn", required = false) String useYn,
                                                   @Parameter(name = "symbolNm", description = "방송아이콘 아이디")
                                                   @RequestParam(value = "symbolNm", required = false) String symbolNm) {

        List<SymbolDTO> symbolDTOS = symbolService.findAll(useYn, symbolNm);

        return new AnsApiResponse<>(symbolDTOS);

    }

    @Operation(summary = "방송아이콘 상세조회", description = "방송아이콘 상세조회")
    @GetMapping(path = "/{symbolId}")
    public AnsApiResponse<SymbolDTO> find(@Parameter(name = "symbolId", required = true, description = "필수값<br>")
                                          @PathVariable("symbolId") String symbolId) {

        SymbolDTO symbolDTO = symbolService.find(symbolId);

        return new AnsApiResponse<>(symbolDTO);
    }

    @Operation(summary = "방송아이콘 등록", description = "방송아이콘 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<SymbolDTO> create(@Parameter(name = "symbolCreateDTO", required = true, description = "방송아이콘 아이디")
                                            @Valid @RequestBody SymbolCreateDTO symbolCreateDTO) {

        String symbolId = symbolService.create(symbolCreateDTO);

        SymbolDTO symbolDTO = symbolService.find(symbolId);

        return new AnsApiResponse<>(symbolDTO);

    }

    @Operation(summary = "방송아이콘 수정", description = "방송아이콘 수정")
    @PutMapping(path = "/{symbolId}")
    public AnsApiResponse<SymbolDTO> update(@Parameter(name = "symbolCreateDTO", required = true, description = "필수값<br>")
                                            @Valid @RequestBody SymbolUpdateDTO symbolUpdateDTO,
                                            @Parameter(name = "symbolId", required = true, description = "방송아이콘 아이디")
                                            @PathVariable("symbolId") String symbolId) {

        symbolService.update(symbolUpdateDTO, symbolId);

        SymbolDTO symbolDTO = symbolService.find(symbolId);

        return new AnsApiResponse<>(symbolDTO);

    }

    @Operation(summary = "방송아이콘 삭제", description = "방송아이콘 삭제")
    @DeleteMapping(path = "/{symbolId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "symbolId", required = true, description = "방송아이콘 아이디")
                                    @PathVariable("symbolId") String symbolId) {

        symbolService.delete(symbolId);

        return AnsApiResponse.noContent();
    }

    @Operation(summary = "방송 아이콘 순서 변경", description = "큐시트 아이템 순서변경")
    @PutMapping(path = "/{symbolId}/order")
    public AnsApiResponse<SymbolSimpleDTO> ordUpdate(@Parameter(name = "symbolCreateDTO", required = true, description = "필수값<br>")
                                       @Valid @RequestBody SymbolOrdUpdateDTO symbolOrdUpdateDTO,
                                       @Parameter(name = "symbolId", required = true, description = "방송아이콘 아이디")
                                       @PathVariable("symbolId") String symbolId) {

        symbolService.ordupdate(symbolOrdUpdateDTO, symbolId);

        SymbolSimpleDTO symbolSimpleDTO = new SymbolSimpleDTO();
        symbolSimpleDTO.setSymbolId(symbolId);
        symbolSimpleDTO.setTypCd(symbolOrdUpdateDTO.getTypCd());

        return new AnsApiResponse<>(symbolSimpleDTO);
    }

    }
