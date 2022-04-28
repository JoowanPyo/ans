package com.gemiso.zodiac.app.mediaTransport;

import com.gemiso.zodiac.app.article.dto.ArticleAuthConfirmDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "부조전송 API")
@RestController
@RequestMapping("/transport")
@RequiredArgsConstructor
@Slf4j
public class MediaTransportController {

    private final MediaTransportService mediaTransportService;

    @Operation(summary = "부조 전송", description = "부조 전송")
    @PutMapping(path = "/")
    public AnsApiResponse<ArticleAuthConfirmDTO> articleConfirm(@Parameter(name = "artclId", required = true, description = "기사 아이디")
                                                                @PathVariable("artclId") Long artclId) {



        return null;
    }


}
