package com.gemiso.zodiac.app.dept;

import com.gemiso.zodiac.app.dept.dto.DeptsDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "부서 API")
@RestController
@RequestMapping("/depts")
@Slf4j
@RequiredArgsConstructor
public class DeptsController {

    private final DeptsService deptsService;


    @Operation(summary = "부서 목록조회", description = "부서 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<DeptsDTO>> findAll(@Parameter(name = "name", description = "부서 명")
                                                  @RequestParam(value = "name", required = false) String name,
                                                  @Parameter(name = "isEnabled", description = "사용여부 ( N, Y )")
                                                  @RequestParam(value = "isEnabled", required = false) String isEnabled,
                                                  @Parameter(name = "parentCode", description = "부모 코드")
                                                  @RequestParam(value = "parentCode", required = false) String parentCode) {

        List<DeptsDTO> deptsDTOList = deptsService.findAll(name, isEnabled, parentCode);


        return new AnsApiResponse<>(deptsDTOList);
    }

    @Operation(summary = "부서 상세조회", description = "부서 상세조회")
    @GetMapping(path = "/{id}")
    public AnsApiResponse<DeptsDTO> find(@Parameter(name = "id", required = true, description = "단어사전 아이디")
                                         @PathVariable("id") long id) {

        DeptsDTO deptsDTO = deptsService.find(id);

        return new AnsApiResponse<>(deptsDTO);
    }
}
