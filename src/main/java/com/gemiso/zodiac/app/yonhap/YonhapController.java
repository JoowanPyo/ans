package com.gemiso.zodiac.app.yonhap;

import com.gemiso.zodiac.app.yonhap.dto.YonhapCreateDTO;
import com.gemiso.zodiac.app.yonhap.dto.YonhapDTO;
import com.gemiso.zodiac.core.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/yonhap")
@Log4j2
@RequiredArgsConstructor
@Slf4j
public class YonhapController {

    private final YonhapService yonhapService;

    @Operation(summary = "연합 목록조회", description = "연합 목록조회")
    @GetMapping(path = "")
    public ApiResponse<List<YonhapDTO>> findAll(@Parameter(name = "sdate", description = "검색시작일[yyyy-MM-dd HH:mm:ss]", required = true)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                @Parameter(name = "edate", description = "검색종료일[yyyy-MM-dd HH:mm:ss]", required = true)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                @Parameter(name = "artcl_cate_cds", description = "분류코드", required = true)
                                                @RequestParam(value = "artcl_cate_cds", required = false) List<String> artcl_cate_cds,
                                                @Parameter(name = "region_cds", description = "통신사코드", required = true)
                                                @RequestParam(value = "region_cds", required = false) List<String> region_cds,
                                                @Parameter(name = "search_word", description = "통신사코드", required = true)
                                                @RequestParam(value = "search_word", required = false) String search_word) {

        List<YonhapDTO> yonhapDTOList = yonhapService.findAll(sdate, edate, artcl_cate_cds, region_cds, search_word);

        return new ApiResponse<>(yonhapDTOList);
    }

    @Operation(summary = "연합 등록", description = "연합 등록")
    @PostMapping(path = "")
    public ResponseEntity<?> create(@Parameter(description = "필수값<br> ", required = true) @RequestBody YonhapCreateDTO yonhapCreateDTO
    ) throws Exception {

        YonhapDTO yonhapDTO = new YonhapDTO();

        try {
            Long yonhapId = yonhapService.create(yonhapCreateDTO);

            yonhapDTO = yonhapService.find(yonhapId);

        }catch(Exception e){
            e.printStackTrace();
            log.error("yonhap : " + e.getMessage());
            return new ResponseEntity<YonhapDTO>(yonhapDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<YonhapDTO>(yonhapDTO, HttpStatus.CREATED);

    }
}
