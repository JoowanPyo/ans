package com.gemiso.zodiac.app.cueCapTmplt;

import com.gemiso.zodiac.app.cueCapTmplt.dto.CueCapTmpltCreateDTO;
import com.gemiso.zodiac.app.cueCapTmplt.dto.CueCapTmpltDTO;
import com.gemiso.zodiac.app.cueCapTmplt.dto.CueCapTmpltUpdateDTO;
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

@Api(description = "템플릿 API")
@RestController
@RequestMapping("/cuecaptmplt")
@Slf4j
@RequiredArgsConstructor
public class CueCapTmpltController {

    private final CueCapTmpltService cueCapTmpltService;


    @Operation(summary = "큐시트 자막 템플릿 목록조회", description = "큐시트 자막 템플릿 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<CueCapTmpltDTO>> findAll(@Parameter(name = "brdcPgmId", description = "방송프로그램 아이디")
                                                        @RequestParam(value = "brdcPgmId", required = false) String brdcPgmId) {

        List<CueCapTmpltDTO> cueCapTmpltDTOList = cueCapTmpltService.findAll(brdcPgmId);

        return new AnsApiResponse<>(cueCapTmpltDTOList);
    }

    @Operation(summary = "큐시트 자막 템플릿 상세조회", description = "큐시트 자막 템플릿 상세조회")
    @GetMapping(path = "/{id}")
    public AnsApiResponse<CueCapTmpltDTO> find(@Parameter(name = "id", description = "큐시트 자막 템플릿 아이디")
                                               @PathVariable("id") Long id) {

        CueCapTmpltDTO cueCapTmpltDTO = cueCapTmpltService.find(id);

        return new AnsApiResponse<>(cueCapTmpltDTO);
    }

    @Operation(summary = "큐시트 자막 템플릿 등록", description = "큐시트 자막 템플릿 등록")
    @PostMapping(path = "/{brdcPgmId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<CueCapTmpltDTO> creata(@Parameter(name = "cueCapTmpltCreateDTO", required = true, description = "필수값<br>")
                                                 @Valid @RequestBody List<CueCapTmpltCreateDTO> cueCapTmpltCreateDTOList,
                                                 @Parameter(name = "brdcPgmId", description = "방송프로그램 아이디")
                                                 @PathVariable("brdcPgmId") String brdcPgmId) {

        CueCapTmpltDTO cueCapTmpltDTO = cueCapTmpltService.create(cueCapTmpltCreateDTOList, brdcPgmId);


        return new AnsApiResponse<>(cueCapTmpltDTO);
    }

    @Operation(summary = "큐시트 자막 템플릿 수정", description = "큐시트 자막 템플릿 수정")
    @PutMapping(path = "/{id}")
    public AnsApiResponse<CueCapTmpltDTO> update(@Parameter(name = "cueCapTmpltCreateDTO", required = true, description = "필수값<br>")
                                                 @Valid @RequestBody CueCapTmpltUpdateDTO cueCapTmpltUpdateDTO,
                                                 @Parameter(name = "id", description = "큐시트 자막 템플릿 아이디")
                                                 @PathVariable("id") Long id) {

        CueCapTmpltDTO cueCapTmpltDTO = cueCapTmpltService.update(cueCapTmpltUpdateDTO, id);

        return new AnsApiResponse<>(cueCapTmpltDTO);

    }

    @Operation(summary = "큐시트 자막 템플릿 삭제", description = "큐시트 자막 템플릿 삭제")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "id", description = "큐시트 자막 템플릿 아이디")
                                    @PathVariable("id") Long id) {

        cueCapTmpltService.delete(id);

        return AnsApiResponse.noContent();
    }
}
