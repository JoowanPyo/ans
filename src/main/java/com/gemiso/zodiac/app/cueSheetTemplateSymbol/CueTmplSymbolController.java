package com.gemiso.zodiac.app.cueSheetTemplateSymbol;

import com.gemiso.zodiac.app.cueSheetTemplateSymbol.dto.CueTmplSymbolCreateDTO;
import com.gemiso.zodiac.app.cueSheetTemplateSymbol.dto.CueTmplSymbolDTO;
import com.gemiso.zodiac.core.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(description = "큐시트 템플릿 방송아이콘 API")
@RestController
@RequestMapping("/cuetmplsymbol")
@Slf4j
@RequiredArgsConstructor
public class CueTmplSymbolController {

    private final CueTmplSymbolService cueTmplSymbolService;


    @Operation(summary = "큐시트 템플릿 방송아이콘 등록", description = "큐시트 템플릿 방송아이콘 등록")
    @PostMapping(path = "/{cueTmpltId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CueTmplSymbolDTO> create(@Parameter(name = "cueTmpltId", required = true, description = "큐시트 템플릿 아이디")
                                                         @PathVariable("cueTmpltId") Long cueTmpltId,
                                                @Parameter(description = "필수값<br> ", required = true)
                                                         @RequestBody @Valid List<CueTmplSymbolCreateDTO> cueTmplSymbolCreateDTO) {

        cueTmplSymbolService.create(cueTmpltId, cueTmplSymbolCreateDTO);

        return null;
    }
}
