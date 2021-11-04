package com.gemiso.zodiac.app.facilityManage;

import com.gemiso.zodiac.app.facilityManage.dto.FacilityManageCreateDTO;
import com.gemiso.zodiac.app.facilityManage.dto.FacilityManageDTO;
import com.gemiso.zodiac.app.facilityManage.dto.FacilityManageResponseDTO;
import com.gemiso.zodiac.app.facilityManage.dto.FacilityManageUpdateDTO;
import com.gemiso.zodiac.core.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "시설관리 API")
@RestController
@RequestMapping("/facility")
@Slf4j
@RequiredArgsConstructor
public class FacilityManageController {

    private final FacilityManageService facilityManageService;


    @Operation(summary = "시설관리 목록조회", description = "시설관리 목록조회")
    @GetMapping(path = "")
    public ApiResponse<List<FacilityManageDTO>> findAll(@Parameter(name = "searchWord", description = "검색어[시설 이름]")
                                                        @RequestParam(value = "searchWord", required = false) String searchWord,
                                                        @Parameter(name = "fcltyDivCd", description = "구분코드")
                                                        @RequestParam(value = "fcltyDivCd", required = false) String fcltyDivCd) {

        List<FacilityManageDTO> facilityManageDTOList = facilityManageService.findAll(searchWord, fcltyDivCd);

        return new ApiResponse<>(facilityManageDTOList);
    }

    @Operation(summary = "시설관리 상세조회", description = "시설관리 상세조회")
    @GetMapping(path = "/{fcltyId}")
    public ApiResponse<FacilityManageDTO> find(@Parameter(name = "fcltyId", required = true, description = "시설관리 아이디")
                                               @PathVariable("fcltyId") long fcltyId) {

        FacilityManageDTO facilityManageDTO = facilityManageService.find(fcltyId);

        return new ApiResponse<>(facilityManageDTO);
    }

    @Operation(summary = "시설관리 등록", description = "시설관리 등록")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<FacilityManageResponseDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                                         @RequestBody FacilityManageCreateDTO facilityManageCreateDTO) {

        FacilityManageResponseDTO responseDTO = new FacilityManageResponseDTO();

        Long fcltyId = facilityManageService.create(facilityManageCreateDTO);
        responseDTO.setFcltyId(fcltyId);

        return new ApiResponse<>(responseDTO);
    }

    @Operation(summary = "시설관리 수정", description = "시설관리 수정")
    @PutMapping(path = "/{fcltyId}")
    public ApiResponse<FacilityManageResponseDTO> update(@Parameter(name = "fcltyId", required = true, description = "시설관리 아이디")
                                                         @PathVariable("fcltyId") long fcltyId,
                                                         @Parameter(description = "필수값<br> ", required = true)
                                                         @RequestBody FacilityManageUpdateDTO facilityManageUpdateDTO) {

        FacilityManageResponseDTO responseDTO = new FacilityManageResponseDTO();

        facilityManageService.update(facilityManageUpdateDTO, fcltyId);

        responseDTO.setFcltyId(fcltyId);

        return new ApiResponse<>(responseDTO);
    }
}
