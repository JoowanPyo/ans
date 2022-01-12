package com.gemiso.zodiac.app.anchorCap;

import com.gemiso.zodiac.app.anchorCap.dto.AnchorCapCreateDTO;
import com.gemiso.zodiac.app.anchorCap.dto.AnchorCapDTO;
import com.gemiso.zodiac.app.anchorCap.dto.AnchorCapUpdateDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "앵커자막 API")
@RestController
@RequestMapping("/anchorcaps")
@Slf4j
@RequiredArgsConstructor
public class AnchorCapController {

    private final AnchorCapService anchorCapService;


    @Operation(summary = "앵커자막 목록조회", description = "앵커자막 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<AnchorCapDTO>> findAll(@Parameter(name = "anchorCapId", description = "앵커자막 아이디")
                                                      @RequestParam(value = "anchorCapId", required = false) Long anchorCapId) {

        List<AnchorCapDTO> anchorCapDTOList = anchorCapService.findAll(anchorCapId);

        return new AnsApiResponse<>(anchorCapDTOList);
    }

    @Operation(summary = "앵커자막 상세조회", description = "앵커자막 상세조회")
    @GetMapping(path = "/{anchorCapId}")
    public AnsApiResponse<AnchorCapDTO> find(@Parameter(name = "anchorCapId", required = true, description = "앵커자막 아이디")
                                             @PathVariable("anchorCapId") Long anchorCapId) {

        AnchorCapDTO anchorCapDTO = anchorCapService.find(anchorCapId);

        return new AnsApiResponse<>(anchorCapDTO);
    }

    @Operation(summary = "앵커자막 등록", description = "앵커자막 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<AnchorCapDTO> create(@Parameter(description = "필수값<br>, ", required = true)
                                               @RequestBody AnchorCapCreateDTO anchorCapCreateDTO) {

        Long anchorCapId = anchorCapService.create(anchorCapCreateDTO);

        //앵커 자막 등록 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        AnchorCapDTO anchorCapDTO = new AnchorCapDTO();
        anchorCapDTO.setAnchorCapId(anchorCapId);

        return new AnsApiResponse<>(anchorCapDTO);
    }

    @Operation(summary = "앵커자막 수정", description = "앵커자막 수정")
    @PutMapping(path = "/{anchorCapId}")
    public AnsApiResponse<AnchorCapDTO> update(@Parameter(description = "필수값<br>, ", required = true)
                                               @RequestBody AnchorCapUpdateDTO anchorCapUpdateDTO,
                                               @Parameter(name = "anchorCapId", required = true, description = "앵커자막 아이디")
                                               @PathVariable("anchorCapId") Long anchorCapId) {

        anchorCapService.update(anchorCapUpdateDTO, anchorCapId);

        //앵커 자막 수정 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        AnchorCapDTO anchorCapDTO = new AnchorCapDTO();
        anchorCapDTO.setAnchorCapId(anchorCapId);

        return new AnsApiResponse<>(anchorCapDTO);

    }

    @Operation(summary = "앵커자막 삭제", description = "앵커자막 삭제")
    @DeleteMapping(path = "/{anchorCapId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "anchorCapId", required = true, description = "앵커자막 아이디")
                                    @PathVariable("anchorCapId") Long anchorCapId) {

        anchorCapService.delete(anchorCapId);

        return AnsApiResponse.noContent();
    }

}
