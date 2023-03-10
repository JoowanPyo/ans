package com.gemiso.zodiac.app.nod;

import com.gemiso.zodiac.app.cueSheet.dto.CueSheetNodDTO;
import com.gemiso.zodiac.app.nod.dto.NodCreateDTO;
import com.gemiso.zodiac.app.nod.dto.NodDTO;
import com.gemiso.zodiac.app.nod.dto.NodScriptSendDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Api(description = "NOD API")
@RestController
@RequestMapping("/nod")
@Slf4j
@RequiredArgsConstructor
public class NodController {

    private final NodService nodService;


    @Operation(summary = "큐시트 목록조회", description = "큐시트 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<CueSheetNodDTO>> findAll(@Parameter(description = "검색 시작 데이터 날짜(yyyy-MM-dd HH:mm:ss)", required = false)
                                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date nowDate,
                                                     @Parameter(name = "before", description = "이전시간")
                                                     @RequestParam(value = "before", required = false) Integer before,
                                                     @Parameter(name = "after", description = "이후시간")
                                                     @RequestParam(value = "after", required = false) Integer after,
                                                     @Parameter(name = "cueDivCd", description = "큐시트 구분 코드")
                                                     @RequestParam(value = "cueDivCd", required = false) String cueDivCd,
                                                     @RequestHeader(value = "securityKey") String securityKey) throws Exception {

        log.info(nowDate + " befor : "+ before+ " after : "+after );

        List<CueSheetNodDTO> cueSheetDTOList = nodService.findCue(nowDate, before, after, cueDivCd);

        return new AnsApiResponse<>(cueSheetDTOList);

    }

    @Operation(summary = "NOD 등록", description = "NOD 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<NodDTO> create(@Parameter(name = "nodDTO", required = true, description = "필수값<br>  , ")
                                         @Valid @RequestBody NodCreateDTO nodCreateDTO,
                                         @RequestHeader(value = "securityKey") String securityKey) throws Exception {

        log.info("NOD CREATE : DTO - "+nodCreateDTO.toString() );

        Long nodId = nodService.create(nodCreateDTO);

        NodDTO returnNod = nodService.find(nodId);


        return new AnsApiResponse<>(returnNod);
    }

    //사용하진 않으나 홈페이지 전송 부분은 주석처리 하여 사용하지 않음 2022-10-20
    @Operation(summary = "NOD 스크립트 홈페이지 등록", description = "NOD 스크립트 홈페이지 등록")
    @PostMapping(path = "/sendhomepage")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<?> sendScriptToHomePage(@Parameter(name = "nodDTO", required = true, description = "필수값<br>  , ")
                                         @Valid @RequestBody NodScriptSendDTO nodScriptSendDTO) throws Exception {

        log.info("SCRIPT SEND : CUESHEET ID - "+nodScriptSendDTO.toString() );

        nodService.sendScriptToHomePage(nodScriptSendDTO);


        return AnsApiResponse.ok();
    }
}
