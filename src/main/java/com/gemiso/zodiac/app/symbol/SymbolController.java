package com.gemiso.zodiac.app.symbol;

import com.gemiso.zodiac.app.symbol.dto.*;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.gemiso.zodiac.core.service.JwtGetUserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Api(description = "방송아이콘 API")
@RestController
@RequestMapping("/symbols")
@Slf4j
@RequiredArgsConstructor
public class SymbolController {

    private final SymbolService symbolService;

    private final JwtGetUserService jwtGetUserService;

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
    public AnsApiResponse<SymbolDTO> create(@Parameter(name = "symbolCreateDTO", required = true, description = "방송아이콘 등록 DTO")
                                            @Valid @RequestBody SymbolCreateDTO symbolCreateDTO,
                                            @RequestHeader(value = "Authorization", required = false) String Authorization
    ) throws Exception {

        String userId = jwtGetUserService.getUser(Authorization);

        String symbolId = symbolService.create(symbolCreateDTO, userId);

        SymbolDTO symbolDTO = symbolService.find(symbolId);

        return new AnsApiResponse<>(symbolDTO);

    }

    @Operation(summary = "방송아이콘 수정", description = "방송아이콘 수정")
    @PutMapping(path = "/{symbolId}")
    public AnsApiResponse<SymbolDTO> update(@Parameter(name = "symbolCreateDTO", required = true, description = "필수값<br>")
                                            @Valid @RequestBody SymbolUpdateDTO symbolUpdateDTO,
                                            @Parameter(name = "symbolId", required = true, description = "방송아이콘 아이디")
                                            @PathVariable("symbolId") String symbolId,
                                            @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        String userId = jwtGetUserService.getUser(Authorization);

        symbolService.update(symbolUpdateDTO, symbolId, userId);

        SymbolDTO symbolDTO = symbolService.find(symbolId);

        return new AnsApiResponse<>(symbolDTO);

    }

    @Operation(summary = "방송아이콘 삭제", description = "방송아이콘 삭제")
    @DeleteMapping(path = "/{symbolId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "symbolId", required = true, description = "방송아이콘 아이디")
                                    @PathVariable("symbolId") String symbolId,
                                    @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        String userId = jwtGetUserService.getUser(Authorization);

        symbolService.delete(symbolId, userId);

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

    @Operation(summary = "파일 프롬프터 전송", description = "파일 프롬프터 전송")
    @PostMapping(path = "/send/prompter", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void sendFileToPrompter(@RequestPart MultipartFile file,@RequestParam String symbolId,
                                   @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        String userId = jwtGetUserService.getUser(Authorization);

        log.info("Send file to prompter - symbol file name : " + file.getOriginalFilename() + " userId : " + userId);

        symbolService.sendFileToPrompter(file, symbolId);


    }

    @Operation(summary = "파일 테이커 전송", description = "파일 테이커 전송")
    @PostMapping(path = "/send/taker", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void sendFileToTaker(@RequestPart MultipartFile file, @RequestParam String fileDivCd, @RequestParam String symbolId,
                                @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        String userId = jwtGetUserService.getUser(Authorization);

        log.info("Send file to taker - symbolId : "+symbolId
                + "symbol type cd : " + fileDivCd + " userId : " + userId +" file name : "+file.getOriginalFilename());

        symbolService.sendFileToTaker(file, symbolId, fileDivCd);


    }

}
