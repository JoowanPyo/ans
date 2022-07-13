package com.gemiso.zodiac.app.cueSheetItemSymbol;

import com.gemiso.zodiac.app.cueSheetItemSymbol.dto.CueSheetItemSymbolCreateDTO;
import com.gemiso.zodiac.app.cueSheetItemSymbol.dto.CueSheetItemSymbolDTO;
import com.gemiso.zodiac.app.cueSheetItemSymbol.dto.CueSheetItemSymbolResponseDTO;
import com.gemiso.zodiac.app.cueSheetItemSymbol.dto.CueSheetItemSymbolUpdateDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(description = "큐시트 아이템 심볼 API")
@RestController
@RequestMapping("/cuesheetitemsymbols")
@RequiredArgsConstructor
@Slf4j
public class CueSheetItemSymbolController {

    private final CueSheetItemSymbolService cueSheetItemSymbolService;


    @Operation(summary = "큐시트 아이템 심볼 상세조회", description = "큐시트 아이템 심볼 상세조회")
    @GetMapping(path = "/{cueItemSymbolId}")
    public AnsApiResponse<CueSheetItemSymbolDTO> find(@Parameter(name = "cueItemSymbolId", description = "큐시트아이템 아이디")
                                                      @PathVariable("cueItemSymbolId") Long cueItemSymbolId) {

        CueSheetItemSymbolDTO cueSheetItemSymbolDTO = cueSheetItemSymbolService.find(cueItemSymbolId);

        return new AnsApiResponse<CueSheetItemSymbolDTO>(cueSheetItemSymbolDTO);
    }

    @Operation(summary = "큐시트 아이템 심볼 등록", description = "큐시트 아이템 심볼 등록")
    @PostMapping(path = "/{cueItemId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<CueSheetItemSymbolResponseDTO> create(@Parameter(name = "cueItemId", description = "큐시트아이템 아이디")
                                                                @PathVariable("cueItemId") Long cueItemId,
                                                                @Parameter(description = "필수값<br> ", required = true)
                                                                @RequestBody @Valid CueSheetItemSymbolCreateDTO cueSheetItemSymbolCreateDTO) {

        log.info("CueSheet Item Symbol Create : CueItemId - "+cueItemId + " DTO - "+ cueSheetItemSymbolCreateDTO.toString() );

        CueSheetItemSymbolResponseDTO cueSheetItemSymbolDTO = new CueSheetItemSymbolResponseDTO();

        Long id = cueSheetItemSymbolService.create(cueSheetItemSymbolCreateDTO, cueItemId);

        cueSheetItemSymbolDTO.setCueItemSymbolId(id);
        /*  CueSheetItemSymbolDTO cueSheetItemSymbolDTO = cueSheetItemSymbolService.find(id);*/

        return new AnsApiResponse<CueSheetItemSymbolResponseDTO>(cueSheetItemSymbolDTO);
    }

    @Operation(summary = "큐시트 아이템 심볼 수정", description = "큐시트 아이템 심볼 수정")
    @PutMapping(path = "/{cueItemId}")
    public AnsApiResponse<CueSheetItemSymbolResponseDTO> update(@Parameter(name = "cueItemId", description = "큐시트아이템 아이디")
                                                                @PathVariable("cueItemId") Long cueItemId,
                                                                @Parameter(description = "필수값<br> ", required = true)
                                                                @RequestBody @Valid CueSheetItemSymbolUpdateDTO cueSheetItemSymbolUpdateDTO) {

        CueSheetItemSymbolResponseDTO cueSheetItemSymbolDTO = new CueSheetItemSymbolResponseDTO();

        Long id = cueSheetItemSymbolService.update(cueSheetItemSymbolUpdateDTO, cueItemId);

        cueSheetItemSymbolDTO.setCueItemSymbolId(id);
        /*CueSheetItemSymbolDTO cueSheetItemSymbolDTO = cueSheetItemSymbolService.find(cueItemId);*/

        return new AnsApiResponse<CueSheetItemSymbolResponseDTO>(cueSheetItemSymbolDTO);
    }

    @Operation(summary = "큐시트 아이템 심볼 삭제", description = "큐시트 아이템 심볼 삭제")
    @DeleteMapping(path = "/{cueItemSymbolId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "cueItemSymbolId", description = "큐시트아이템 방송아이콘 아이디")
                                    @PathVariable("cueItemSymbolId") Long cueItemSymbolId) {

        cueSheetItemSymbolService.delete(cueItemSymbolId);

        return AnsApiResponse.noContent();
    }
}
