package com.gemiso.zodiac.app.scrollNewsFtpInfo;

import com.gemiso.zodiac.app.scrollNewsFtpInfo.dto.ScrollNewsFtpInfoDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "스크롤 뉴스 FTP정보 API")
@RestController
@RequestMapping("/scrollnewsftpinfo")
@RequiredArgsConstructor
@Slf4j
public class ScrollNewsFtpInfoController {

    private final ScrollNewsFtpInfoService scrollNewsFtpInfoService;


    @Operation(summary = "스크롤 뉴스 FTP 정보 상세 상세조회", description = "스크롤 뉴스 FTP 정보 상세 상세조회")
    @GetMapping(path = "/{id}")
    public AnsApiResponse<ScrollNewsFtpInfoDTO> find(@Parameter(name = "id", required = true, description = "스크롤 뉴스 상세 아이디",
            in = ParameterIn.PATH) @PathVariable("id") Long id) {

        ScrollNewsFtpInfoDTO scrollNewsFtpInfoDTO = scrollNewsFtpInfoService.find(id);

        return new AnsApiResponse<>(scrollNewsFtpInfoDTO);
    }
}
