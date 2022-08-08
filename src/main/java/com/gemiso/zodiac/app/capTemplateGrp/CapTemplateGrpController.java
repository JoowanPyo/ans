package com.gemiso.zodiac.app.capTemplateGrp;

import com.gemiso.zodiac.app.capTemplateGrp.dto.CapTemplateGrpCreateDTO;
import com.gemiso.zodiac.app.capTemplateGrp.dto.CapTemplateGrpDTO;
import com.gemiso.zodiac.app.capTemplateGrp.dto.CapTemplateGrpUpdateDTO;
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
import java.io.UnsupportedEncodingException;
import java.util.List;

@Api(description = "템플릿 API")
@RestController
@RequestMapping("/templategroups")
@Slf4j
@RequiredArgsConstructor
public class CapTemplateGrpController {

    private final CapTemplateGrpService capTemplateGrpService;

    private final JwtGetUserService jwtGetUserService;

    @Operation(summary = "템플릿 그룹 목록조회", description = "템플릿 그룹 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<CapTemplateGrpDTO>> findAll(@Parameter(name = "searchWord", description = "템플릿 그룹 이름 검색")
                                                           @RequestParam(value = "searchWord", required = false) String searchWord) {

        List<CapTemplateGrpDTO> capTemplateGrpDTOList = capTemplateGrpService.findAll(searchWord);

        return new AnsApiResponse<>(capTemplateGrpDTOList);
    }

    @Operation(summary = "템플릿 그룹 상세조회", description = "템플릿 그룹 상세조회")
    @GetMapping(path = "/{tmpltGrpId}")
    public AnsApiResponse<CapTemplateGrpDTO> find(@Parameter(name = "tmpltGrpId", description = "템플릿 그룹 아이디")
                                                  @PathVariable("tmpltGrpId") Long tmpltGrpId) {

        CapTemplateGrpDTO capTemplateGrpDTO = capTemplateGrpService.find(tmpltGrpId);

        return new AnsApiResponse<>(capTemplateGrpDTO);

    }

    @Operation(summary = "템플릿 그룹 등록", description = "템플릿 그룹 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<CapTemplateGrpDTO> create(@Parameter(name = "capTemplateGrpCreateDTO", required = true, description = "필수값<br>")
                                                    @Valid @RequestBody CapTemplateGrpCreateDTO capTemplateGrpCreateDTO,
                                                    @RequestHeader(value = "Authorization", required = false)String Authorization) throws Exception {

        String userId =jwtGetUserService.getUser(Authorization);

        Long tmpltGrpId = capTemplateGrpService.create(capTemplateGrpCreateDTO, userId);

        //자막템플릿 등록 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        CapTemplateGrpDTO capTemplateGrpDTO = new CapTemplateGrpDTO();
        capTemplateGrpDTO.setTmpltGrpId(tmpltGrpId);

        return new AnsApiResponse<>(capTemplateGrpDTO);

    }

    @Operation(summary = "템플릿 그룹 수정", description = "템플릿 그룹 수정")
    @PutMapping(path = "/{tmpltGrpId}")
    public AnsApiResponse<CapTemplateGrpDTO> update(@Parameter(name = "tmpltGrpId", description = "템플릿 그룹 아이디")
                                                    @PathVariable("tmpltGrpId") Long tmpltGrpId,
                                                    @Parameter(name = "capTemplateGrpUpdateDTO", required = true, description = "필수값<br>")
                                                    @Valid @RequestBody CapTemplateGrpUpdateDTO capTemplateGrpUpdateDTO,
                                                    @RequestHeader(value = "Authorization", required = false)String Authorization) throws Exception {

        String userId =jwtGetUserService.getUser(Authorization);

        capTemplateGrpService.update(capTemplateGrpUpdateDTO, tmpltGrpId, userId);

        //자막템플릿 수정 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        CapTemplateGrpDTO capTemplateGrpDTO = new CapTemplateGrpDTO();
        capTemplateGrpDTO.setTmpltGrpId(tmpltGrpId);

        return new AnsApiResponse<>(capTemplateGrpDTO);

    }

    @Operation(summary = "템플릿 그룹 삭제", description = "템플릿 그룹 삭제")
    @DeleteMapping(path = "/{tmpltGrpId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "tmpltGrpId", description = "템플릿 그룹 아이디")
                                    @PathVariable("tmpltGrpId") Long tmpltGrpId) {

        capTemplateGrpService.delete(tmpltGrpId);

        return AnsApiResponse.noContent();
    }
}
