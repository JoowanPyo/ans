package com.gemiso.zodiac.app.dept;

import com.gemiso.zodiac.app.dept.dto.DeptsDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "부서 API")
@RestController
@RequestMapping("/depts")
@Slf4j
@RequiredArgsConstructor
public class DeptsController {

    private final DeptsService deptsService;


    @Operation(summary = "부서 상세조회", description = "부서 상세조회")
    @GetMapping(path = "/{id}")
    public AnsApiResponse<DeptsDTO> find(@Parameter(name = "id", required = true, description = "단어사전 아이디")
                                         @PathVariable("id") long id) {

        return null;
    }
}
