package com.gemiso.zodiac.app.breakingNewsFtpInfo;

import com.gemiso.zodiac.app.breakingNewsFtpInfo.dto.BreakingNewsFtpInfoDTO;
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

@Api(description = "속보뉴스 FPT 전송정보 API")
@RestController
@RequestMapping("/breaingnewsftpinfo")
@Slf4j
@RequiredArgsConstructor
public class BreakingNewsFtpInfoController {

    private final BreakingNewsFtpInfoService breakingNewsFtpInfoService;

    @Operation(summary = "속보뉴스 FPT 전송정보 상세조회", description = "속보뉴스 FPT 전송정보 상세조회")
    @GetMapping(path = "/{id}")
    public AnsApiResponse<BreakingNewsFtpInfoDTO> find(@Parameter(name = "id", required = true, description = "속보뉴스 FPT 전송정보 아이디",
            in = ParameterIn.PATH) @PathVariable("id") Long id) {

        BreakingNewsFtpInfoDTO breakingNewsFtpInfoDTO = breakingNewsFtpInfoService.find(id);

        return new AnsApiResponse<>(breakingNewsFtpInfoDTO);
    }
}
