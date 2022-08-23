package com.gemiso.zodiac.app.anchorCapHist;

import com.gemiso.zodiac.app.anchorCapHist.dto.AnchorCapHistDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(description = "앵커자막 이력 API")
@RestController
@RequestMapping("/anchorcaphist")
@Slf4j
@RequiredArgsConstructor
public class AnchorCapHistController {

    private final AnchorCapHistService anchorCapHistService;


    @Operation(summary = "앵커자막 이력 목록조회", description = "앵커자막 이력 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<AnchorCapHistDTO>> findAll(@Parameter(name = "artclHistId", description = "기사이력 아이디")
                                                          @RequestParam(value = "artclHistId", required = false) Long artclHistId) {

        List<AnchorCapHistDTO> anchorCapHistDTOList = new ArrayList<>();

        if (ObjectUtils.isEmpty(artclHistId)){
            return new AnsApiResponse<>(anchorCapHistDTOList);
        }

        anchorCapHistDTOList =anchorCapHistService.findAll(artclHistId);

        return new AnsApiResponse<>(anchorCapHistDTOList);
    }

    @Operation(summary = "앵커자막 이력 상세조회", description = "앵커자막 이력 상세조회")
    @GetMapping(path = "/{ancCapHistId}")
    public AnsApiResponse<AnchorCapHistDTO> find(@Parameter(name = "ancCapHistId", required = true, description = "앵커자막 이력 아이디")
                                                 @PathVariable("ancCapHistId") Long ancCapHistId) {

        AnchorCapHistDTO anchorCapHistDTO = anchorCapHistService.find(ancCapHistId);

        return new AnsApiResponse<>(anchorCapHistDTO);
    }
}
