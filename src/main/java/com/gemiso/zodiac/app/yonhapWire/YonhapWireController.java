package com.gemiso.zodiac.app.yonhapWire;

import com.gemiso.zodiac.app.yonhapWire.dto.YonhapWireCreateDTO;
import com.gemiso.zodiac.app.yonhapWire.dto.YonhapWireDTO;
import com.gemiso.zodiac.core.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/yonhapInternational")
@Log4j2
@RequiredArgsConstructor
//@Tag(name = "User Controllers", description = "User API")
@Api(description = "연합 외신 API")
public class YonhapWireController {

    private final YonhapWireService yonhapWireService;

    @Operation(summary = "연합외신 목록조회", description = "연합외신 목록조회")
    @GetMapping(path = "")
    public ApiResponse<List<YonhapWireDTO>> findAll(@Parameter(name = "sdate", description = "검색시작일[yyyy-MM-dd HH:mm:ss]", required = true)
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date sdate,
                                                    @Parameter(name = "edate", description = "검색종료일[yyyy-MM-dd HH:mm:ss]", required = true)
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date edate,
                                                    @Parameter(name = "agcyCd", description = "통신사코드", required = true)
                                                    @RequestParam(value = "agcyCd", required = false) String agcyCd,
                                                    @Parameter(name = "searchWord", description = "검색어", required = true)
                                                    @RequestParam(value = "searchWord", required = false) String searchWord,
                                                    @Parameter(name = "imprt", description = "중요도 List<String>", required = true)
                                                    @RequestParam(value = "imprt", required = false) List<String> imprtList) {

        List<YonhapWireDTO> yonhapWireDTOList = yonhapWireService.findAll(sdate, edate, agcyCd, searchWord, imprtList);

        return new ApiResponse<>(yonhapWireDTOList);
    }

    @Operation(summary = "연합외신 등록", description = "연합외신 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Parameter(description = "필수값<br> ", required = true)
                                    @RequestBody YonhapWireCreateDTO yonhapWireCreateDTO /*UriComponentsBuilder ucBuilder*/
    ) {
        /* HttpHeaders headers = null;*/

        YonhapWireDTO yonhapWireDTO = new YonhapWireDTO();

        try {
            Long yhWireId = yonhapWireService.create(yonhapWireCreateDTO);

            if (!ObjectUtils.isEmpty(yhWireId)) {

               /* headers = new HttpHeaders();
                headers.setLocation(ucBuilder.path("/yonhapInternational/{yh_artcl_id}").buildAndExpand(yh_artcl_id).toUri());
*/
                yonhapWireDTO = yonhapWireService.find(yhWireId);
            }


        } catch (Exception e) {
            e.printStackTrace();
            log.error("yonhap : " + e.getMessage());
            return new ResponseEntity<YonhapWireDTO>(yonhapWireDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<YonhapWireDTO>(yonhapWireDTO, HttpStatus.CREATED);
    }

}
