package com.gemiso.zodiac.app.rundownItem;

import com.gemiso.zodiac.app.rundownItem.dto.RundownItemCreateDTO;
import com.gemiso.zodiac.app.rundownItem.dto.RundownItemDTO;
import com.gemiso.zodiac.app.rundownItem.dto.RundownItemUpdateDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.gemiso.zodiac.core.service.JwtGetUserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(description = "런다운 아이템 API")
@RestController
@RequestMapping("/rundownitem")
@Slf4j
@RequiredArgsConstructor
public class RundownItemController {

    private final RundownItemService rundownItemService;

    private final JwtGetUserService jwtGetUserService;


    @Operation(summary = "런다운 아이템 목록조회", description = "런다운 아이템 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<RundownItemDTO>> findAll(@Parameter(name = "rundownId", description = "런다운 아이디")
                                                        @RequestParam(value = "rundownId", required = false) Long rundownId) {

        List<RundownItemDTO> rundownItemDTOS = rundownItemService.findAll(rundownId);

        return new AnsApiResponse<>(rundownItemDTOS);
    }

    @Operation(summary = "런다운 아이템 상세조회", description = "런다운 아이템 상세조회")
    @GetMapping(path = "/{rundownId}")
    public AnsApiResponse<RundownItemDTO> find(@Parameter(name = "rundownId", description = "런다운 아이디", required = true)
                                               @PathVariable("rundownId") Long rundownId) throws Exception {

        RundownItemDTO rundownItemDTO = rundownItemService.find(rundownId);

        return new AnsApiResponse<>(rundownItemDTO);

    }

    @Operation(summary = "런다운 아이템 등록", description = "런다운 아이템 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<RundownItemDTO> create(@Parameter(name = "rundownCreateDTO", required = true, description = "필수값<br>  , ")
                                                 @Valid @RequestBody RundownItemCreateDTO rundownItemCreateDTO,
                                                 @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        String userId = jwtGetUserService.getUser(Authorization);

        log.info("RUNDOWN ITEM CREATE : DTO - " + rundownItemCreateDTO.toString() + " UserId : " + userId);

        Long rundownId = rundownItemService.create(rundownItemCreateDTO, userId);

        RundownItemDTO rundownItemDTO = rundownItemService.find(rundownId);


        return new AnsApiResponse<>(rundownItemDTO);
    }

    @Operation(summary = "런다운 아이템 수정", description = "런다운 아이템 수정")
    @PutMapping(path = "/{rundownItemId}")
    public AnsApiResponse<RundownItemDTO> update(@Parameter(name = "rundownUpdateDTO", required = true, description = "필수값<br>")
                                                 @Valid @RequestBody RundownItemUpdateDTO rundownItemUpdateDTO,
                                                 @Parameter(name = "rundownItemId", required = true) @PathVariable("rundownItemId") Long rundownItemId,
                                                 @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        String userId = jwtGetUserService.getUser(Authorization);

        log.info("RUNDOWN Item UPDATE : DTO - " + rundownItemUpdateDTO.toString() + " UserId : " + userId);

        rundownItemService.update(rundownItemUpdateDTO, rundownItemId);

        RundownItemDTO rundownItemDTO = rundownItemService.find(rundownItemId);

        return new AnsApiResponse<>(rundownItemDTO);
    }

    @Operation(summary = "런다운 아이템 삭제", description = "런다운 아이템 삭제")
    @DeleteMapping(path = "/{rundownItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "rundownItemId", description = "런다운 아이템 아이디")
                                    @PathVariable("rundownItemId") Long rundownItemId,
                                    @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        String userId = jwtGetUserService.getUser(Authorization);

        log.info("RUNDOWN Item Delete : rundownItemId - " + rundownItemId + " UserId : " + userId);

        rundownItemService.delete(rundownItemId);
        //rundownService.delete(rundownId, userId);


        return AnsApiResponse.noContent();
    }

    @Operation(summary = "런다운 아이템 순서변경", description = "런다운 아이템 순서변경")
    @PutMapping(path = "/{rundownItemId}/ord")
    public AnsApiResponse<?> ordUpdate(@Parameter(name = "rundownItemId", description = "런다운 아이템 아이디")
                                       @PathVariable("rundownItemId") Long rundownItemId,
                                       @Parameter(name = "rundownId", description = "런다운 아이디")
                                       @RequestParam(value = "rundownId", required = false) Long rundownId,
                                       @Parameter(name = "rundownItemOrd", description = "런다운 아이템 순번")
                                       @RequestParam(value = "rundownItemOrd", required = false) Integer rundownItemOrd,
                                       @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = jwtGetUserService.getUser(Authorization);
        log.info("RUNDOWN Item Order Update : rundownId - " + rundownId + " rundownItemId - " + rundownItemId + " rundownItemOrd - " + rundownItemOrd + " UserId : " + userId);

        rundownItemService.ordUpdate(rundownId, rundownItemId, rundownItemOrd);

        return AnsApiResponse.ok();

    }
}
