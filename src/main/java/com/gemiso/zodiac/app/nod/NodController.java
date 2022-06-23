package com.gemiso.zodiac.app.nod;

import com.gemiso.zodiac.app.nod.dto.NodDTO;
import com.gemiso.zodiac.app.nod.dto.NodSimpleDTO;
import com.gemiso.zodiac.app.program.dto.ProgramCreateDTO;
import com.gemiso.zodiac.app.program.dto.ProgramDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(description = "NOD API")
@RestController
@RequestMapping("/nod")
@Slf4j
@RequiredArgsConstructor
public class NodController {

    private final NodService nodService;



    @Operation(summary = "NOD 등록", description = "NOD 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<NodDTO> create(@Parameter(name = "nodDTO", required = true, description = "필수값<br>  , ")
                                             @Valid @RequestBody NodDTO nodDTO) throws Exception {

        Long nodId = nodService.create(nodDTO);

        NodDTO returnNod = nodService.find(nodId);


        return new AnsApiResponse<>(returnNod, HttpStatus.CREATED);
    }
}
