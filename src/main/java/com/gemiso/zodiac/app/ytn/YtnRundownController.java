package com.gemiso.zodiac.app.ytn;

import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ytnrundown")
@Slf4j
@RequiredArgsConstructor
@Api(description = "YTN 런다운 API")
public class YtnRundownController {

    private final YtnRundownService ytnRundownService;


    /*@Operation(summary = "YTN 런다운 등록", description = "YTN 런다운 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(){



        return new ResponseEntity<>();
    }*/
}
