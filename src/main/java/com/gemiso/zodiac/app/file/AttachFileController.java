package com.gemiso.zodiac.app.file;

import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.file.dto.StatusCodeFileDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.OutputStream;
import java.nio.charset.Charset;

@Api(description = "파일 API")
@RestController
@RequestMapping("/files")
@Slf4j
@RequiredArgsConstructor
public class AttachFileController {

    private final AttachFileService attachFileService;


    @Operation(summary = "파일 업로드", description = "파일 업로드")
    @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<StatusCodeFileDTO> create(@RequestPart MultipartFile file, @RequestParam String fileDivCd) {

        StatusCodeFileDTO statusCodeFileDTO = attachFileService.create(file, fileDivCd);

        return new AnsApiResponse<>(statusCodeFileDTO);
    }


    @Operation(summary = "파일 다운로드", description = "파일 다운로드")
    @GetMapping(path = "/{fileId}")
    public Response find(HttpServletRequest request, HttpServletResponse response,
                         @Parameter(name = "fileId", description = "파일 아이디") @PathVariable Long fileId,
                         @Parameter(name = "fileDivCd", description = "파일 구분코드")
                         @RequestParam(value = "fileDivCd", required = false) String fileDivCd) throws Exception {

        Response rep = null;
        AttachFileDTO attachFileDTO = attachFileService.strFilefind(fileId);

        if (attachFileDTO.getFileDivCd().equals(fileDivCd)) {
            rep = attachFileService.find(request, response, attachFileDTO, fileDivCd);
        } else {
            String errorMessage = "File does not exist";
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return null;
        }

        return rep;

    }
}
