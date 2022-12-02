package com.gemiso.zodiac.app.fileFtpInfo;

import com.gemiso.zodiac.app.fileFtpInfo.dto.FileFtpInfoDTO;
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

@Api(description = "파일 FTP 정보 조회 API")
@RestController
@RequestMapping("/fileftpinfo")
@Slf4j
@RequiredArgsConstructor
public class FileFtpInfoController {

    private final FileFtpInfoService fileFtpInfoService;


    @Operation(summary = "파일 FTP 정보 상세 상세조회", description = "파일 FTP 정보 상세 상세조회")
    @GetMapping(path = "/{id}")
    public AnsApiResponse<FileFtpInfoDTO> find(@Parameter(name = "id", required = true, description = "스크롤 뉴스 상세 아이디",
            in = ParameterIn.PATH) @PathVariable("id") Long id) {

        FileFtpInfoDTO fileFtpInfoDTO = fileFtpInfoService.find(id);

        return new AnsApiResponse<>(fileFtpInfoDTO);
    }
}
