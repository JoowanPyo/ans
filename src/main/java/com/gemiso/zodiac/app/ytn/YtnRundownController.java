package com.gemiso.zodiac.app.ytn;

import com.gemiso.zodiac.app.yonhapWire.dto.YonhapReuterCreateDTO;
import com.gemiso.zodiac.app.yonhapWire.dto.YonhapWireResponseDTO;
import com.gemiso.zodiac.app.ytn.dto.YtnRundownCreateDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ytnrundown")
@Slf4j
@RequiredArgsConstructor
@Api(description = "YTN 런다운 API")
public class YtnRundownController {

    private final YtnRundownService ytnRundownService;


    @Operation(summary = "YTN 런다운 등록", description = "YTN 런다운 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Parameter(description = "필수값<br> ", required = true)
                                    @RequestBody YtnRundownCreateDTO ytnRundownCreateDTO) {

        Long id = ytnRundownService.create(ytnRundownCreateDTO);


        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}
