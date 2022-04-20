package com.gemiso.zodiac.app.lbox;

import com.gemiso.zodiac.app.lbox.categoriesDTO.CategoriesDataDTO;
import com.gemiso.zodiac.app.lbox.contentDTO.DataDTO;
import com.gemiso.zodiac.app.lbox.userInfoDTO.UserInfoDataDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "엘박스 API")
@RestController
@RequestMapping("/lbox")
@RequiredArgsConstructor
@Slf4j
public class LboxController {

    private final LboxService lboxService;

    @Operation(summary = "엘박스 영상 목록 조회", description = "엘박스 영상 목록 조회")
    @GetMapping(path = "")
    public AnsApiResponse<JSONObject> findAll(@Parameter(name = "sdate", description = "검색 시작 데이터 날짜(yyyy-MM-dd)")
                                     @RequestParam(value = "sdate", required = false) String sdate,
                                              @Parameter(name = "edate", description = "검색 시작 데이터 날짜(yyyy-MM-dd)")
                                     @RequestParam(value = "edate", required = false) String edate) throws Exception {

        JSONObject content = lboxService.findAll(sdate, edate);


        return new AnsApiResponse<>(content);
    }

    @Operation(summary = "엘박스 카테고리 조회", description = "엘박스 카테고리 조회")
    @GetMapping(path = "/categories")
    public AnsApiResponse<JSONObject> findCategories(@Parameter(name = "parentId", description = "parentId")
                                           @RequestParam(value = "parentId", required = false) Integer parentId) throws Exception {

        JSONObject categoriesData = lboxService.findCategories(parentId);


        return new AnsApiResponse<>(categoriesData);
    }

    @Operation(summary = "엘박스 사용자정보 조회", description = "엘박스 사용자정보 조회")
    @GetMapping(path = "/userinfo")
    public AnsApiResponse<JSONObject> findUserInfo() throws Exception {

        JSONObject userInfoData = lboxService.findUserInfo();


        return new AnsApiResponse<>(userInfoData);
    }
}
