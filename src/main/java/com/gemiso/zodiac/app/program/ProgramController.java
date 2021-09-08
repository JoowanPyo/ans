package com.gemiso.zodiac.app.program;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/programs")
@Log4j2
@RequiredArgsConstructor
//@Tag(name = "User Controllers", description = "User API")
@Api(description = "프로그램 API")
public class ProgramController {
}
