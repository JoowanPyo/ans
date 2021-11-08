package com.gemiso.zodiac.app.symbol;

import com.gemiso.zodiac.app.symbol.dto.SymbolCreateDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolUpdateDTO;
import com.gemiso.zodiac.core.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/symbols")
@Slf4j
@RequiredArgsConstructor
public class SymbolController {

    private final SymbolService symbolService;

    @Operation(summary = "방송아이콘 목록조회", description = "방송아이콘 목록조회")
    @GetMapping(path = "")
    public ApiResponse<List<SymbolDTO>> findAll(@Parameter(name = "useYn", description = "사용여부 (N , Y)")
                                                @RequestParam(value = "useYn", required = false) String useYn,
                                                @Parameter(name = "symbolNm", description = "방송아이콘 아이디")
                                                @RequestParam(value = "symbolNm", required = false) String symbolNm) {

        List<SymbolDTO> symbolDTOS = symbolService.findAll(useYn, symbolNm);

        return new ApiResponse<>(symbolDTOS);

    }

    @Operation(summary = "방송아이콘 상세조회", description = "방송아이콘 상세조회")
    @GetMapping(path = "/{symbolId}")
    public ApiResponse<SymbolDTO> find(@Parameter(name = "symbolId", required = true, description = "필수값<br>")
                                       @PathVariable("symbolId") String symbolId) {

        SymbolDTO symbolDTO = symbolService.find(symbolId);

        return new ApiResponse<>(symbolDTO);
    }

    @Operation(summary = "방송아이콘 등록", description = "방송아이콘 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SymbolDTO> create(@Parameter(name = "symbolCreateDTO", required = true, description = "방송아이콘 아이디")
                                         @Valid @RequestBody SymbolCreateDTO symbolCreateDTO) {

        String symbolId = symbolService.create(symbolCreateDTO);

        SymbolDTO symbolDTO = symbolService.find(symbolId);

        return new ApiResponse<>(symbolDTO);

    }

    @Operation(summary = "방송아이콘 수정", description = "방송아이콘 수정")
    @PutMapping(path = "/{symbolId}")
    public ApiResponse<SymbolDTO> update(@Parameter(name = "symbolCreateDTO", required = true, description = "필수값<br>")
                                         @Valid @RequestBody SymbolUpdateDTO symbolUpdateDTO,
                                         @Parameter(name = "symbolId", required = true, description = "방송아이콘 아이디")
                                         @PathVariable("symbolId") String symbolId) {

        symbolService.update(symbolUpdateDTO, symbolId);

        SymbolDTO symbolDTO = symbolService.find(symbolId);

        return new ApiResponse<>(symbolDTO);

    }

    @Operation(summary = "방송아이콘 삭제", description = "방송아이콘 삭제")
    @DeleteMapping(path = "/{symbolId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> delete(@Parameter(name = "symbolId", required = true, description = "방송아이콘 아이디")
                                 @PathVariable("symbolId") String symbolId){

        symbolService.delete(symbolId);

        return ApiResponse.noContent();
    }

}
